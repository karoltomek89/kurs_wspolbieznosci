package com.kt.lekcje.tydzien4;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class WorkStealingPool {

    public static void main(String[] args) throws InterruptedException {

        System.out.println(Thread.currentThread().getName() + "Startning...");

        final ExecutorService stealingPool = Executors.newWorkStealingPool();

        AtomicInteger counter = new AtomicInteger();
        stealingPool.execute(new Task(1_000, counter.getAndIncrement()));
        stealingPool.execute(new Task(100, counter.getAndIncrement()));
        stealingPool.execute(new Task(1_000, counter.getAndIncrement()));
        stealingPool.execute(new Task(100, counter.getAndIncrement()));
        stealingPool.execute(new Task(1_000, counter.getAndIncrement()));
        stealingPool.execute(new Task(100, counter.getAndIncrement()));
        stealingPool.execute(new Task(1_000, counter.getAndIncrement()));
        stealingPool.execute(new Task(100, counter.getAndIncrement()));
        stealingPool.execute(new Task(1_000, counter.getAndIncrement()));
        stealingPool.execute(new Task(100, counter.getAndIncrement()));
        stealingPool.execute(new Task(1_000, counter.getAndIncrement()));

        stealingPool.awaitTermination(30, TimeUnit.SECONDS);

        System.out.println(Thread.currentThread().getName() + " Done");
    }

    static class Task implements Runnable {
        int sleep;
        int id;

        Task(int sleep, int id) {
            this.sleep = sleep;
            this.id = id;
        }

        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() +  " " + id + " " + " Doing work for " + sleep + " ms..");
                Thread.sleep(sleep);
                System.out.println(Thread.currentThread().getName() +  " " + id + " " + " Done");
            } catch (InterruptedException e) {
                throw new IllegalStateException();
            }
        }
    }
}
