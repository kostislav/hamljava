package cz.judas.jan.haml;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class ShortCollections {
    @SafeVarargs
    public static <T> List<T> list(T... items) {
        return ImmutableList.copyOf(items);
    }
}
