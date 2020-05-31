package com.testThread.juc;

import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerTest {
    public static void main(String[] args) throws InterruptedException {
        AtomicTest at = new AtomicTest();

        Thread t1 = new Thread(()->{
            for(int i=0;i<500;i++)
                at.add();
        });
        Thread t2 = new Thread(()->{
            for(int i=0;i<500;i++)
                at.add();
        });
        Thread t3 = new Thread(()->{
            for(int i=0;i<500;i++)
                at.add();
        });

        t1.start();
        t2.start();
        t3.start();
        t1.join();
        t2.join();
        t3.join();

        System.out.println(at.getSize());
    }
}

class AtomicTest{
    private AtomicInteger ai = new AtomicInteger(0);
    private Set<Integer> all = Sets.newConcurrentHashSet();

    public void add(){

        all.add(ai.incrementAndGet());
    }

    public int getSize(){
        return all.size();
    }
}
