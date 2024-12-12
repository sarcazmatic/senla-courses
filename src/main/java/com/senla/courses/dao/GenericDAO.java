package com.senla.courses.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface GenericDAO<T, PK extends Serializable> {

    PK save(T entity);

    Optional<T> update(T entity);

    Optional<T> find(PK id);

    List<T> findAll(String text, int from, int size);

    void deleteById(PK id);

}
