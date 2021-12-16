package com.kt.lekcje.tydzien4;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

class ExecutorsLesson {

    public static void main(String[] args) {
        System.out.println("Starting...");

        AtomicInteger taskCounter = new AtomicInteger();

        Executor executor = Executors.newFixedThreadPool(4);

        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    int id = taskCounter.getAndIncrement();
                    System.out.println(Thread.currentThread().getName() +" Doing hard work... " + id);
                    Thread.sleep(500);
                    System.out.println(Thread.currentThread().getName() +" DONE " +id);
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

        System.out.println(Thread.currentThread().getName() + "Done");
    }

}
