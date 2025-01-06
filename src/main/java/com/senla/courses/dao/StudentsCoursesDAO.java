package com.senla.courses.dao;

import com.senla.courses.model.StudentsCourses;
import com.senla.courses.util.HibernateUtil;
import com.senla.courses.util.enums.StudentsCoursesRequestEnum;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class StudentsCoursesDAO implements GenericDAO<StudentsCourses, Long> {

    @Override
    public Long save(StudentsCourses entity) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.save(entity);
            transaction.commit();
            return 1L;
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public StudentsCourses update(StudentsCourses entity) {
        return null;
    }

    @Override
    public Optional<StudentsCourses> find(Long id) {
        return Optional.empty();
    }

    public StudentsCourses findByIds(Long studentId, Long courseId) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query<StudentsCourses> query = session.createQuery("SELECT sc from StudentsCourses sc " +
                    "JOIN FETCH Student AS s JOIN FETCH Course AS c " +
                    "WHERE (s.id = :studentId AND c.id = :courseId)", StudentsCourses.class);
            query.setParameter("studentId", studentId);
            query.setParameter("courseId", courseId);
            StudentsCourses studentsCourses = query.getSingleResult();
            transaction.commit();
            return studentsCourses;
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Long id) {

    }

    public List<StudentsCourses> findAllByCourseId(Long courseId) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query<StudentsCourses> query = session.createQuery("SELECT sc from StudentsCourses sc " +
                    "JOIN FETCH Student AS s JOIN FETCH Course AS c " +
                    "WHERE c.id = :courseId", StudentsCourses.class);
            query.setParameter("courseId", courseId);
            List<StudentsCourses> studentsCourses = query.getResultList();
            transaction.commit();
            return studentsCourses;
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException(e.getMessage());
        }
    }

    public Integer updateRequest(Long courseId, List<Long> ids, StudentsCoursesRequestEnum response) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            String query = "UPDATE StudentsCourses sc SET " +
                    "sc.courseStatus = :response " +
                    "WHERE (sc.course.id = :courseId) AND (sc.student.id IN :ids)";
            var sqlQuery = session.createQuery(query);
            sqlQuery.setParameter("courseId", courseId);
            sqlQuery.setParameter("ids", ids);
            sqlQuery.setParameter("response", response);
            int rows = sqlQuery.executeUpdate();
            transaction.commit();
            return rows;
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException(e.getMessage());
        } finally {
            session.clear();
            session.close();
        }
    }

}
