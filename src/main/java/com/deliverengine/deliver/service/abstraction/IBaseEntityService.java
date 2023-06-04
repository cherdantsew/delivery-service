package com.deliverengine.deliver.service.abstraction;

import java.util.List;

public interface IBaseEntityService<T> {
    T findOneById(Long id);

    List<T> findAll();

    T saveAndFlush(T entity);

    void save(T entity);
}
