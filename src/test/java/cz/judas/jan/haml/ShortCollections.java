package cz.judas.jan.haml;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;

public class ShortCollections {
    @SafeVarargs
    public static <T> List<T> list(T... items) {
        return ImmutableList.copyOf(items);
    }

    public static <K, V> Map<K, V> map(K key, V value) {
        return ImmutableMap.of(key, value);
    }

    public static <K, V> Map<K, V> map(K key1, V value1, K key2, V value2) {
        return ImmutableMap.of(key1, value1, key2, value2);
    }
}
