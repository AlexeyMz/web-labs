package ru.alexeymz.web.impl.data;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.alexeymz.web.data.OrderRepository;
import ru.alexeymz.web.model.Order;

import java.util.List;

public class HibernateOrderRepository implements OrderRepository {
    private SessionFactory sessionFactory;

    public HibernateOrderRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Order> findByUsername(String username) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            List<Order> orders = session.createQuery(
                    "SELECT o FROM Order o WHERE o.username = :name")
                    .setString("name", username)
                    .list();
            transaction.commit();
            return orders;
        } catch (HibernateException ex) {
            if (transaction != null) { transaction.rollback(); }
            throw ex;
        } finally {
            if (session != null && session.isOpen()) { session.close(); }
        }
    }

    @Override
    public void save(Order order) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(order);
            transaction.commit();
        } catch (HibernateException ex) {
            if (transaction != null) { transaction.rollback(); }
            throw ex;
        } finally {
            if (session != null && session.isOpen()) { session.close(); }
        }
    }
}
