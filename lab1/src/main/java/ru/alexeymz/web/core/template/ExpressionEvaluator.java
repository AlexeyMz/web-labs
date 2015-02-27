package ru.alexeymz.web.core.template;

import ru.alexeymz.web.core.utils.EscapeUtils;

import java.util.Map;

@FunctionalInterface
public interface ExpressionEvaluator {

    Object evaluate(String expression);

    public static ExpressionEvaluator fromMap(Map<String, Object> elements) {
        return fromMap(elements, null);
    }

    public static ExpressionEvaluator fromMap(
            Map<String, Object> elements, ExpressionEvaluator parent) {
        return key -> {
            Object value = elements.get(key);
            if (value == null && parent != null) {
                return parent.evaluate(key);
            }
            return value;
        };
    }
}
