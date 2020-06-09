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
    }


    private static String get() throws InterruptedException {
        Thread.sleep(10000);
        return "FINISH";
    }
}
