package ru.alexeymz.web.impl.data;

import org.hibernate.*;
import ru.alexeymz.web.data.CommentRepository;
import ru.alexeymz.web.model.Comment;

import java.util.List;
import java.util.Optional;

public class HibernateCommentRepository implements CommentRepository {
    private SessionFactory sessionFactory;

    public HibernateCommentRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Comment> findCommentFrom(Optional<Long> commentId) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Query query = commentId.isPresent()
                    ? session.createQuery("SELECT o FROM Comment o WHERE o.id >= :from_id")
                        .setLong("from_id", commentId.get())
                    : session.createQuery("FROM Comment");
            List<Comment> comments = query.list();
            transaction.commit();
            return comments;
        } catch (HibernateException ex) {
            if (transaction != null) { transaction.rollback(); }
            throw ex;
        } finally {
            if (session != null && session.isOpen()) { session.close(); }
        }
    }

    @Override
    public void save(Comment comment) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(comment);
            transaction.commit();
        } catch (HibernateException ex) {
            if (transaction != null) { transaction.rollback(); }
            throw ex;
        } finally {
            if (session != null && session.isOpen()) { session.close(); }
        }
    }
}
