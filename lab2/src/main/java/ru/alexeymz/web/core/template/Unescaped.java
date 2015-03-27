package ru.alexeymz.web.core.template;

import ru.alexeymz.web.core.utils.EscapeUtils;

import java.util.ResourceBundle;

public final class Unescaped implements Template {
    private final String markup;

    public Unescaped(String markup) {
        this.markup = markup;
    }

    public static Template escapedWithNewLines(String text) {
        return new Unescaped(EscapeUtils.escapeHTML(text)
                .replaceAll("(\\r)?\\n", "<br/>"));
    }

    @Override
    public String render(ResourceBundle localization, ExpressionEvaluator evaluator) {
        return markup;
    }
}
