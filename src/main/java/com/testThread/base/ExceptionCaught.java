package com.testThread.base;

public class ExceptionCaught {
    public static void main(String[] args) {
        Thread t = new Thread(()->{
            try {
                while(true){
                    Thread.sleep(1_000);
                    int result = 1/0;
                    System.out.println(result);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t.setUncaughtExceptionHandler((thread,e)->{
            System.out.println(e);
            System.out.println(thread);
        });

        t.start();


        try {
            Thread.sleep(5_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(t.isAlive());

    }


}
