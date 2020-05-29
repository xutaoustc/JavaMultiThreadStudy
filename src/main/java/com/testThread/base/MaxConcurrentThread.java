package com.testThread.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// 最多并行执行的线程效果
public class MaxConcurrentThread {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        List<Thread> allThreads = new ArrayList<>();
        ThreadTask task = new ThreadTask();

        Arrays.asList("T1","T2","T3","T4","T5","T6","T7","T8","T9").stream().forEach(
                (name)->{
                    Thread t = new Thread(()->{
                        task.doThings();
                    },name);

                    allThreads.add(t);
                    t.start();
                }
        );

        allThreads.stream().forEach(x-> {
            try {
                x.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });


        System.out.println("All task has finished in " + (System.currentTimeMillis()-start));
    }
}


class ThreadTask{
    private int count = 0;
    private int MAX_CONCURRENT_SIZE = 5;

    public void doThings(){
        // 这里要注意分段加锁，把业务逻辑不加锁，否则达不到并行的效果
        synchronized (this) {
            while (count >= MAX_CONCURRENT_SIZE) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            count++;
        }

        System.out.println("do things ...");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) { e.printStackTrace(); }

        synchronized (this) {
            System.out.println("do things finished...");
            count--;
            this.notifyAll();
        }
    }
}