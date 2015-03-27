package ru.alexeymz.web.controllers;

import ru.alexeymz.web.core.BaseHttpServlet;
import ru.alexeymz.web.data.RepositoryFactory;

import javax.servlet.http.HttpServletRequest;

public abstract class BaseAppController extends BaseHttpServlet {

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
}
