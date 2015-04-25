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
    dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.FORWARD})
public class UserInfoFilter extends HttpFilter {
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
        if (username != null) {
            User user = userRepository.findByUsername(username);
            if (user != null) {
                req.setAttribute("user", user);
            }
        }
        chain.doFilter(req, resp);
    }
}
