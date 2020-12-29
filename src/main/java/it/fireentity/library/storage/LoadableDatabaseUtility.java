package it.fireentity.library.storage;

import it.fireentity.library.interfaces.Cacheable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class LoadableDatabaseUtility<T extends Cacheable<K>, K> {
    public List<T> load() {
        return new ArrayList<>();
    }
    public Optional<T> select(K key) {
        return Optional.empty();
    }

    public void insert(T t) {

    }
    public void remove(T t) {

    }
}
