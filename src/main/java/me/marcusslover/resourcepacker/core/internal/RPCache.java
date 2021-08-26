package me.marcusslover.resourcepacker.core.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class RPCache {
    private static final Map<Class<?>, Map<String, ?>> CACHE = new HashMap<>();

    RPCache() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String name, Supplier<T> supplier, Class<T> clazz) {
        return get(name, new CacheSupplier<>(clazz) {
            @Override
            public T get() {
                return supplier.get();
            }
        });
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String name, CacheSupplier<T> supplier) {
        Class<T> type = supplier.getType();
        Map<String, T> map = (Map<String, T>) CACHE.getOrDefault(type, new HashMap<>());
        if (map.containsKey(name)) {
            return map.get(name);
        }
        T obj = supplier.get();
        map.put(name, obj);
        CACHE.put(type, map);

        return obj;
    }

    public static abstract class CacheSupplier<T> implements Supplier<T> {
        private final Class<T> type;

        public CacheSupplier(Class<T> type) {
            this.type = type;
        }

        public Class<T> getType() {
            return type;
        }
    }
}
