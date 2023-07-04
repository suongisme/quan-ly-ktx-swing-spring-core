package com.ktx.core.utils;

import com.ktx.core.constant.CacheEnum;
import com.ktx.core.model.Cache;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class CacheUtils {

    private final static ConcurrentMap<CacheEnum, Cache<?>> caches = new ConcurrentHashMap<>();

    /**
     * set value to caches
     * @param key: key
     * @param expire: miliseconds
     * @param value: value
     * @param <T>: type
     */
    public <T> void set(CacheEnum key, Long expire, T value) {
        caches.put(key, new Cache<T>()
                .setValue(value)
                .setExpire(new Date(System.currentTimeMillis() + expire))
        );
    }

    /**
     * return null if key not exist or value is expired
     * @param key key
     * @param tClass: type
     * @return value
     * @param <T> type
     */
    @Nullable
    public <T> T get(CacheEnum key, Class<T> tClass) {
        Cache<?> cache = caches.get(key);
        if (Objects.isNull(cache)) {
            return null;
        }
        Date expire = cache.getExpire();
        if (new Date().after(expire)) {
            this.remove(key);
            return null;
        }
        return (T) cache.getValue();
    }

    public void remove(CacheEnum key) {
        caches.remove(key);
    }
}
