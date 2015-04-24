package ru.alexeymz.web.config;

import ru.alexeymz.web.data.CardRepository;
import ru.alexeymz.web.impl.data.XmlCardRepository;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ApplicationContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        context.setAttribute(CardRepository.ATTRIBUTE, new XmlCardRepository("carddb.xml"));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {}
}
