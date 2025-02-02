package com.senla.courses.dao;

import com.senla.courses.model.Student;
import com.senla.courses.config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class StudentDAO implements GenericDAO<Student, Long> {


    @Override
    public Long save(Student entity) {
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
    public Student update(Student entity) {
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
    public Optional<Student> find(Long id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();;
        try {
            Optional<Student> student = Optional.ofNullable(session.get(Student.class, id));
            transaction.commit();
            return student;
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Не нашли пользователя по id");
        }
    }

    public List<Student> findAllByText(String name, int from, int size) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query<Student> query = session.createQuery("SELECT s from Student s JOIN FETCH s.user " +
                    "WHERE (:name IS NULL) " +
                    "OR (:name IS NOT NULL " +
                    "AND UPPER(s.user.name) LIKE CONCAT ('%', UPPER(:name), '%'))", Student.class);
            query.setParameter("name", name);
            query.setFirstResult(from - 1);
            query.setMaxResults(size);
            List<Student> students = query.list();
            transaction.commit();
            return students;
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
            Student student = Optional.of(session.get(Student.class, id))
                    .orElseThrow(() -> new RuntimeException("Не смогли найти студента"));
            var query1 = session.createQuery("DELETE FROM Student s WHERE s.id = :studentId");
            query1.setParameter("studentId", student.getId());
            query1.executeUpdate();
            var query2 = session.createQuery("DELETE FROM User u WHERE u.id = :userId");
            query2.setParameter("userId", student.getUser().getId());
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
