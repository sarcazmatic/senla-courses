package com.senla.courses.dao;

import com.senla.courses.exception.NotFoundException;
import com.senla.courses.model.Message;
import com.senla.courses.config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MessageDAO implements GenericDAO<Message, Long> {

    @Override
    public Long save(Message entity) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Long pk = (Long) session.save(entity);
            transaction.commit();
            return pk;
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Не смогли сохранить сообщение");
        } finally {
            session.clear();
        }
    }

    @Override
    public Message update(Message entity) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.update(entity);
            return entity;
        } catch (RuntimeException e) {
            transaction.rollback();
            throw new RuntimeException("Runtime исключение");
        } finally {
            session.close();
        }
    }

    @Override
    public Optional<Message> find(Long id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Optional<Message> message = Optional.ofNullable(session.get(Message.class, id));
            transaction.commit();
            return message;
        } catch (RuntimeException e) {
            transaction.rollback();
            throw new RuntimeException("Runtime исключение");
        }
    }

    public List<Message> findAllByText(Long fromOrTo, String text, int from, int size) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query<Message> query = session.createQuery("SELECT m from Message m " +
                    "WHERE UPPER(m.body) LIKE CONCAT ('%', UPPER(:text), '%') " +
                    "AND (m.from.id = :fromOrTo OR m.to.id = :fromOrTo)", Message.class);
            query.setParameter("fromOrTo", fromOrTo);
            query.setParameter("text", text);
            query.setFirstResult(from - 1);
            query.setMaxResults(size);
            List<Message> messages = query.list();
            transaction.commit();
            return messages;
        } catch (RuntimeException e) {
            transaction.rollback();
            throw new NotFoundException("Не удалось найти пользователей");
        }
    }

    @Override
    public void deleteById(Long id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Message message = Optional.of(session.get(Message.class, id))
                    .orElseThrow(() -> new RuntimeException("Не нашли сообщения на удаление"));
            session.delete(message);
            transaction.commit();
        } catch (RuntimeException e) {
            transaction.rollback();
            throw new RuntimeException("Runtime исключение");
        } finally {
            session.clear();
        }
    }

    public List<Message> findMessagesBetween(Long userOne, Long userTwo, int from, int size) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query<Message> query = session.createQuery("SELECT m FROM Message m " +
                    "WHERE m.from.id = :userOne AND m.to.id = :userTwo " +
                    "OR m.to.id = :userOne AND m.from.id = :userTwo " +
                    "ORDER BY m.dateTimeSent DESC", Message.class);
            query.setParameter("userOne", userOne);
            query.setParameter("userTwo", userTwo);
            query.setFirstResult(from - 1);
            query.setMaxResults(size);
            List<Message> messages = query.list();
            transaction.commit();
            return messages;
        } catch (RuntimeException e) {
            transaction.rollback();
            throw new RuntimeException("Runtime исключение");
        } finally {
            session.clear();
        }
    }

}
