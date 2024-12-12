package com.senla.courses.dao;

import com.senla.courses.model.Course;
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
public class CourseDAO implements GenericDAO<Course, Long> {

    Logger logger = Logger.getLogger("CourseDAO");

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
    public Optional<Course> update(Course entity) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query<Course> query = session.createQuery("SELECT u from Course u " +
                    "WHERE :name IS NOT NULL AND UPPER(u.name) = UPPER(:name) ", Course.class);
            query.setParameter("name", entity.getName());
            Optional<Course> course = Optional.ofNullable(query.getSingleResult());
            /*if (entity.getDateTimeRegistered() != null) {
                course.setDateTimeRegistered(entity.getDateTimeRegistered());
            }
            if (entity.getAge() != null) {
                course.setAge(entity.getAge());
            }
            if (entity.getDescription() != null) {
                course.setDescription(entity.getDescription());
            }
            if (entity.getEmail() != null) {
                course.setEmail(entity.getEmail());
            }
            if (entity.getName() != null) {
                course.setName(entity.getName());
            }
            if (entity.getPassword() != null) {
                course.setPassword(entity.getPassword());
            }*/
            session.update(course.get());
            transaction.commit();
            return course;
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Не смогли обновить пользователя");
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
            throw new RuntimeException("Не нашли пользователя по id");
        }
    }

    @Override
    public List<Course> findAll(String text, int from, int size) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query<Course> query = session.createQuery("SELECT u from Course u " +
                    "WHERE (:name IS NULL) " +
                    "OR (:name IS NOT NULL " +
                    "AND UPPER(u.name) LIKE CONCAT ('%', UPPER(:name), '%'))", Course.class);
            query.setParameter("name", text);
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
            Course course = session.get(Course.class, id);
            if (course != null) {
                session.delete(course);
            } else {
                logger.log(Level.WARNING, "Не нашли пользователя на удаление");
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Не удалось удалить пользователя");
        } finally {
            session.clear();
        }
    }
}
