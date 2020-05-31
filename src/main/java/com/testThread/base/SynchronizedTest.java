package com.testThread.base;

import java.math.BigInteger;

public class SynchronizedTest {
    public static void main(String[] args) throws InterruptedException {
        Business b = new Business();

        Thread t1 = new Thread(()->{
            b.longJob();
        });

        Thread t2 = new Thread(()->{
            b.anotherThing();
        });

        t1.start();
        Thread.sleep(1000);
        t2.start();

    }
}


class Business{
    public void longJob() {
        BigInteger bi = new BigInteger("0");

        // 只要一个任务获取了锁，那么在它执行完退出锁之前，是不会让出锁的
        synchronized (this){
            while(true){
                System.out.println(".....");
                for(int i=0;i<100_000_000;i++)
                    bi.add(new BigInteger("1"));
            }
        }
    }

    public void anotherThing(){
        synchronized (this){
            while(true){
                System.out.println("another");
            }
        }
    }
}
