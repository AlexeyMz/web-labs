package ru.alexeymz.web.controllers;

import ru.alexeymz.web.core.BaseHttpServlet;

import java.util.IllegalFormatException;

public abstract class BaseAppController extends BaseHttpServlet {
    protected Long matchId(String param) {
        if (param == null) { return null; }
        try {
            return Long.valueOf(param);
        } catch (IllegalFormatException ex) {
            return null;
        }
    }
}
