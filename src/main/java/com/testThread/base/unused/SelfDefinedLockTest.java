package com.testThread.base.unused;

import java.util.*;
import java.util.stream.Collectors;

public class SelfDefinedLockTest {
    public static void main(String[] args) {
        SelfDefinedLock lock = new SelfDefinedLock();

        Arrays.asList("T1","T2","T3","T4","T5").stream().forEach(
                (name)->{
                    new Thread(()->{
                        try {
                            lock.lock();
                            System.out.println(Thread.currentThread().getName() + " get monitor     " + lock.getWaitedThread().stream().map(Thread::getName).collect(Collectors.joining(","))+ " waiting");
                            System.out.println(Thread.currentThread().getName() + " working..."+"waited thread" + new Date() );
                            try {
                                Thread.sleep(10_000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }finally {
                            lock.unlock();
                        }
                    },name).start();
                }
        );
    }
}

class SelfDefinedLock{
    private Set<Thread> waitedThread = new HashSet<>();
    private volatile boolean isLocked = false;

    public void lock(){
        synchronized (this){
            while(isLocked){
                try {
                    waitedThread.add( Thread.currentThread() );
                    this.wait();
                } catch (InterruptedException e) { e.printStackTrace(); }
            }
            waitedThread.remove(Thread.currentThread());

            isLocked = true;
        }
    }

    public void unlock(){
        synchronized (this){
            isLocked = false;
            this.notifyAll();
        }
    }

    public Set<Thread> getWaitedThread(){
        return Collections.unmodifiableSet(waitedThread);
    }
}
