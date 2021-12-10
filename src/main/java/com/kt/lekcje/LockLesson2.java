package com.kt.lekcje;


import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class LockLesson2 {
    LockLesson2() {
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting..");
        final LockLesson2.Inventory inventory = new LockLesson2.Inventory();
        Runnable task = new Runnable() {
            public void run() {
                for(int i = 0; i < 10; ++i) {
                    try {
                        inventory.put("pomidor", i);
                    } catch (InterruptedException var3) {
                        throw new IllegalStateException();
                    }
                }

            }
        };
        Thread t0 = new Thread(task);
        Thread t1 = new Thread(task);
        t0.start();
        t1.start();
        t0.join();
        t1.join();
        System.out.println("There is: " + inventory.howMany("pomidor") + " of pomidor in invertory");
        System.out.println("Done");
    }

    static class Inventory {
        Map<String, Integer> state = new HashMap();
        Lock lock = new ReentrantLock();

        Inventory() {
        }

        public void put(String item, Integer quantity) throws InterruptedException {
            boolean tryLock = this.lock.tryLock(100L, TimeUnit.MILLISECONDS);
            if (tryLock) {
                try {
                    System.out.println(Thread.currentThread().getName() + " Got lock...");
                    Thread.sleep(100L);
                    PrintStream var10000 = System.out;
                    String var10001 = Thread.currentThread().getName();
                    var10000.println(var10001 + " There is: " + this.howMany("pomidor") + " of pomidor in invertory");
                    this.state.put(item, quantity);
                } finally {
                    this.lock.unlock();
                }
            } else {
                System.out.println(Thread.currentThread().getName() + " Unable to get lock...");
            }

        }

        public Integer howMany(String item) {
            if (this.lock.tryLock()) {
                Integer var3;
                try {
                    Integer quantity = (Integer)this.state.get(item);
                    var3 = quantity;
                } finally {
                    this.lock.unlock();
                }

                return var3;
            } else {
                return -1;
            }
        }
    }
}
