package com.testThread.base;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

public class SimpleThreadPool {
    private final int queueSize;
    private final static LinkedList<Runnable> TASK_QUEUE = new LinkedList<>();

    private final int threadPoolSize;
    private final static List<WorkerTask> THREAD_QUEUE = new ArrayList();
    private static final ThreadGroup GROUP = new ThreadGroup("Pool_Group");
    private static final String THREAD_PREFIX = "SIMPLE_THREAD_POOL-";
    private static volatile int seq = 0;

    private volatile boolean destroy = false;

    private final DiscardPolicy discardPolicy;
    public static final DiscardPolicy DEFAULT_DISCARD_POLICY = ()->{
        throw new DiscardException("Discard this task");
    };


    public SimpleThreadPool(int threadPoolSize, int queueSize, DiscardPolicy discardPolicy){
        this.threadPoolSize = threadPoolSize;
        this.queueSize = queueSize;
        this.discardPolicy = discardPolicy==null? DEFAULT_DISCARD_POLICY : discardPolicy;
        init();
    }

    private void init(){
        for(int i = 0;i < threadPoolSize;i++){
            WorkerTask task = new WorkerTask(GROUP, THREAD_PREFIX + (seq++));
            task.start();
            THREAD_QUEUE.add(task);
        }
    }


    public void submit(Runnable runnable){
        if(destroy)
            throw new IllegalStateException("The thread pool is already destroy and not allow to submit task");

        synchronized (TASK_QUEUE){
            if(TASK_QUEUE.size() >= queueSize)
                discardPolicy.discard();

            TASK_QUEUE.addLast(runnable);
            TASK_QUEUE.notifyAll();
        }
    }

    public void shutdown(){
        this.destroy = true;

        while(!TASK_QUEUE.isEmpty()){
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        int initVal = THREAD_QUEUE.size();
        while (initVal > 0) {
            for(WorkerTask task : THREAD_QUEUE){
                if(task.getTaskState() == TaskState.BLOCKED){
                    task.interrupt();
                    task.close();
                    initVal--;
                }
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private enum TaskState{
        FREE,RUNNING,BLOCKED,DEAD
    }


    public static class DiscardException extends RuntimeException{
        public DiscardException(String message){
            super(message);
        }
    }
    public interface DiscardPolicy{
        void discard() throws DiscardException;
    }

    private static class WorkerTask extends Thread{
        private volatile TaskState taskState = TaskState.FREE;

        public TaskState getTaskState(){
            return taskState;
        }

        public WorkerTask(ThreadGroup group, String name){
            super(group,name);
        }

        public void run(){
            OUTER:
            while(this.taskState != TaskState.DEAD){
                Runnable runnable;
                synchronized (TASK_QUEUE) {
                    while (TASK_QUEUE.isEmpty()) {
                        try {
                            taskState = TaskState.BLOCKED;
                            TASK_QUEUE.wait();
                        } catch (InterruptedException e) {
                            break OUTER;
                        }
                    }
                    runnable = TASK_QUEUE.removeFirst();
                }

                if(runnable != null){
                    taskState = TaskState.RUNNING;
                    runnable.run();
                    taskState = TaskState.FREE;
                }
            }

        }

        public void close(){
            this.taskState = TaskState.DEAD;
        }
    }


    public static void main(String[]args){
        SimpleThreadPool simpleThreadPool = new SimpleThreadPool(10,40, SimpleThreadPool.DEFAULT_DISCARD_POLICY);
//        SimpleThreadPool simpleThreadPool = new SimpleThreadPool(6,10, SimpleThreadPool.DEFAULT_DISCARD_POLICY);
        IntStream.rangeClosed(0,40)
                .forEach(i->{
                    simpleThreadPool.submit(()->{
                        System.out.println("The runnable " + i + " be serviced by " + Thread.currentThread() + " start");
                        try {
                            Thread.sleep(1_000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("The runnable " + i + " be serviced by " + Thread.currentThread() + " finished");
                    });
                });

        simpleThreadPool.shutdown();
    }
}
