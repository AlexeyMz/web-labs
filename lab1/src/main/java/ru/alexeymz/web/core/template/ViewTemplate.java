package ru.alexeymz.web.core.template;

import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ViewTemplate {
    private static final Pattern EXPRESSION_PATTERN = Pattern.compile(
            "[\\$#]\\{([^\\}<>]+)\\}");

    private final String template;

    private ViewTemplate(String template) {
        this.template = template;
    }

    public static ViewTemplate fromResource(Class klass, String resourceName) throws IOException {
        try (InputStream is = klass.getClassLoader().getResourceAsStream(resourceName)) {
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
                replacement = evaluator.evaluate(key);
            }
            matcher.appendReplacement(result, replacement);
        }
        matcher.appendTail(result);
        return result.toString();
    }
}
