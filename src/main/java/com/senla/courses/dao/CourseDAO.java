package com.senla.courses.dao;

import com.senla.courses.exception.NotFoundException;
import com.senla.courses.model.Course;
import com.senla.courses.config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CourseDAO implements GenericDAO<Course, Long> {

    @Override
    public Long save(Course entity) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Long pk = (Long) session.save(entity);
            transaction.commit();
            return pk;
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Не смогли создать курс");
        }
    }

    @Override
    public Course update(Course entity) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.update(entity);
            transaction.commit();
            return entity;
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Не смогли обновить курс");
        } finally {
            session.clear();
        }
    }

    @Override
    public Optional<Course> find(Long id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Optional<Course> course = Optional.ofNullable(session.get(Course.class, id));
            transaction.commit();
            return course;
        } catch (Exception e) {
            transaction.rollback();
            throw new NotFoundException("Не удалось найти курс по id " + id);
        }
    }

    public List<Course> findAll(int from, int size) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query<Course> query = session.createQuery("SELECT c from Course c", Course.class);
            query.setFirstResult(from - 1);
            query.setMaxResults(size);
            List<Course> courses = query.list();
            transaction.commit();
            return courses;
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Не нашли пользователей");
        }
    }

    public List<Course> findAllByText(String text, int from, int size) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query<Course> query = session.createQuery("SELECT c from Course c " +
                    "WHERE ((:text IS NULL) " +
                    "OR (:text IS NOT NULL " +
                    "AND UPPER(c.name) LIKE CONCAT ('%', UPPER(:text), '%')) " +
                    "OR (:text IS NOT NULL " +
                    "AND UPPER(c.description) LIKE CONCAT ('%', UPPER(:text), '%'))) ", Course.class);
            query.setParameter("text", text);
            query.setFirstResult(from - 1);
            query.setMaxResults(size);
            List<Course> courses = query.list();
            transaction.commit();
            return courses;
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
            Course course = Optional.of(session.get(Course.class, id))
                    .orElseThrow(() -> new RuntimeException("Не нашли курс на удаление"));
            session.delete(course);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Не удалось удалить пользователя");
        } finally {
            session.clear();
        }
    }
}
