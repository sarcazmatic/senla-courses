package com.senla.courses.dao;

import com.senla.courses.model.Teacher;
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
public class TeacherDAO implements GenericDAO<Teacher, Long> {

    Logger logger = Logger.getLogger("TeacherDAO");

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
    public Optional<Teacher> update(Teacher entity) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            User userIn = entity.getUser();
            Query<Teacher> query = session.createQuery("SELECT t from Teacher t JOIN FETCH t.user " +
                    "WHERE :name IS NOT NULL AND UPPER(t.user.name) = UPPER(:name) ", Teacher.class);
            query.setParameter("name", userIn.getName());
            Teacher teacher = Optional.ofNullable(query.getSingleResult()).orElseThrow(() -> new RuntimeException("Не смогли найти учителя по запросу"));
            User userOut = teacher.getUser();
            if (userIn.getDateTimeRegistered() != null) {
                userOut.setDateTimeRegistered(userIn.getDateTimeRegistered());
            }
            if (userIn.getAge() != null) {
                userOut.setAge(userIn.getAge());
            }
            if (userIn.getDescription() != null) {
                userOut.setDescription(userIn.getDescription());
            }
            if (userIn.getEmail() != null) {
                userOut.setEmail(userIn.getEmail());
            }
            if (userIn.getName() != null) {
                userOut.setName(userIn.getName());
            }
            if (userIn.getPassword() != null) {
                userOut.setPassword(userIn.getPassword());
            }
            session.update(teacher);
            transaction.commit();
            return Optional.of(teacher);
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Не смогли обновить пользователя");
        }
    }

    @Override
    public Optional<Teacher> find(Long id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Optional<Teacher> teacher = Optional.ofNullable(session.get(Teacher.class, id));
            transaction.commit();
            return teacher;
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Не нашли пользователя по id");
        }
    }

    @Override
    public List<Teacher> findAll(String text, int from, int size) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query<Teacher> query = session.createQuery("SELECT t from Teacher t JOIN FETCH t.user " +
                    "WHERE (:name IS NULL) " +
                    "OR (:name IS NOT NULL " +
                    "AND UPPER(t.user.name) LIKE CONCAT ('%', UPPER(:name), '%'))", Teacher.class);
            query.setParameter("name", text);
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
                    .orElseThrow(() -> new RuntimeException("не нашли преподавателя"));
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
