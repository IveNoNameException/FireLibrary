package it.fireentity.library.storage;


import java.util.*;

public class GenericCache<K,V> {
    private final HashMap<K,V> cache = new HashMap<>();

    public Optional<V> getValue(K key) {
        return Optional.ofNullable(cache.get(key));
    }

    public void addValue(K key, V value) {
        cache.put(key, value);
    }

    public void setValue(K key, V value) {
        cache.remove(key);
        cache.put(key,value);
    }

    public void removeValue(K key) {
        cache.remove(key);
    }

    public Set<K> getKeys() {
        return cache.keySet();
    }

    public Collection<V> getValues() {
        return cache.values();
    }

    public void clear() {
        cache.clear();
    }

    public Map<K,V> getMap() {
        return cache;
    }

    public void putAll(HashMap<K,V> hashMap) {
        cache.putAll(hashMap);
    }
}
