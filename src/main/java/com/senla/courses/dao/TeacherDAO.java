package com.senla.courses.dao;

import com.senla.courses.model.Teacher;
import com.senla.courses.config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TeacherDAO implements GenericDAO<Teacher, Long> {


    @Override
    public Long save(Teacher entity) {
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
    public Teacher update(Teacher entity) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.update(entity);
            transaction.commit();
            return entity;
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Не смогли обновить пользователя");
        }
    }

    @Override
    public Optional<Teacher> find(Long id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();;
        try {
            Optional<Teacher> teacher = Optional.ofNullable(session.get(Teacher.class, id));
            transaction.commit();
            return teacher;
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Не нашли пользователя по id");
        }
    }

    public List<Teacher> findAllByText(String name, int from, int size) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query<Teacher> query = session.createQuery("SELECT t from Teacher t JOIN FETCH t.user " +
                    "WHERE (:name IS NULL) " +
                    "OR (:name IS NOT NULL " +
                    "AND UPPER(t.user.name) LIKE CONCAT ('%', UPPER(:name), '%'))", Teacher.class);
            query.setParameter("name", name);
            query.setFirstResult(from - 1);
            query.setMaxResults(size);
            List<Teacher> teachers = query.list();
            transaction.commit();
            return teachers;
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
            Teacher teacher = Optional.of(session.get(Teacher.class, id))
                    .orElseThrow(() -> new RuntimeException("Не нашли преподавателя"));
            var query1 = session.createQuery("DELETE FROM Teacher t WHERE t.id = :teacherId");
            query1.setParameter("teacherId", teacher.getId());
            query1.executeUpdate();
            var query2 = session.createQuery("DELETE FROM User u WHERE u.id = :userId");
            query2.setParameter("userId", teacher.getUser().getId());
            query2.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Не удалось удалить пользователя");
        } finally {
            session.clear();
        }
    }

}
