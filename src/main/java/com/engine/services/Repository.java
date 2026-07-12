package com.engine.services;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {
    void save(T o);
    void delete(T o);
    List<T> findAll();
    Optional<T> findById(String id);
    void deleteById(String id);
    long count();
}
