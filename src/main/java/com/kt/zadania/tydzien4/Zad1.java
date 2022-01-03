package com.kt.zadania.tydzien4;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class Zad1 {
    public static void main(String[] args) {

        System.out.println(Thread.currentThread().getName() + " Starting...");

        AtomicInteger numberOfRejectedTasks = new AtomicInteger();

        ThreadPoolExecutor cachedExecutor = new ThreadPoolExecutor(
                1,
                4,
                30L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(10),
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        System.out.println(Thread.currentThread().getName()
                                + " I am full, sory bro");
                        System.out.println(Thread.currentThread().getName()
                                + " Number of rejected tasks: "
                                + numberOfRejectedTasks.incrementAndGet());
                    }
                }
        );

        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(Thread.currentThread().getName() + " Doing hard work...");
                    Thread.sleep(2000);
                    System.out.println(Thread.currentThread().getName() + " Done :)");
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
            }
        };

        while (numberOfRejectedTasks.get() < 10) {
            cachedExecutor.execute(task);
        }

        cachedExecutor.shutdown();

        System.out.println(Thread.currentThread().getName() + " DONE");
    }
}
