package com.senla.courses.dao;

import java.io.Serializable;

public interface GenericDAO<T, PK extends Serializable> {

    public PK save(T entity);

}
