package com.senla.courses.dao;

import com.senla.courses.model.File;
import com.senla.courses.config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FileDAO implements GenericDAO<File, Long> {
    @Override
    public Long save(File entity) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Long pk = (Long) session.save(entity);
            transaction.commit();
            return pk;
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public File update(File entity) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.update(entity);
            transaction.commit();
            return entity;
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException(e.getMessage());
        } finally {
            session.clear();
        }
    }

    @Override
    public Optional<File> find(Long id) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Optional<File> file = Optional.ofNullable(session.get(File.class, id));
            transaction.commit();
            return file;
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Не смогли найти файл");
        }
    }

    public List<File> findAllByModuleId(Long moduleId) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query<File> query = session.createQuery("SELECT f from File f " +
                    "WHERE (:moduleId IS NOT NULL) " +
                    "AND (f.module.id = :moduleId)", File.class);
            query.setParameter("moduleId", moduleId);
            List<File> filesList = query.list();
            transaction.commit();
            return filesList;
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
            File file = Optional.of(session.get(File.class, id))
                    .orElseThrow(() -> new RuntimeException("Не нашли файл на удаление"));
            session.delete(file);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException("Не удалось удалить файл");
        } finally {
            session.clear();
        }
    }
}
