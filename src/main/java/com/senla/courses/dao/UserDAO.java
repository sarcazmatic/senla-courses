package com.senla.courses.dao;

import com.senla.courses.exception.NotFoundException;
import com.senla.courses.model.User;
import com.senla.courses.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class UserDAO implements GenericDAO<User, Long> {

    Logger logger = Logger.getLogger("UserDAO");

    @Override
    public Long save(User entity) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Long pk = (Long) session.save(entity);
            transaction.commit();
            return pk;
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Не смогли создать пользователя");
        }
    }

    @Override
    public Optional<User> update(User entity) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query<User> query = session.createQuery("SELECT u from User u " +
                    "WHERE :name IS NOT NULL AND UPPER(u.name) = UPPER(:name) ", User.class);
            query.setParameter("name", entity.getName());
            User user = Optional.ofNullable(query.getSingleResult())
                    .orElseThrow(() -> new RuntimeException("Не нашли пользователя"));
            if (entity.getDateTimeRegistered() != null) {
                user.setDateTimeRegistered(entity.getDateTimeRegistered());
            }
            if (entity.getAge() != null) {
                user.setAge(entity.getAge());
            }
            if (entity.getDescription() != null) {
                user.setDescription(entity.getDescription());
            }
            if (entity.getEmail() != null) {
                user.setEmail(entity.getEmail());
            }
            if (entity.getName() != null) {
                user.setName(entity.getName());
            }
            if (entity.getPassword() != null) {
                user.setPassword(entity.getPassword());
            }
            session.update(user);
            transaction.commit();
            return Optional.of(user);
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Не смогли обновить пользователя");
        }
    }

    @Override
    public Optional<User> find(Long id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Optional<User> user = Optional.ofNullable(session.get(User.class, id));
            transaction.commit();
            return user;
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Не нашли пользователя по id");
        }
    }

    @Override
    public List<User> findAll(String text, int from, int size) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query<User> query = session.createQuery("SELECT u from User u " +
                    "WHERE (:name IS NULL) " +
                    "OR (:name IS NOT NULL " +
                    "AND UPPER(u.name) LIKE CONCAT ('%', UPPER(:name), '%'))", User.class);
            query.setParameter("name", text);
            query.setFirstResult(from - 1);
            query.setMaxResults(size);
            List<User> users = query.list();
            transaction.commit();
            return users;
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Не нашли пользователей");
        }
    }

    @Override
    public void deleteById(Long id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            User user = Optional.of(session.get(User.class, id))
                    .orElseThrow(() -> new RuntimeException("Не нашли пользователя"));
            session.delete(user);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Не удалось удалить пользователя");
        } finally {
            session.clear();
        }
    }
}
