package ru.alexeymz.web.core.utils;


import sun.util.resources.CalendarData;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAccessor;
import java.util.*;

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

    public static String calendarToISO8601(Calendar calendar) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mmX")
                .withZone(ZoneOffset.UTC).format(calendar.toInstant());
    }

    public static String calendarToLocal(Calendar calendar, Locale locale, int minuteOffset) {
        ZoneId zoneId = ZoneId.ofOffset("UTC", ZoneOffset.ofTotalSeconds(minuteOffset * 60));
        return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                .withLocale(locale).format(LocalDateTime.ofInstant(
                        calendar.toInstant(), zoneId));
    }

    /**
     * Decodes the passed UTF-8 String using an algorithm that's compatible with
     * JavaScript's <code>decodeURIComponent</code> function. Returns
     * <code>null</code> if the String is <code>null</code>.
     *
     * @param s The UTF-8 encoded String to be decoded
     * @return the decoded String
     */
    public static String decodeURIComponent(String s) {
        if (s == null) { return null; }
        try {
            return URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // This exception should never occur.
            throw new RuntimeException(e);
        }
    }

    /**
     * Encodes the passed String as UTF-8 using an algorithm that's compatible
     * with JavaScript's <code>encodeURIComponent</code> function. Returns
     * <code>null</code> if the String is <code>null</code>.
     *
     * @param s The String to be encoded
     * @return the encoded String
     */
    public static String encodeURIComponent(String s) {
        if (s == null) { return null; }
        try {
            return URLEncoder.encode(s, "UTF-8")
                    .replaceAll("\\+", "%20")
                    .replaceAll("\\%21", "!")
                    .replaceAll("\\%27", "'")
                    .replaceAll("\\%28", "(")
                    .replaceAll("\\%29", ")")
                    .replaceAll("\\%7E", "~");
        } catch (UnsupportedEncodingException e) {
            // This exception should never occur.
            throw new RuntimeException(e);
        }
    }

    public static String toJsonObject(Object object) {
        StringBuilder builder = new StringBuilder();
        appendJsonObject(builder, object);
        return builder.toString();
    }

    @SuppressWarnings("unchecked")
    private static void appendJsonObject(StringBuilder builder, Object object) {
        if (object == null) {
            builder.append("null");
        } else if (object.getClass().isArray()) {
            builder.append("[");
            for (int i = 0; i < Array.getLength(object); i++) {
                if (i != 0) { builder.append(", "); }
                appendJsonObject(builder, Array.get(object, i));
            }
            builder.append("]");
        } else if (object instanceof Iterable) {
            Iterable iterable = (Iterable)object;
            Iterator iterator = iterable.iterator();
            builder.append("[");
            if (iterator.hasNext()) {
                appendJsonObject(builder, iterator.next());
            }
            while (iterator.hasNext()) {
                builder.append(", ");
                appendJsonObject(builder, iterator.next());
            }
            builder.append("]");
        } else if (object instanceof Map) {
            Map map = (Map)object;
            Iterator<Map.Entry> iterator = map.entrySet().iterator();
            boolean first = true;
            builder.append("{");
            while (iterator.hasNext()) {
                if (first) { first = false; }
                else { builder.append(", "); }
                Map.Entry entry = iterator.next();
                builder
                    .append("\"")
                    .append(escapeJsonString(entry.getKey().toString()))
                    .append("\": ");
                appendJsonObject(builder, entry.getValue());
            }
            builder.append("}");
        } else if (object instanceof Number) {
            builder.append(object.toString());
        } else {
            builder
                .append("\"")
                .append(escapeJsonString(object.toString()))
                .append("\"");
        }
    }

    private static String escapeJsonString(String unescapedText) {
        return unescapedText
            .replaceAll("\"", "\\")
            .replaceAll("\r", "\\r")
            .replaceAll("\n", "\\n")
            .replaceAll("\\\\", "\\\\");
    }
}
