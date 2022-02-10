package com.kt.zadania.tydzien6;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Zad1 {

    public static void main(String[] args) throws InterruptedException {

        print("Starting...");
        Random random = new Random();
        BlockingQueue<CacheObject> queue = new ArrayBlockingQueue<>(10);

        Runnable producer = () -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Thread.sleep(100 + random.nextInt(1500));
                    print("Adding cacheObject object");
                    CacheObject cacheObject = new CacheObject(Instant.now());
                    queue.put(cacheObject);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        Runnable consumer = new Runnable() {
            @Override
            public void run() {
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        CacheObject cacheObject = queue.take();
                        print("Object consumed, time in cache: "
                                + Duration.between(Instant.now(), cacheObject.createdTime).toMillis() + " ms ");
                        Thread.sleep(100 + random.nextInt(2500));
                    }
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        Runnable remover = new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        Thread.sleep(100);
                        if (queue.size() > 0) {
                            long result = Duration.between(queue.peek().createdTime, Instant.now()).toMillis();
                            print("Checking time for first cacheObject from queue: " + result);
                            if (result > 10_000L) {
                                print("Object removed, time in cache: "
                                        + Duration.between(queue.peek().createdTime, Instant.now()).toMillis() + " ms ");
                                queue.take();
                            }
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        };

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        executorService.execute(producer);
        executorService.execute(consumer);
        executorService.execute(remover);

        Thread.sleep(60000);
        print("DONE");
        executorService.shutdownNow();
    }

    static void print(Object output) {
        System.out.printf("%s: %s - %s%n", LocalDateTime.now(), Thread.currentThread().getName(), output);
    }

    static class CacheObject {
        Instant createdTime;

        CacheObject(Instant createdTime) {
            this.createdTime = createdTime;
        }
    }
}
