package com.senla.courses.dao;

import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;

public interface GenericDAO<T, PK extends Serializable> {

    PK save(T entity);

    T update(T entity);

    T find(PK id);

    List<T> findAll(String text, int from, int size);

    void deleteById(PK id);

}
