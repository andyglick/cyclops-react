package com.aol.cyclops.internal.stream.spliterators;

import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

//@AllArgsConstructor
public class ReversingArraySpliterator<T> implements Spliterator<T>, ReversableSpliterator<T> {

    private final Object[] array;
    @Getter
    @Setter
    private boolean reverse;

    int index = 0;

    public ReversingArraySpliterator(Object[] array,boolean reverse) {
        this.array = array;
        this.reverse=reverse;
        this.index = calcIndex();
    }

    @Override
    public long estimateSize() {
        return array.length;
    }

    @Override
    public int characteristics() {
        return IMMUTABLE;
    }

    @Override
    public ReversingArraySpliterator<T> invert() {
        System.out.println("inverting!");
        setReverse(!isReverse());
        index = calcIndex();
        return this;
    }

    private int calcIndex() {
        if(isReverse()) {
            return array.length - 1;
        }else{
            return 0;
        }
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {
        Objects.requireNonNull(action);

        int index = this.index; //local index for replayability

        if (!reverse) {
            for (;index < array.length && index > -1;) {
                action.accept((T) array[index++]);

            }
        } else {
            for (;index > -1 & index < array.length;) {
                action.accept((T) array[index--]);

            }
        }

    }

    @Override
    public boolean tryAdvance(final Consumer<? super T> action) {
        Objects.requireNonNull(action);



        if (!reverse) {
            if (index < array.length && index > -1) {
                action.accept((T) array[index++]);
                return true;
            }
        } else {
            if (index > -1 & index < array.length) {
                action.accept((T) array[index--]);
                return true;
            }
        }
        return false;

    }

    @Override
    public Spliterator<T> trySplit() {

        return this;
    }

    @Override
    public ReversableSpliterator<T> copy() {

        return new ReversingArraySpliterator<T>(
                                                array, reverse);
    }

}
