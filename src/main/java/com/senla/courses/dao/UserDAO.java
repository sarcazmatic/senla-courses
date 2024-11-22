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
        try (Session session = HibernateUtil.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(entity);
            transaction.commit();
            return entity.getId();
        } catch (Exception ignored) {
        }
        return 0L;
    }
}
