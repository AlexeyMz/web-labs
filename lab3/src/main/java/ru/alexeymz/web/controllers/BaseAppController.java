package ru.alexeymz.web.controllers;

import ru.alexeymz.web.core.BaseHttpServlet;
import ru.alexeymz.web.data.RepositoryFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.IllegalFormatException;
import java.util.Locale;
import java.util.ResourceBundle;

public abstract class BaseAppController extends BaseHttpServlet {
    protected static final String LOCALE_SESSION_ATTRIBUTE = "langCode";

    public final static class Language {
        public final String code;
        public final String label;

        public Language(String code, String label) {
            this.code = code;
            this.label = label;
        }
    }

    protected static final Language[] LANGUAGES = {
        new Language("en", "English"),
        new Language("ru", "Русский"),
        new Language("ja", "日本人")
    };

    protected RepositoryFactory repositoryFactory;

    public BaseAppController() {
        repositoryFactory = RepositoryFactory.instance();
    }

    protected String getLanguageParam(HttpServletRequest req) {
        String langParam = req.getParameter("lang");
        if (langParam == null) {
            langParam = req.getLocale().getLanguage();
        }
        return langParam;
    }

    protected void configureLanguage(HttpServletRequest req) {
        String langParam = req.getParameter("lang");
        if (langParam != null) {
            req.getSession().setAttribute(LOCALE_SESSION_ATTRIBUTE, langParam);
        }
        String langCode = (String)req.getSession().getAttribute(
                LOCALE_SESSION_ATTRIBUTE);
        if (langCode == null) {
            langCode = req.getLocale().getLanguage();
            req.getSession().setAttribute(LOCALE_SESSION_ATTRIBUTE, langCode);
        }
        ResourceBundle localization = ResourceBundle.getBundle(
            "i18n.text", Locale.forLanguageTag(langCode), LANGUAGE_SELECTOR);
        req.setAttribute("langCode", langCode);
        req.setAttribute("l10n", localization);
        req.setAttribute("languages", LANGUAGES);
    }

    protected Long matchId(String param) {
        if (param == null) { return null; }
        try {
            return Long.valueOf(param);
        } catch (IllegalFormatException ex) {
            return null;
        }
    }
}
