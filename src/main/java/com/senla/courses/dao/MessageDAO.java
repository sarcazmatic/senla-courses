package com.senla.courses.dao;

import com.senla.courses.exception.NotFoundException;
import com.senla.courses.model.Message;
import com.senla.courses.model.User;
import com.senla.courses.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

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
        }
    }

    @Override
    public Message update(Message entity) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Long pk = (long) session.save(entity);
            Message message = session.get(Message.class, pk);
            transaction.commit();
            return message;
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Не смогли обновить сообщение по id");
        }
    }

    @Override
    public Message find(Long id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Message message = session.get(Message.class, id);
            transaction.commit();
            return message;
        } catch (Exception e) {
            transaction.rollback();
            throw new NotFoundException("Не удалось найти сообщение по id " + id);
        }
    }

    @Override
    public List<Message> findAll(String text, int from, int size) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query<Message> query = session.createQuery("SELECT m from Message m " +
                    "WHERE UPPER(m.body) LIKE CONCAT ('%', UPPER(:text), '%')", Message.class);
            query.setParameter("text", text);
            query.setFirstResult(from - 1);
            query.setMaxResults(size);
            List<Message> messages = query.list();
            transaction.commit();
            return messages;
        } catch (Exception e) {
            transaction.rollback();
            throw new NotFoundException("Не удалось найти пользователей");
        }
    }

    @Override
    public void deleteById(Long id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Message message = session.get(Message.class, id);
            if (message != null) {
                session.delete(message);
            } else {
                throw new NotFoundException("Не удалось найти сообщение по id " + id);
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Не удалось удалить сообщение");
        } finally {
            session.clear();
        }
    }

    public List<Message> findMessagesBetween(Long userOne, Long userTwo, int from, int size) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query<Message> query = session.createQuery("SELECT m FROM Message m " +
                    "WHERE m.student.id = :userOne AND m.teacher.id = :userTwo " +
                    "OR m.teacher.id = :userOne AND m.student.id = :userTwo " +
                    "ORDER BY m.dateTimeSent DESC", Message.class);
            query.setParameter("userOne", userOne);
            query.setParameter("userTwo", userTwo);
            query.setFirstResult(from - 1);
            query.setMaxResults(size);
            List<Message> messages = query.list();
            transaction.commit();
            return messages;
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Не нашли сообщений между пользователями");
        }
    }

}
