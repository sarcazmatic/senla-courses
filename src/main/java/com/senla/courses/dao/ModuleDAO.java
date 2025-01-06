package com.senla.courses.dao;

import com.senla.courses.exception.NotFoundException;
import com.senla.courses.model.Module;
import com.senla.courses.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ModuleDAO implements GenericDAO<Module, Long> {

    @Override
    public Long save(Module entity) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Long pk = (Long) session.save(entity);
            transaction.commit();
            return pk;
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Не смогли создать модуль");
        }
    }

    @Override
    public Module update(Module entity) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.update(entity);
            transaction.commit();
            return entity;
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Не смогли обновить модуль");
        } finally {
            session.clear();
        }
    }

    @Override
    public Optional<Module> find(Long id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Module module = session.get(Module.class, id);
            if (module == null) {
                throw new NotFoundException("Модуль с id " + id + "не найден");
            }
            transaction.commit();
            return Optional.of(module);
        } catch (Exception e) {
            transaction.rollback();
            throw new NotFoundException("Не удалось найти модуль по id " + id);
        }
    }

    public List<Module> findAllByText(String text, int from, int size) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query<Module> query = session.createQuery("SELECT m from Module m " +
                    "WHERE ((:text IS NULL) " +
                    "OR (:text IS NOT NULL " +
                    "AND UPPER(m.description) LIKE CONCAT ('%', UPPER(:text), '%'))) ", Module.class);
            query.setParameter("text", text);
            query.setFirstResult(from - 1);
            query.setMaxResults(size);
            List<Module> modules = query.list();
            transaction.commit();
            return modules;
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Не нашли модулей");
        }
    }

    public List<Module> findAllByName(String text, int from, int size) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query<Module> query = session.createQuery("SELECT m from Module m " +
                    "WHERE ((:text IS NULL) " +
                    "OR (:text IS NOT NULL " +
                    "AND UPPER(m.name) LIKE CONCAT ('%', UPPER(:text), '%'))) ", Module.class);
            query.setParameter("text", text);
            query.setFirstResult(from - 1);
            query.setMaxResults(size);
            List<Module> modules = query.list();
            transaction.commit();
            return modules;
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Не нашли модулей");
        }
    }

    @Override
    public void deleteById(Long id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Module module = session.get(Module.class, id);
            if (module != null) {
                session.delete(module);
            } else {
                throw new NotFoundException("Не удалось найти модуль по id " + id);
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Не удалось удалить модуль");
        } finally {
            session.clear();
        }
    }
}
