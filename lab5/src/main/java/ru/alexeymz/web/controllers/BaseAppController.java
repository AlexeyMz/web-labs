package ru.alexeymz.web.controllers;

import ru.alexeymz.web.data.UserRepository;
import ru.alexeymz.web.model.CartEntry;
import ru.alexeymz.web.model.User;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;

public abstract class BaseAppController extends HttpServlet {
    @Override
    public void log(String msg) {
        super.log(msg);
    }

    @Override
    public void log(String message, Throwable t) {
        super.log(message, t);
    }

    protected Long matchId(String param) {
        if (param == null) { return null; }
        try {
            return Long.valueOf(param);
        } catch (IllegalFormatException ex) {
            return null;
        }
    }

    protected User getCurrentUserOrError(
            UserRepository repository, HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String username = req.getRemoteUser();
        if (username == null) {
            resp.sendError(403);
            return null;
        }
        User user = repository.findByUsername(username);
        if (user == null) {
            resp.sendError(400);
            return null;
        }
        return user;
    }
}
