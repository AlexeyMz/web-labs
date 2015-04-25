package ru.alexeymz.web.core;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class HttpFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}

    protected abstract void filter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
            throws IOException, ServletException;

    @Override
    public final void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest) ||
                !(response instanceof HttpServletResponse)) {
            chain.doFilter(request, response);
            return;
        }
        filter((HttpServletRequest)request, (HttpServletResponse)response, chain);
    }
}
