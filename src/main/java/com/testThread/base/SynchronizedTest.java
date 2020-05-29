package com.testThread.base;

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
        // 只要一个任务获取了锁，那么在它执行完退出锁之前，是不会让出锁的
        synchronized (this){
            while(true){
                System.out.println(".....");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
