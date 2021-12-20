package com.kt.lekcje.tydzien4;

import java.time.LocalDateTime;
import java.util.concurrent.*;

class ThreadPoolLesson {

    public static void main(String[] args) {
        System.out.println("Starting...");

        //ExecutorService executor = Executors.newFixedThreadPool(4);
//        ExecutorService executor = Executors.newSingleThreadExecutor();
        //ExecutorService executor = Executors.newCachedThreadPool();

        //ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(4);

        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(Thread.currentThread().getName() + LocalDateTime.now() + " Doing hard work...");
                    Thread.sleep(500);
                    System.out.println("Job done, go home");
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

//        scheduledExecutor.schedule(task,500, TimeUnit.MILLISECONDS);
 //     scheduledExecutor.scheduleAtFixedRate(task,500,400, TimeUnit.MILLISECONDS);
        scheduledExecutor.scheduleWithFixedDelay(task,500,400, TimeUnit.MILLISECONDS);


        System.out.println("Done");
    }

}
