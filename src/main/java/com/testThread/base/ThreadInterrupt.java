package com.testThread.base;

public class ThreadInterrupt {
    public static void main(String[] args) throws InterruptedException {
        // 1、没有任何语言方面的需求一个被中断的线程应该终止。中断一个线程只是为了引起该线程的注意，被中断线程可以决定如何应对中断。
        // 2、Thread.interrupt()方法不会中断一个正在运行的线程。
        // 3、如果线程在调用 Object 类的 wait()、wait(long) 或 wait(long, int) 方法，或者该类的 join()、join(long)、join(long, int)、sleep(long) 或 sleep(long, int) 方法过程中受阻，则其中断状态将被清除，它还将收到一个InterruptedException异常。这个时候，我们可以通过捕获InterruptedException异常来终止线程的执行，具体可以通过return等退出或改变共享变量的值使其退出。

        // 注意在join时，要打断的线程是join的父线程
        Thread t = new Thread(()->{
           while(true){
               try {
                   Thread.sleep(1000);
               } catch (InterruptedException e) {
                   break;
               }
           }
        });
        t.start();

        Thread.sleep(5000);
        System.out.println(t.isInterrupted());
        t.interrupt();
        System.out.println(t.isInterrupted());
    }
}
