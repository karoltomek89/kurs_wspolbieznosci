package com.kt.lekcje.tydzien4;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class ExecutorServiceLesson {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting...");

        AtomicInteger taskCounter = new AtomicInteger();

        ExecutorService executor = Executors.newFixedThreadPool(4);

        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    int id = taskCounter.getAndIncrement();
                    System.out.println(Thread.currentThread().getName() + " Doing hard work... " + id);
                    Thread.sleep(500);
                    System.out.println(Thread.currentThread().getName() + " DONE " + id);
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
            }
        };

        executor.execute(task);
        executor.execute(task);
        executor.execute(task);
        executor.execute(task);
        executor.execute(task);
        executor.execute(task);
        executor.execute(task);

        executor.shutdown();
        System.out.println("ExecutorService isShoutdown= " + executor.isShutdown()
        + " isTerminated=" + executor.isTerminated());

        executor.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("ExecutorService isShoutdown= " + executor.isShutdown()
                + " isTerminated=" + executor.isTerminated());

        System.out.println(Thread.currentThread().getName() + "Done");
    }

}
