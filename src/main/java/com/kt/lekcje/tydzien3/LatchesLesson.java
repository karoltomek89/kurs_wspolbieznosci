package com.kt.lekcje.tydzien3;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

class LatchesLesson {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting...");

        //CountDownLatch latch = new CountDownLatch(1);
        CountDownLatch latch = new CountDownLatch(3);
        Runnable task = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " Starting run...");
                try {
                    latch.countDown();
                    System.out.println(Thread.currentThread().getName() + " Waiting for other threads: " + latch.getCount());
                    latch.await(10, TimeUnit.MINUTES);
                    System.out.println(Thread.currentThread().getName() + " Working hard...");
                    Thread.sleep(1_000);
                    System.out.println(Thread.currentThread().getName() + " Finishing...");
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
            }
        };

        System.out.println("Done.");

        Thread t0 = new Thread(task);
        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t0.start();
        Thread.sleep(1_000);
        t1.start();
        Thread.sleep(1_000);
        t2.start();

        //latch.countDown();

        System.out.println("All threads started");

    }


}
