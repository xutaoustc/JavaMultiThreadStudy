package com.testThread.base;

import java.util.Arrays;

public class StackTrace {
    public static void main(String[] args) {
        Arrays.stream(Thread.currentThread().getStackTrace())
                .filter(x->!x.isNativeMethod())
                .forEach(e-> System.out.println(e.getClassName() + " " + e.getMethodName() + " " + e.getLineNumber()));
    }
}
