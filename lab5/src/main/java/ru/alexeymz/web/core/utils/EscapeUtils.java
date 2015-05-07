package ru.alexeymz.web.core.utils;


import java.lang.reflect.Array;
import java.util.Arrays;

public final class EscapeUtils {
    private EscapeUtils() {}

    /**
     * Returns a string with HTML special characters replaced by their entity
     * equivalents.
     *
     * @param str
     *          the string to escape
     * @return a new string without HTML special characters
     */
    public static String escapeHTML(String str) {
        if (str == null || str.length() == 0)
            return "";

        StringBuffer buf = new StringBuffer();
        int len = str.length();
        for (int i = 0; i < len; ++i) {
            char c = str.charAt(i);
            switch (c) {
                case '&':
                    buf.append("&amp;");
                    break;
                case '<':
                    buf.append("&lt;");
                    break;
                case '>':
                    buf.append("&gt;");
                    break;
                case '"':
                    buf.append("&quot;");
                    break;
                case '\'':
                    buf.append("&apos;");
                    break;
                default:
                    buf.append(c);
                    break;
            }
        }
        return buf.toString();
    }

    public static String escapeWithNewlines(String text) {
        return escapeHTML(text).replaceAll("(\\r)?\\n", "<br/>");
    }

    public static String escapeWithParagraphs(String text) {
        return "<p>" + escapeHTML(text).replaceAll(
                "(\\r)?\\n", "</p>\r\n<p>") + "</p>";
    }

    public static String wrapNull(String input) {
        return input == null ? "" : input;
    }

    public static String joinParameterValues(String[] requestParameterValues) {
        if (requestParameterValues.length == 0) {
            return "";
        } else if (requestParameterValues.length == 1) {
            return requestParameterValues[0];
        } else {
            return Arrays.stream(requestParameterValues).reduce("", (acc, s) -> acc + "," + s);
        }
    }
}
