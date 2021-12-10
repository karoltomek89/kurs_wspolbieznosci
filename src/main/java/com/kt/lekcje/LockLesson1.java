package com.kt.lekcje;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class LockLesson1 {
    LockLesson1() {
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting..");
        final LockLesson1.Counter counter = new LockLesson1.Counter();
        Runnable task = new Runnable() {
            public void run() {
                for(int i = 0; i < 100000; ++i) {
                    counter.increment();
                }

            }
        };
        Thread t0 = new Thread(task);
        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        t0.start();
        t1.start();
        t2.start();
        t0.join();
        t1.join();
        t2.join();
        System.out.println("Counter value is: " + counter.value);
        System.out.println("Done");
    }

    static class Counter {
        long value = 0L;
        Lock lock = new ReentrantLock();

        Counter() {
        }

        public void increment() {
            try {
                this.lock.lock();
                ++this.value;
            } finally {
                this.lock.unlock();
            }

        }
    }
}
