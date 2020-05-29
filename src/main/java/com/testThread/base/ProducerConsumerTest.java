package com.testThread.base;

// 线程间通信不是指多个线程去做同一件事情，而是不同的线程去做不同的事情
// 这里我当时实现的时候没有想到多个线程可以共用一个业务类对象。实现Runnable的类不一定要包含业务，它可能只是个壳，去调用真正的业务本身
public class ProducerConsumerTest {
    public static void main(String[] args) {
        ProducerConsumer producerConsumer = new ProducerConsumer();


        Thread producer = new Thread(()->{
            while(true) {
                try {
                    producerConsumer.produce();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"P");
        producer.start();

        Thread consumer = new Thread(()->{
            while(true) {
                try {
                    producerConsumer.consume();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"C");
        consumer.start();
    }
}


// 被notify的程序不会立刻执行，甚至不会立刻获得锁
// 这个程序在多生产者，多消费者的情况下不能用
// 因为notify的时候不知道notify谁
//        P1 -> 1           P1抢到锁，P1生产    (P1*,P2,C1,C2) -> (P1,P2,C1,C2)
//        P1 wait           P1抢到锁，P1阻塞    (P1*,P2,C1,C2) -> (P2,C1,C2)
//        P2 wait           P2抢到锁，P2阻塞    (P2*,C1,C2) -> (C1,C2)
//        C1 -> 1           C1抢到锁，C1消费，主动唤醒一个  (C1*,C2) -> (C1,C2,P1)
//        C1 wait           C1抢到锁，C1阻塞    (C1*,C2,P1) -> (C2,P1)
//        C2 wait           C2抢到锁，C2阻塞    (C2*,P1) -> (P1)
//        P1 wait end       P1抢到锁
//        P1 -> 2           P1生产，主动唤醒一个  (P1*) -> (P1,P2)
//        P1 wait           P1抢到锁，P1阻塞    (P1*,P2) -> (P2)
//        P2 wait end       P2抢到锁
//        P2 wait           P2阻塞    (P2*) -> ()
class ProducerConsumer{
    private int i =0;
    public Object LOCK = new Object();
    private boolean isProduced = false;

    public void produce() throws InterruptedException {
        synchronized (LOCK) {
            if(isProduced) {
                System.out.println(Thread.currentThread().getName() + " wait ");
                LOCK.wait();
                System.out.println(Thread.currentThread().getName() + " wait end");
            }

            i++;
            System.out.println(Thread.currentThread().getName() + " -> " + i);
            isProduced = true;
            LOCK.notify();
        }
    }

    public void consume() throws InterruptedException {
        synchronized (LOCK) {
            if (!isProduced) {
                System.out.println(Thread.currentThread().getName() + " wait ");
                LOCK.wait();
                System.out.println(Thread.currentThread().getName() + " wait end");
            }

            System.out.println(Thread.currentThread().getName() + " -> " + i);
            isProduced = false;
            LOCK.notify();
        }
    }
}
