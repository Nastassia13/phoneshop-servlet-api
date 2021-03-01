package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.Dao;
import com.es.phoneshop.exception.ArgumentIsNullException;
import com.es.phoneshop.exception.NotFoundException;
import com.es.phoneshop.model.item.Item;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractGenericDao<T extends Item> implements Dao<T> {
    protected Long maxId = 0L;
    protected List<T> items = new ArrayList<>();
    protected final Object lock = new Object();

    public T getItem(Long id) {
        if (id == null) {
            throw new ArgumentIsNullException();
        }
        synchronized (lock) {
            String className = ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                    .getActualTypeArguments()[0]).getSimpleName();
            return items.stream()
                    .filter(o -> id.equals(o.getId()))
                    .findAny()
                    .orElseThrow(() -> new NotFoundException(id, className));
        }
    }

    public void save(T item) {
        if (item == null) {
            throw new ArgumentIsNullException();
        }
        synchronized (lock) {
            Long id = item.getId();
            if (id != null) {
                Optional<T> current = items.stream()
                        .filter(p -> id.equals(p.getId()))
                        .findAny();
                if (current.isPresent()) {
                    items.set(items.indexOf(current.get()), item);
                } else {
                    items.add(item);
                }
            } else {
                item.setId(++maxId);
                items.add(item);
            }
        }
    }
}
