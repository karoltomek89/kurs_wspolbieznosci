package com.kt.lekcje.tydzien5;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

class CompletableFutureLesson4 {
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


        final CompletableFuture<Long> futureValue = new MyTask(-5).square();

        final CompletableFuture<Long> future = futureValue.whenComplete(new BiConsumer<Long, Throwable>() {
            @Override
            public void accept(Long aLong, Throwable throwable) {
                if (throwable == null) {
                    System.out.println("Calculated long is: " + aLong);
                } else {
                    System.out.println("Something went wrong: " + throwable.getMessage());
                }
                System.out.println("Throwable is: " + throwable);
            }
        });

        final CompletableFuture<Optional<Long>> future2 = futureValue.handle(new BiFunction<Long, Throwable, Optional<Long>>() {
            @Override
            public Optional<Long> apply(Long aLong, Throwable throwable) {
                if (throwable == null) {
                    System.out.println("Calculated long is: " + aLong);
                    return Optional.of(aLong);
                } else {
                    System.out.println("Something went wrong: " + throwable.getMessage());
                    return Optional.empty();
                }
            }
        });


        System.out.println("Result is:" + future2.get());

        System.out.println(Thread.currentThread().getName() + " DONE");
    }

    static class MyTask {
        private long value;

        MyTask(long value) {
            this.value = value;
        }

        CompletableFuture<Long> square() {
            return CompletableFuture.supplyAsync(() -> {
                if (value < 0) {
                    throw new IllegalArgumentException("Argument must be equal or higher than 0");
                }
                try {
                    System.out.println(Thread.currentThread().getName() + " Computing...");
                    Thread.sleep(500);
                    System.out.println(Thread.currentThread().getName() + " DONE");
                    return value * value;

                } catch (InterruptedException e) {
                    throw new IllegalStateException();
                }
            });
        }
    }
}

