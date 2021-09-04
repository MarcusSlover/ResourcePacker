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

package me.marcusslover.resourcepacker.core.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class RPCache {
    private static final Map<Class<?>, Map<String, ?>> CACHE = new HashMap<>();

    RPCache() {
    }

    public static <T> T get(String name, Supplier<T> supplier, Class<T> clazz) {
        return get(name, new CacheSupplier<T>(clazz) {
            @Override
            public T get() {
                return supplier.get();
            }
        });
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String name, CacheSupplier<T> supplier) {
        if (name == null) return supplier.get();

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
