package com.kt.lekcje.tydzien5;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

class CompletetableFutureLesson1 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println(Thread.currentThread().getName() + " Starting...");

        CompletableFuture<Long> cf = new CompletableFuture<>();

        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(Thread.currentThread().getName() + " Doing hard work...");
                    Thread.sleep(500);
                    cf.complete(42L);
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
            }
        };


        Executor executor = Executors.newFixedThreadPool(4);
        executor.execute(task);

        Long result = cf.get();

        System.out.println(Thread.currentThread().getName() + " Result is: " + result);

        CompletableFuture<String> future = CompletableFuture.completedFuture("I've already got this one");
        String result2 = future.get();

        System.out.println(Thread.currentThread().getName() + " " + result2);

        final CompletableFuture<Long> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " Doing hard work...");
                Thread.sleep(500);
                return 123L;
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        });

        Long result3 = future2.get();

        System.out.println(Thread.currentThread().getName() + " " + result3);

        System.out.println(Thread.currentThread().getName() + " DONE");
    }

}
