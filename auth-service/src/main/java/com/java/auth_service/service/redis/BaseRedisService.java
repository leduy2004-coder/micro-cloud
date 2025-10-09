package com.java.auth_service.service.redis;
import java.util.List;
import java.util.Map;
import java.util.Set;
public interface BaseRedisService<K,F,V> {
    void set(K key, V value);

    void setTimeToLive(K key, long timeoutInDays);

    void hashSet(K key, F field, V value);

    boolean hashExists(K key, F field);

    boolean hasKey(K key);
    V get(K key);

    public Map<F, V> getField(K key);

    V hashGet(K key, F field);

    List<V> hashGetByFieldPrefix(K key, F filedPrefix);

    Set<F> getFieldPrefixes(K key);

    void delete(K key);

    void delete(K key, F field);

    void delete(K key, List<F> fields);
}
