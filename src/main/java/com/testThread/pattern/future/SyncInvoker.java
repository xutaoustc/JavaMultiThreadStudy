package com.testThread.pattern.future;

public class SyncInvoker {
    public static void main(String[] args) throws InterruptedException {
//        String result = get();
//        System.out.println(result);

        FutureService futureService = new FutureService();
        Future<String> future = futureService.submit(()->{
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "FINISH";
        });

        System.out.println("===========================");
        System.out.println(" do other thing.");
        System.out.println("===========================");

        System.out.println( future.get() );


        // 第二种方式，不是我主动询问，而是好了以后通知我(回调)
        futureService.submit(()->{
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "FINISH1";
        }, System.out::println);
        System.out.println("============================");
        System.out.println(" do other thing 1.");
        System.out.println("============================");


    }


    private static String get() throws InterruptedException {
        Thread.sleep(10000);
        return "FINISH";
    }
}
