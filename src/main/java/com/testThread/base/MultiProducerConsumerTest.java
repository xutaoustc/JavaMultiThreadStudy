package com.testThread.base;

import java.util.stream.Stream;

// 原先的ProducerConsumer类在多个生产者或者消费者的时候会有问题
public class MultiProducerConsumerTest {
    public static void main(String[] args) {
        ProducerConsumerMulti producerConsumer = new ProducerConsumerMulti();

        Stream.of("P1","P2").forEach(name->
            new Thread(
                ()-> {
                    while(true) {
                        try {
                            producerConsumer.produce();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                },
                name
            ).start()
        );

        Stream.of("C1","C2").forEach(name->
            new Thread(
                ()->{
                    while(true) {
                        try {
                            producerConsumer.consume();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                },
                name
            ).start()
        );

    }
}


class ProducerConsumerMulti{
    private int i =0;
    public Object LOCK = new Object();
    private boolean isProduced = false;

    public void produce() throws InterruptedException {
        synchronized (LOCK) {
            // 这里和单Producer Consumer不同的一点是，要用while代替if，因为防止notifyAll之后，重复生产
            while(isProduced) {
                System.out.println(Thread.currentThread().getName() + " wait ");
                LOCK.wait();
                System.out.println(Thread.currentThread().getName() + " wait end");
            }

            i++;
            System.out.println(Thread.currentThread().getName() + " -> " + i);
            isProduced = true;
            LOCK.notifyAll();  // 这里要改成notifyAll，让大家都参与抢锁，防止同类之间抢锁
        }
    }

    public void consume() throws InterruptedException {
        synchronized (LOCK){
            while (!isProduced) {
                System.out.println(Thread.currentThread().getName() + " wait ");
                LOCK.wait();
                System.out.println(Thread.currentThread().getName() + " wait end");
            }

            System.out.println(Thread.currentThread().getName() + " -> " + i);
            isProduced = false;
            LOCK.notifyAll();
        }
    }
}
