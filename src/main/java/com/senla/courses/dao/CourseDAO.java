package com.senla.courses.dao;

import com.senla.courses.exception.NotFoundException;
import com.senla.courses.model.Course;
import com.senla.courses.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

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
            Query<Course> query = session.createQuery("SELECT c from Course c " +
                    "WHERE :name IS NOT NULL AND UPPER(c.name) = UPPER(:name) ", Course.class);
            query.setParameter("name", entity.getName());
            Course course = query.getSingleResult();
            System.out.println(course.getName());
            if (entity.getComplexity() != null) {
                course.setComplexity(entity.getComplexity());
            }
            if (entity.getDuration() != null) {
                course.setDuration(entity.getDuration());
            }
            if (entity.getDescription() != null) {
                course.setDescription(entity.getDescription());
            }
            if (entity.getField() != null) {
                course.setField(entity.getField());
            }
            if (entity.getName() != null) {
                course.setName(entity.getName());
            }
            session.update(course);
            transaction.commit();
            return course;
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Не смогли обновить курс");
        } finally {
            session.clear();
        }
    }

    @Override
    public Course find(Long id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query<Course> query = session.createQuery("SELECT c from Course c " +
                    "LEFT JOIN Module m ON c.id = m.course.id " +
                    "WHERE c.id = :id", Course.class);
            query.setParameter("id", id);
            Course course = query.getSingleResult();
            if (course == null) {
                throw new NotFoundException("Курса с id " + id + "не найдено");
            }
            transaction.commit();
            return course;
        } catch (Exception e) {
            transaction.rollback();
            throw new NotFoundException("Не удалось найти курс по id " + id);
        }
    }

    @Override
    public List<Course> findAll(String text, int from, int size) {
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
            Course course = session.get(Course.class, id);
            if (course != null) {
                session.delete(course);
            } else {
                throw new NotFoundException("Не удалось найти курс по id " + id);
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
