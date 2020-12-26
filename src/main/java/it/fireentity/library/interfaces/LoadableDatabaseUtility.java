package it.fireentity.library.interfaces;

import java.util.List;
import java.util.Optional;

public interface LoadableDatabaseUtility<T extends Cacheable<K>, K> {
    List<T> load();
    void insert(T t);
    void remove(T t);
    Optional<T> select(K key);
}
