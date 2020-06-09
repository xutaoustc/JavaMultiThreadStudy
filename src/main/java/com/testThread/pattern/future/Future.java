package com.testThread.pattern.future;

public interface Future<T> {
    T get() throws InterruptedException;
}
