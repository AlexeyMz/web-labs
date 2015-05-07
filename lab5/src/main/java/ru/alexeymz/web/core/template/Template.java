package ru.alexeymz.web.core.template;

import java.util.ResourceBundle;

public interface Template {
    String render(ResourceBundle localization, ExpressionEvaluator evaluator);
}
