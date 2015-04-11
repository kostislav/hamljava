package cz.judas.jan.haml.util;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

public class InterleavedIterableTest {
    @Test
    public void emptyStaysEmpty() throws Exception {
        InterleavedIterable<String> interleavedIterable = new InterleavedIterable<>(Collections.emptyList(), "x");

        assertThat(ImmutableList.copyOf(interleavedIterable), is(empty()));
    }

    @Test
    public void insertsPaddingBetweenEachElement() throws Exception {
        InterleavedIterable<String> interleavedIterable = new InterleavedIterable<>(ImmutableList.of("a", "b", "c"), "x");

        assertThat(ImmutableList.copyOf(interleavedIterable), is(ImmutableList.of("a", "x", "b", "x", "c")));
    }
}
