package com.es.phoneshop.dao;

public interface Dao<T> {
    T getItem(Long id);

    void save(T item);
}
