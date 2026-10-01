package com.engine.services;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {

    T save(T o);

    void delete(T o);

    List<T> findAll();

    Optional<T> findById(long id);

    void deleteById(long id);

    long count();
}
