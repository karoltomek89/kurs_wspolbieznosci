package com.kt.lekcje.tydzien5;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

class CompletableFutureLesson6 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println(Thread.currentThread().getName() + " Starting...");

        CompletableFuture<Long> asyncLong = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " Computing...");
                Thread.sleep(1500);
                System.out.println(Thread.currentThread().getName() + " DONE");
                return 42L;

            } catch (InterruptedException e) {
                throw new IllegalStateException();
            }
        });

        CompletableFuture<Long> asyncLong2 = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " Computing...");
                Thread.sleep(1500);
                System.out.println(Thread.currentThread().getName() + " DONE");
                return 42L;

            } catch (InterruptedException e) {
                throw new IllegalStateException();
            }
        });

        Executor executor = Executors.newSingleThreadExecutor();

        final CompletableFuture<Void> future = asyncLong.thenAcceptAsync(value -> {
            try {
                System.out.println(Thread.currentThread().getName() + " Accepting value...");
                Thread.sleep(200);
                System.out.println(Thread.currentThread().getName() + " Consumed value is: " + value);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, executor);

        asyncLong.get();
        //future.get();

        CompletableFuture<Long> completableFuture = new CompletableFuture<>();
        //completableFuture.complete(99L);
        completableFuture.completeExceptionally(new RuntimeException("Ups..."));
        completableFuture.get();

        System.out.println(Thread.currentThread().getName() + " DONE");


    }

}