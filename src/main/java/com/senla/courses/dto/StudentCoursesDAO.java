package com.senla.courses.dto;

import com.senla.courses.dao.GenericDAO;
import com.senla.courses.model.StudentsCourses;
import com.senla.courses.model.StudentsCoursesPK;
import com.senla.courses.model.User;
import com.senla.courses.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class StudentCoursesDAO implements GenericDAO<StudentsCourses, Long> {

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
    public List<StudentsCourses> findAllByText(String text, int from, int size) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }
}
