package com.fjx.arms.integration.cache;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author guanzhirui
 * @date 2022/8/30 15:37
 */
public class IntelligentCache<V> implements Cache<String, V> {
    public static final String KEY_KEEP = "Keep=";
    private final Map<String, V> mMap;
    private final Cache<String, V> mCache;

    public IntelligentCache(int size) {
        this.mMap = new HashMap<>();
        this.mCache = new LruCache<>(size);
    }

    @NonNull
    public static String getKeyOfKeep(@NonNull String key) {
        return IntelligentCache.KEY_KEEP + key;
    }

    @Override
    public synchronized int size() {
        return mMap.size() + mCache.size();
    }

    @Override
    public synchronized int getMaxSize() {
        return mMap.size() + mCache.getMaxSize();
    }

    @Nullable
    @Override
    public synchronized V get(String key) {
        if (key.startsWith(KEY_KEEP)) {
            return mMap.get(key);
        }
        return mCache.get(key);
    }

    @Nullable
    @Override
    public synchronized V put(String key, V value) {
        if (key.startsWith(KEY_KEEP)) {
            return mMap.put(key, value);
        }
        return mCache.put(key, value);
    }

    @Nullable
    @Override
    public synchronized V remove(String key) {
        if (key.startsWith(KEY_KEEP)) {
            return mMap.remove(key);
        }
        return mCache.remove(key);
    }

    @Override
    public synchronized boolean containsKey(String key) {
        if (key.startsWith(KEY_KEEP)) {
            return mMap.containsKey(key);
        }
        return mCache.containsKey(key);
    }

    @Override
    public synchronized Set<String> keySet() {
        Set<String> set = mCache.keySet();
        set.addAll(mMap.keySet());
        return set;
    }

    @Override
    public void clear() {
        mCache.clear();
        mMap.clear();
    }
}