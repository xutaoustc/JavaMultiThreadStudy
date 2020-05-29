package com.testThread.base;

public class Deadlock {
    public static void main(String[] args) {
        Thread t1 = new Thread(new Task1());
        Thread t2 = new Thread(new Task2());

        t1.start();
        t2.start();
    }
}

class Task1 implements Runnable{

    @Override
    public void run() {
        synchronized (Task1.class){
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (Task2.class){

            }
        }
    }
}

class Task2 implements Runnable{

    @Override
    public void run() {
        synchronized (Task2.class){
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (Task1.class){

            }
        }
    }
}
