package com.senla.courses.dao;

import com.senla.courses.exception.NotFoundException;
import com.senla.courses.model.Literature;
import com.senla.courses.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class LiteratureDAO implements GenericDAO<Literature, Long> {

    @Override
    public Long save(Literature entity) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Long pk = (Long) session.save(entity);
            transaction.commit();
            return pk;
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Не смогли создать литературу");
        } finally {
            session.clear();
        }
    }

    @Override
    public Literature update(Literature entity) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.update(entity);
            transaction.commit();
            return entity;
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Не смогли обновить литературу");
        } finally {
            session.clear();
        }
    }

    @Override
    public Optional<Literature> find(Long id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Optional<Literature> literature = Optional.ofNullable(session.get(Literature.class, id));
            transaction.commit();
            return literature;
        } catch (Exception e) {
            transaction.rollback();
            throw new NotFoundException("Не удалось найти литературу по id " + id);
        }
    }

    public List<Literature> findAllByText(String text, int from, int size) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query<Literature> query = session.createQuery("SELECT l from Literature l " +
                    "WHERE ((:text IS NULL) " +
                    "OR (:text IS NOT NULL " +
                    "AND UPPER(l.name) LIKE CONCAT ('%', UPPER(:text), '%')))", Literature.class);
            query.setParameter("text", text);
            query.setFirstResult(from - 1);
            query.setMaxResults(size);
            List<Literature> literatureList = query.list();
            transaction.commit();
            return literatureList;
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Не нашли литературу по тексту");
        }
    }

    public List<Literature> findAllByAuthor(String author, int from, int size) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query<Literature> query = session.createQuery("SELECT l from Literature l " +
                    "WHERE (:author IS NULL) " +
                    "OR (:author IS NOT NULL " +
                    "AND UPPER(l.author) LIKE CONCAT ('%', UPPER(:author), '%'))", Literature.class);
            query.setParameter("author", author);
            query.setFirstResult(from - 1);
            query.setMaxResults(size);
            List<Literature> literatureList = query.list();
            transaction.commit();
            return literatureList;
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Не нашли литературу по тексту");
        }
    }

    public List<Literature> findByModuleId(Long moduleId, int from, int size) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query<Literature> query = session.createQuery("SELECT l from Literature l JOIN FETCH l.module " +
                    "WHERE (:moduleId IS NOT NULL " +
                    "AND (l.module.id = :moduleId))", Literature.class);
            query.setParameter("moduleId", moduleId);
            query.setFirstResult(from - 1);
            query.setMaxResults(size);
            List<Literature> literatureList = query.list();
            transaction.commit();
            return literatureList;
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Не нашли литературу по тексту");
        }
    }

    @Override
    public void deleteById(Long id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Literature literature = Optional.of(session.get(Literature.class, id)).orElseThrow(()
                    -> new NotFoundException("Не удалось найти литературу"));
            session.delete(literature);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Не удалось удалить литературу");
        } finally {
            session.clear();
        }
    }
}
