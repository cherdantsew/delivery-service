package com.orderengine.deliver.deliverservice.service.abstraction;

import java.util.List;

public interface IBaseEntityService<T> {
    T findOneById(Long id);

    List<T> findAll();

    T saveAndFlush(T entity);

    void save(T entity);
}
