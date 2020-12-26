package it.fireentity.library.storage;

import it.fireentity.library.interfaces.Cacheable;

public class Cache<K,V extends Cacheable<K>> extends GenericCache<K,V> {


    public void addValue(V v) {
        addValue(v.getKey(), v);
    }
}
