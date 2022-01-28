package com.kt.lekcje.tydzien5;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;

class CompletableFutureLesson2 {
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

//        var result = asyncLong.get();
//
//        System.out.println("Value is: " + result);

        CompletableFuture<String> asyncResult = asyncLong.thenApply(v -> {
            return "The answer to all questions is... " + v;
        });

        CompletableFuture<String> asyncResult2 = asyncLong
                .thenApply(v -> v * v).
                        thenApply(v -> {
                            try {
                                Thread.sleep(500);
                                return v / 4;
                            } catch (InterruptedException e) {
                                throw new IllegalStateException(e);
                            }
                        }).
                        thenApply(v -> {
                            return "Final value is: " + v;
                        });

        System.out.println("Async Result is: " + asyncResult.get());

        System.out.println("Async Result2 is: " + asyncResult2.get());



        CompletableFuture<String> asyncString = CompletableFuture.supplyAsync(() -> {

            try {
                System.out.println(Thread.currentThread().getName() + " Computing...");
                Thread.sleep(500);
                System.out.println(Thread.currentThread().getName() + " DONE");
                return "Consumer name is John Smith";

            } catch (InterruptedException e) {
                throw new IllegalStateException();
            }
        });

        CompletableFuture<String> asyncString2 = CompletableFuture.supplyAsync(() -> {

            try {
                System.out.println(Thread.currentThread().getName() + " Computing...");
                Thread.sleep(500);
                System.out.println(Thread.currentThread().getName() + " DONE");
                return " And it is not important";

            } catch (InterruptedException e) {
                throw new IllegalStateException();
            }
        });

        final CompletableFuture<String> asyncTask = asyncString.thenCombine(asyncString2, new BiFunction<String, String, String>() {
            @Override
            public String apply(String s, String s2) {
                return s + s2;
            }
        });
        System.out.println(Thread.currentThread().getName() + "Async Result is: " +  asyncTask.get());

        System.out.println(Thread.currentThread().getName() + " DONE");
    }
}