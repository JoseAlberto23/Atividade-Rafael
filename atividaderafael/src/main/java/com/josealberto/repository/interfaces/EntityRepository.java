package com.josealberto.repository.interfaces;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EntityRepository<T> {
    void save(T entity);
    Optional<T> findById(UUID uuid);
    List<T> findAll();
    void deleteById(UUID uuid);
}