package ru.alexeymz.web.core.template;

import java.util.ResourceBundle;

public final class Unescaped implements Template {
    private final String markup;

    public Unescaped(String markup) {
        this.markup = markup;
    }

    @Override
    public String render(ResourceBundle localization, ExpressionEvaluator evaluator) {
        return markup;
    }
}
