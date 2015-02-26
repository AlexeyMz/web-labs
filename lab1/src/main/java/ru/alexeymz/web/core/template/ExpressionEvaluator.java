package ru.alexeymz.web.core.template;

@FunctionalInterface
public interface ExpressionEvaluator {
    String evaluate(String expression);
}
