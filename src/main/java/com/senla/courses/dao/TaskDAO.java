package com.senla.courses.dao;

import com.senla.courses.exception.NotFoundException;
import com.senla.courses.model.Task;
import com.senla.courses.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TaskDAO implements GenericDAO<Task, Long> {

    @Override
    public Long save(Task entity) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Long pk = (Long) session.save(entity);
            transaction.commit();
            return pk;
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Не смогли создать задачу");
        } finally {
            session.clear();
        }
    }

    @Override
    public Task update(Task entity) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.update(entity);
            transaction.commit();
            return entity;
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Не смогли обновить задачу");
        } finally {
            session.clear();
        }
    }

    @Override
    public Optional<Task> find(Long id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Optional<Task> task = Optional.ofNullable(session.get(Task.class, id));
            transaction.commit();
            return task;
        } catch (Exception e) {
            transaction.rollback();
            throw new NotFoundException("Не удалось найти задачу по id " + id);
        }
    }

    @Override
    public List<Task> findAllByText(String text, int from, int size) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query<Task> query = session.createQuery("SELECT t from Task t " +
                    "WHERE ((:text IS NULL) " +
                    "OR (:text IS NOT NULL " +
                    "AND UPPER(t.name) LIKE CONCAT ('%', UPPER(:text), '%')) " +
                    "OR (:text IS NOT NULL " +
                    "AND UPPER(t.body) LIKE CONCAT ('%', UPPER(:text), '%'))) ", Task.class);
            query.setParameter("text", text);
            query.setFirstResult(from - 1);
            query.setMaxResults(size);
            List<Task> tasks = query.list();
            transaction.commit();
            return tasks;
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Не нашли задач по тексту");
        }
    }

    public List<Task> findAllByModuleId(Long moduleId, int from, int size) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query<Task> query = session.createQuery("SELECT t from Task t JOIN FETCH t.module " +
                    "WHERE (:moduleId IS NOT NULL " +
                    "AND (t.module.id = :moduleId))", Task.class);
            query.setParameter("moduleId", moduleId);
            query.setFirstResult(from - 1);
            query.setMaxResults(size);
            List<Task> tasks = query.list();
            transaction.commit();
            return tasks;
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Не удалось найти задач по id модуля " + moduleId);
        }
    }

    @Override
    public void deleteById(Long id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Task task = Optional.of(session.get(Task.class, id)).orElseThrow(()
                    -> new NotFoundException("Не удалось найти задачу"));
            session.delete(task);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Не удалось удалить задачу");
        } finally {
            session.clear();
        }
    }
}
