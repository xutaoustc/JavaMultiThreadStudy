package com.testThread.base;

public class ThreadSafe {
    public static void main(String[] args) {
        TicketWindow tw = new TicketWindow();

        final int TICKET_COUNT = 5000;
        Thread t1 = new Thread(()->{
            for(int i =1;i<=TICKET_COUNT;i++)
                tw.sell();
        },"一号");
        Thread t2 = new Thread(()->{
            for(int i =1;i<=TICKET_COUNT;i++)
                tw.sell();
        },"二号");
        Thread t3 = new Thread(()->{
            for(int i =1;i<=TICKET_COUNT;i++)
                tw.sell();
        },"三号");

        t1.start();
        t2.start();
        t3.start();
    }
}


class TicketWindow  {
    private volatile static int index = 1;
    private Object LOCK_OBJ = new Object();

    // 多线程程序包含两部分，代码和变量状态
    // 看待多线程程序时，永远要假设同一行"代码"在同一"时间"有多个"线程"在执行
    // 进入synchronized相当于变成单线程
    public void sell(){
        int temp0;
        synchronized (LOCK_OBJ){
            temp0 = index;
            int temp1 = temp0 + 1;
            index = temp1;
        }
        //非共享代码不会产生线程安全问题，因为线程虽然运行同一套代码，但是局部变量的值大家并不共享
        System.out.println(Thread.currentThread().getName() + " 号码：" + temp0);
    }
}
