package com.kt.lekcje.tydzien5;

import java.util.concurrent.*;

class FutureLesson2a {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        System.out.println(Thread.currentThread().getName() + " Starting...");

        Callable<Long> longRunningTask = () -> {
            System.out.println(Thread.currentThread().getName() + " Starting hard work...");
            Thread.sleep(15_000);
            System.out.println(Thread.currentThread().getName() + " Done, returning result...");
            return 123L;
        };

        ExecutorService es = Executors.newFixedThreadPool(4);

        Future<Long> future = es.submit(longRunningTask);

        if (!future.isDone()) {
            Thread.sleep(1_000);
        }
        if (!future.isDone()) {
            boolean cancelled = future.cancel(true);
            System.out.println("Is canncelled: " + cancelled);
            long result = future.get();
            System.out.println("Result: " + result);
        }
        System.out.println(Thread.currentThread().getName() + " DONE");
    }

}
