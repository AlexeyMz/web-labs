package ru.alexeymz.web.config;

import ru.alexeymz.web.data.CardRepository;
import ru.alexeymz.web.data.UserRepository;
import ru.alexeymz.web.impl.data.InMemoryUserRepository;
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
        context.setAttribute(UserRepository.ATTRIBUTE, new InMemoryUserRepository());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {}
}
