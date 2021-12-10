package com.kt.lekcje.tydzien3;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

class LockLesson3 {
    LockLesson3() {
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting..");
        final LockLesson3.Inventory inventory = new LockLesson3.Inventory();
        Runnable writeTask = new Runnable() {
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
        Runnable readTask = new Runnable() {
            public void run() {
                for(int i = 0; i < 10; ++i) {
                    Integer quantity = inventory.howMany("pomidory");
                    System.out.println("There is " + quantity + " of pomidors");
                }

            }
        };
        Thread t0 = new Thread(writeTask);
        Thread t1 = new Thread(readTask);
        Thread t2 = new Thread(readTask);
        Thread t3 = new Thread(readTask);
        t0.start();
        t1.start();
        t2.start();
        t3.start();
        t0.join();
        t1.join();
        t2.join();
        t3.join();
        System.out.println("There is: " + inventory.howMany("pomidor") + " of pomidor in invertory");
        System.out.println("Done");
    }

    static class Inventory {
        Map<String, Integer> state = new HashMap();
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

        Inventory() {
        }

        public void put(String item, Integer quantity) throws InterruptedException {
            WriteLock writeLock = this.lock.writeLock();
            boolean tryLock = writeLock.tryLock(100L, TimeUnit.MILLISECONDS);
            if (tryLock) {
                try {
                    System.out.println(Thread.currentThread().getName() + " Got lock...");
                    Thread.sleep(100L);
                    PrintStream var10000 = System.out;
                    String var10001 = Thread.currentThread().getName();
                    var10000.println(var10001 + " There is: " + this.howMany("pomidor") + " of pomidor in invertory");
                    this.state.put(item, quantity);
                } finally {
                    writeLock.unlock();
                }
            } else {
                System.out.println(Thread.currentThread().getName() + " Unable to get lock...");
            }

        }

        public Integer howMany(String item) {
            ReadLock readLock = this.lock.readLock();
            if (readLock.tryLock()) {
                Integer var4;
                try {
                    System.out.println(Thread.currentThread().getName() + " READ-LOCK obtained...");
                    Thread.sleep(100L);
                    Integer quantity = (Integer)this.state.get(item);
                    var4 = quantity;
                } catch (InterruptedException var8) {
                    var8.printStackTrace();
                    return -1;
                } finally {
                    System.out.println(Thread.currentThread().getName() + " READ-LOCK released...");
                    readLock.unlock();
                }

                return var4;
            } else {
                return -1;
            }
        }
    }
}

