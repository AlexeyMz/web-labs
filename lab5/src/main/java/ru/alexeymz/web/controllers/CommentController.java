package ru.alexeymz.web.controllers;

import ru.alexeymz.web.core.utils.EscapeUtils;
import ru.alexeymz.web.data.CommentRepository;
import ru.alexeymz.web.data.UserRepository;
import ru.alexeymz.web.model.Comment;
import ru.alexeymz.web.model.User;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;

@WebServlet("/comments")
public class CommentController extends BaseAppController {
    private UserRepository userRepository;
    private CommentRepository commentRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = config.getServletContext();
        this.userRepository = (UserRepository)context.getAttribute(UserRepository.ATTRIBUTE);
        this.commentRepository = (CommentRepository)context.getAttribute(CommentRepository.ATTRIBUTE);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getCurrentUserOrError(userRepository, req, resp);
        if (user == null) { return; }

        Optional<Long> startId = Optional.ofNullable(matchId(req.getParameter("fromID")));
        Locale locale = Locale.forLanguageTag((String)req.getAttribute("langCode"));
        int offset = Integer.valueOf(req.getParameter("date"));

        List<Object> resultJson = new ArrayList<>();
        resultJson.add(EscapeUtils.calendarToLocal(Calendar.getInstance(), locale, offset));

        for (Comment comment : commentRepository.findCommentFrom(startId)) {
            User author = userRepository.findByUsername(comment.getUsername());
            Map<String, Object> info = new HashMap<>();
            info.put("id", comment.getId());
            info.put("author", author == null ? comment.getUsername() : author.getFirstName());
            info.put("date", EscapeUtils.calendarToLocal(comment.getCommentDate(), locale, offset));
            info.put("text", EscapeUtils.escapeHTML(comment.getText()));
            resultJson.add(info);
        }

        try (OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream())) {
            writer.write(EscapeUtils.encodeURIComponent(EscapeUtils.toJsonObject(resultJson)));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getCurrentUserOrError(userRepository, req, resp);
        if (user == null) { return; }

        String text = req.getParameter("text");
        if (text != null) { text = EscapeUtils.decodeURIComponent(text).trim(); }
        if (text == null || text.isEmpty()) {
            resp.sendError(400);
            return;
        }

        Comment comment = new Comment(Calendar.getInstance(), user.getUsername(), text);
        commentRepository.save(comment);
        log(String.format("User <%s> leaved a comment %s", user.getUsername(), comment));

        resp.setStatus(200);
    }
}
