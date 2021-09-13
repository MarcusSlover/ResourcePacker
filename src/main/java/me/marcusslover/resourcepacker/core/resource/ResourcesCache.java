/*
 * MIT License
 *
 * Copyright (c) 2021 MarcusSlover
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package me.marcusslover.resourcepacker.core.resource;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ResourcesCache<V> {
    /*All caches*/
    private static StringCache STRING_CACHE;
    private static IntegerCache INTEGER_CACHE;

    protected final Map<Class<?>, Map<V, ?>> CACHE = new HashMap<>();
    private final Class<V> type;

    private ResourcesCache(@NotNull Class<V> type) {
        this.type = type;
    }

    public static StringCache string() {
        if (STRING_CACHE == null) STRING_CACHE = new StringCache(String.class);
        return STRING_CACHE;
    }

    public static IntegerCache integer() {
        if (INTEGER_CACHE == null) INTEGER_CACHE = new IntegerCache(Integer.class);
        return INTEGER_CACHE;
    }

    public <T> T get(@Nullable V key, @NotNull Supplier<T> supplier, @NotNull Class<T> clazz) {
        return get(key, new CacheSupplier<T>(clazz) {
            @Override
            public T get() {
                return supplier.get();
            }
        });
    }

    @SuppressWarnings("unchecked")
    public <T> T get(@Nullable V key, @NotNull CacheSupplier<T> supplier) {
        if (key == null) return supplier.get();

        Class<T> type = supplier.getType();
        Map<V, T> map = (Map<V, T>) CACHE.getOrDefault(type, new HashMap<>());
        if (map.containsKey(key)) return map.get(key);
        T obj = supplier.get();
        map.put(key, obj);
        CACHE.put(type, map);

        return obj;
    }

    public static class StringCache extends ResourcesCache<String> {
        private StringCache(@NotNull Class<String> type) {
            super(type);
        }
    }

    public static class IntegerCache extends ResourcesCache<Integer> {
        private IntegerCache(@NotNull Class<Integer> type) {
            super(type);
        }
    }


    public static abstract class CacheSupplier<T> implements Supplier<T> {
        private final Class<T> type;

        public CacheSupplier(@NotNull Class<T> type) {
            this.type = type;
        }

        public Class<T> getType() {
            return type;
        }
    }
}
