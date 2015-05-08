package ru.alexeymz.web.config;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import ru.alexeymz.web.data.CardRepository;
import ru.alexeymz.web.data.DeliveryPointRepository;
import ru.alexeymz.web.data.OrderRepository;
import ru.alexeymz.web.data.UserRepository;
import ru.alexeymz.web.impl.data.HibernateOrderRepository;
import ru.alexeymz.web.impl.data.InMemoryUserRepository;
import ru.alexeymz.web.impl.data.XmlCardRepository;
import ru.alexeymz.web.impl.data.XmlDeliveryPointRepository;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ApplicationContextListener implements ServletContextListener {
    private SessionFactory sessionFactory;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        context.setAttribute(CardRepository.ATTRIBUTE, new XmlCardRepository("carddb.xml"));
        context.setAttribute(DeliveryPointRepository.ATTRIBUTE, new XmlDeliveryPointRepository("delivery_points.xml"));
        context.setAttribute(UserRepository.ATTRIBUTE, new InMemoryUserRepository());
        try {
            buildSessionFactory();
            context.setAttribute(OrderRepository.ATTRIBUTE, new HibernateOrderRepository(sessionFactory));
        } catch (Exception ex) {
            sce.getServletContext().log("SessionFactory creation error", ex);
        }
    }

    private SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.configure();
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
                configuration.getProperties()).build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        return sessionFactory;
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
