package com.kt.lekcje.tydzien3;

import java.util.concurrent.atomic.AtomicLong;

class VolatileLesson {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting...");

        Counter counter = new Counter();
        Thread t0 = new Thread(counter);

        t0.start();

        Thread.sleep(1_500);

        counter.stopPlease();

        System.out.println("Done");
    }

    static class Counter implements Runnable {
        AtomicLong value = new AtomicLong(0);
        volatile boolean isRunning = true;

        public long increment() {
            value.incrementAndGet();
            return value.get();
        }

        @Override
        public void run() {
            try {
                while (isRunning) {
                    long current = increment();
                    System.out.println("Incremented a counter to: " + current);
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                throw new IllegalStateException();
            }
        }

        public void stopPlease() {
            System.out.println("Setting isRunning to false");
            isRunning = false;
        }
    }
}
