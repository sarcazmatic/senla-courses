package com.senla.courses.dao;

import java.io.Serializable;

public interface GenericDAO<T, PK extends Serializable> {

    PK save(T entity);

    T find(PK id);

}
