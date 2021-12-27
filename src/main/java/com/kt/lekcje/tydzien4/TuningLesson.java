package com.kt.lekcje.tydzien4;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

class TuningLesson {

    public static void main(String[] args) throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " Starting...");

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                4,
                4,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(4),
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        System.out.println(Thread.currentThread().getName() + " I am full, sory bro");
                    }
                }

        );

        ThreadPoolExecutor cachedExecutor = new ThreadPoolExecutor(
                0,
                4,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(4),
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        System.out.println(Thread.currentThread().getName() + " I am full, sory bro");
                    }
                }
        );

        Executor daemonExecutor = Executors.newFixedThreadPool(4, new ThreadFactory() {
            AtomicInteger id = new AtomicInteger();

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r,"My daemon thread- " + id.incrementAndGet());
                thread.setDaemon(true);
                return thread;
            }
        });

        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(Thread.currentThread().getName() + " Doing hard work...");
                    Thread.sleep(200);
                    System.out.println(Thread.currentThread().getName() + " Done");
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
            }
        };

//        executor.execute(task);
//        executor.execute(task);
//        executor.execute(task);
//        executor.execute(task);
//        executor.execute(task);
//        executor.execute(task);
//        executor.execute(task);
//        executor.execute(task);
//        executor.execute(task);
//        executor.execute(task);
//        executor.execute(task);
//        executor.execute(task);

//        cachedExecutor.execute(task);
//        cachedExecutor.execute(task);
//        cachedExecutor.execute(task);
//        cachedExecutor.execute(task);
//        cachedExecutor.execute(task);
//        cachedExecutor.execute(task);
//        cachedExecutor.execute(task);
//        cachedExecutor.execute(task);
//        cachedExecutor.execute(task);
//        cachedExecutor.execute(task);
//        cachedExecutor.execute(task);
//        cachedExecutor.execute(task);

        daemonExecutor.execute(task);
        daemonExecutor.execute(task);
        daemonExecutor.execute(task);
        daemonExecutor.execute(task);
        Thread.sleep(2000);
        daemonExecutor.execute(task);
        daemonExecutor.execute(task);
        daemonExecutor.execute(task);
        daemonExecutor.execute(task);
        daemonExecutor.execute(task);
        daemonExecutor.execute(task);
        daemonExecutor.execute(task);
        daemonExecutor.execute(task);

        System.out.println(Thread.currentThread().getName() + " Done");
    }

}
