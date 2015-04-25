package ru.alexeymz.web.config;

import ru.alexeymz.web.core.HttpFilter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@WebFilter(
    urlPatterns = {"/*"},
    dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.FORWARD})
public class LocalizationFilter extends HttpFilter {
    protected static final ResourceBundle.Control LANGUAGE_SELECTOR = new ResourceBundle.Control() {
        @Override
        public List<Locale> getCandidateLocales(String baseName, Locale locale) {
            return Arrays.asList(locale, Locale.ROOT);
        }
    };

    protected static final String LOCALE_SESSION_ATTRIBUTE = "langCode";

    public static final Language[] LANGUAGES = {
        new Language("en", "English"),
        new Language("ru", "Русский"),
        new Language("ja", "日本人")
    };

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext context = filterConfig.getServletContext();
        context.setAttribute("languages", LANGUAGES);
    }

    @Override
    public void filter(
        HttpServletRequest req,
        HttpServletResponse resp,
        FilterChain chain) throws IOException, ServletException
    {
        configureForHtmlUtf8(req, resp);

        String langParam = req.getParameter("lang");
        if (langParam != null) {
            req.getSession().setAttribute(LOCALE_SESSION_ATTRIBUTE, langParam);
        }
        String langCode = (String)req.getSession().getAttribute(LOCALE_SESSION_ATTRIBUTE);
        if (langCode == null) {
            langCode = req.getLocale().getLanguage();
            req.getSession().setAttribute(LOCALE_SESSION_ATTRIBUTE, langCode);
        }
        ResourceBundle localization = ResourceBundle.getBundle(
                "i18n.text", Locale.forLanguageTag(langCode), LANGUAGE_SELECTOR);
        req.setAttribute("langCode", langCode);
        req.setAttribute("l10n", localization);
        chain.doFilter(req, resp);
    }

    private void configureForHtmlUtf8(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
    }
}
