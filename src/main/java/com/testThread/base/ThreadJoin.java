package com.testThread.base;

public class ThreadJoin {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(()->{
            for(int i =0;i<10000;i++)
                System.out.println(Thread.currentThread().getName() + " " + i);
        });
        Thread t2 = new Thread(()->{
            for(int i =0;i<10000;i++)
                System.out.println(Thread.currentThread().getName() + " " + i);
        });

        t1.start();
        t2.start();
        // Waits for this thread to die. 但是这两个线程会并行执行，他们的join都相对于它的父线程来说
        t1.join();
        t2.join();

        for(int i =0;i<10000;i++)
            System.out.println(Thread.currentThread().getName() + " " + i);

    }
}
