package com.senla.courses.dao;

import java.io.Serializable;
import java.util.Optional;

public interface GenericDAO<T, PK extends Serializable> {

    PK save(T entity);

    T update(T entity);

    Optional<T> find(PK id);

    void deleteById(PK id);

}
