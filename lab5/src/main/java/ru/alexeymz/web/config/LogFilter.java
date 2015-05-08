package ru.alexeymz.web.config;

import ru.alexeymz.web.core.HttpFilter;
import ru.alexeymz.web.data.UserRepository;
import ru.alexeymz.web.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(
    urlPatterns = {"/*"},
    dispatcherTypes = {DispatcherType.REQUEST})
public class LogFilter extends HttpFilter {
    private UserRepository userRepository;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext context = filterConfig.getServletContext();
        this.userRepository = (UserRepository)context.getAttribute(
                UserRepository.ATTRIBUTE);
    }

    @Override
    protected void filter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        String username = req.getRemoteUser();
        User user = null;
        if (username != null) {
            user = userRepository.findByUsername(username);
        }
        //String userLabel = user == null ? "Anonymous user" : String.format("User <%s>", user.getUsername());
        //req.getServletContext().log(userLabel + ": " + req.getRequestURI());
        chain.doFilter(req, resp);
    }
}
