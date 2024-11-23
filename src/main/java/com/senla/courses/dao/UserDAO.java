package com.senla.courses.dao;

import com.senla.courses.model.User;
import com.senla.courses.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAO implements GenericDAO<User, Long> {

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
    public User find(Long id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            User user = session.load(User.class, id);
            transaction.commit();
            return user;
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Не нашли пользователя по id");
        }
    }
}
