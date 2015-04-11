package cz.judas.jan.haml.util;

import java.util.Iterator;

public class InterleavedIterable<T> implements Iterable<T> {
    private final Iterable<? extends T> original;
    private final T padding;

    public InterleavedIterable(Iterable<? extends T> original, T padding) {
        this.original = original;
        this.padding = padding;
    }

    @Override
    public Iterator<T> iterator() {
        return new InterleavedIterator<>(original.iterator(), padding);
    }

    private static class InterleavedIterator<T> implements Iterator<T> {
        private final Iterator<? extends T> original;
        private final T padding;
        private int counter = 0;

        private InterleavedIterator(Iterator<? extends T> original, T padding) {
            this.original = original;
            this.padding = padding;
        }

        @Override
        public boolean hasNext() {
            return original.hasNext();
        }

        @Override
        public T next() {
            counter++;
            if(counter % 2 == 0) {
                return padding;
            } else {
                return original.next();
            }
        }
    }
}
