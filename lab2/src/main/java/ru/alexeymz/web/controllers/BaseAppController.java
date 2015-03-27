package ru.alexeymz.web.controllers;

import ru.alexeymz.web.core.BaseHttpServlet;
import ru.alexeymz.web.data.RepositoryFactory;

import javax.servlet.http.HttpServletRequest;

public abstract class BaseAppController extends BaseHttpServlet {

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
