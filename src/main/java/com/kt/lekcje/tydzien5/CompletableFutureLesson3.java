package com.kt.lekcje.tydzien5;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

class CompletableFutureLesson3 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println(Thread.currentThread().getName() + " Starting...");

        CompletableFuture<Long> asyncLong = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " Computing...");
                Thread.sleep(500);
                System.out.println(Thread.currentThread().getName() + " DONE");
                return 42L;

            } catch (InterruptedException e) {
                throw new IllegalStateException();
            }
        });

        CompletableFuture<Long> asyncLong2 = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " Computing...");
                Thread.sleep(500);
                System.out.println(Thread.currentThread().getName() + " DONE");
                return 97L;

            } catch (InterruptedException e) {
                throw new IllegalStateException();
            }
        });

        CompletableFuture<String> asyncString = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " Computing...");
                Thread.sleep(100);
                System.out.println(Thread.currentThread().getName() + " DONE");
                return "Jon Smith";

            } catch (InterruptedException e) {
                throw new IllegalStateException();
            }
        });


        final CompletableFuture<String> asyncTask = asyncLong
                .thenCompose(CompletableFutureLesson3::asyncAnswer);

        final CompletableFuture<Void> future = asyncLong.acceptEither(asyncLong2,
                v -> System.out.println("The faster one was: " + v));

        final CompletableFuture<Void> future1 = asyncLong.thenAcceptBoth(asyncLong2,
                (aLong, aLong2) -> System.out.println("The values are: " + aLong + " and " + aLong2));


        final CompletableFuture<Object> future2 = CompletableFuture.anyOf(asyncLong, asyncLong2, asyncTask, asyncString);

        final CompletableFuture<Void> future3 = CompletableFuture.allOf(asyncLong, asyncLong2, asyncTask, asyncString);

        System.out.println(Thread.currentThread().getName() + "Async Result is: " + future.get());
        System.out.println(Thread.currentThread().getName() + "Async Result is: " + future1.get());

        System.out.println(Thread.currentThread().getName() + "Async Result is: " + future2.get());
        System.out.println(Thread.currentThread().getName() + "Async Result is: " + future3.get());
        System.out.println("value is: " + asyncLong.get());
        System.out.println("value is: " + asyncLong2.get());
        System.out.println("value is: " + asyncTask.get());
        System.out.println("value is: " + asyncString.get());

        System.out.println(Thread.currentThread().getName() + " DONE");
    }

    static CompletableFuture<String> asyncAnswer(Long value) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " Computing...");
                Thread.sleep(500);
                System.out.println(Thread.currentThread().getName() + " DONE");
                return "Answer to all questions is: " + value;
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        });
    }
}