package ru.alexeymz.web.controllers;

import javax.servlet.http.HttpServlet;
import java.util.IllegalFormatException;

public abstract class BaseAppController extends HttpServlet {
    protected Long matchId(String param) {
        if (param == null) { return null; }
        try {
            return Long.valueOf(param);
        } catch (IllegalFormatException ex) {
            return null;
        }
    }
}
