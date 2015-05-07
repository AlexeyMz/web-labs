package ru.alexeymz.web.core.template;

import ru.alexeymz.web.core.utils.EscapeUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ViewTemplate implements Template {
    private static final Pattern EXPRESSION_PATTERN = Pattern.compile(
            "[\\$#]\\{([^\\}<>]+)\\}");

    private final String template;

    private ViewTemplate(String template) {
        this.template = template;
    }

    public static ViewTemplate fromResource(
            ClassLoader resourceLoader, String resourceName) throws IOException {
        try (InputStream is = resourceLoader.getResourceAsStream(resourceName)) {
            return fromStream(is);
        }
    }

    public static ViewTemplate fromStream(InputStream is) throws IOException {
        return new ViewTemplate(readTemplate(is));
    }

    private static String readTemplate(InputStream is) throws IOException {
        try (Scanner scanner = new Scanner(is, "UTF-8")) {
            // Java Way to read stream to end
            return scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
        }
    }

    public String render(ResourceBundle localization, ExpressionEvaluator evaluator) {
        Matcher matcher = EXPRESSION_PATTERN.matcher(template);
        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            String expression = matcher.group(0);
            String key = matcher.group(1);

            String replacement = expression;
            if (expression.startsWith("#")) {
                if (localization.containsKey(key)) {
                    replacement = localization.getString(key);
                }
            } else if (expression.startsWith("$")) {
                replacement = renderValue(evaluator.evaluate(key), localization, evaluator);
            }
            matcher.appendReplacement(result, replacement);
        }
        matcher.appendTail(result);
        return result.toString();
    }

    private static String renderValue(
            Object value, ResourceBundle localization, ExpressionEvaluator evaluator) {
        if (value instanceof String) {
            return EscapeUtils.escapeHTML((String) value);
        } else if (value instanceof Template) {
            Template template = (Template)value;
            return template.render(localization, evaluator);
        } else {
            return value == null ? "" : value.toString();
        }
    }

    public <T> Template forEach(Collection<? extends T> items,
                                BiConsumer<T, Map<String, Object>> bagFiller) {
        return (localization, evaluator) -> {
            StringBuilder builder = new StringBuilder();
            for (T item : items) {
                Map<String, Object> bag = new HashMap<>();
                bagFiller.accept(item, bag);
                builder.append(render(
                    localization, ExpressionEvaluator.fromMap(bag, evaluator)));
            }
            return builder.toString();
        };
    }
}
