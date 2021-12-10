package com.kt.lekcje;

class DeadLockLesson {
    static Object lock1 = new Object();
    static Object lock2 = new Object();

    DeadLockLesson() {
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting..");
        Thread t0 = new Thread(new Runnable() {
            public void run() {
                try {
                    (new DeadLockLesson.Task()).doTheJob();
                } catch (InterruptedException var2) {
                    var2.printStackTrace();
                }

            }
        });
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                try {
                    (new DeadLockLesson.OtherTask()).doTheJob();
                } catch (InterruptedException var2) {
                    var2.printStackTrace();
                }

            }
        });
        t0.start();
        t1.start();
        t0.join();
        t1.join();
        System.out.println("DONE");
    }

    static class OtherTask {
        OtherTask() {
        }

        public void doTheJob() throws InterruptedException {
            System.out.println(Thread.currentThread().getName() + " OtherTask trying to acquire a lock1");
            synchronized(DeadLockLesson.lock1) {
                System.out.println(Thread.currentThread().getName() + " Holding lock1");
                Thread.sleep(100L);
                synchronized(DeadLockLesson.lock1) {
                    System.out.println(Thread.currentThread().getName() + " Holding lock1");
                    Thread.sleep(1000L);
                    System.out.println(Thread.currentThread().getName() + " Releasing lock1");
                }
            }

            System.out.println(Thread.currentThread().getName() + " Releasing lock1");
        }
    }

    static class Task {
        Task() {
        }

        public void doTheJob() throws InterruptedException {
            System.out.println(Thread.currentThread().getName() + " Task trying to acquire a lock1");
            synchronized(DeadLockLesson.lock1) {
                System.out.println(Thread.currentThread().getName() + " Holding lock1");
                Thread.sleep(100L);
                synchronized(DeadLockLesson.lock2) {
                    System.out.println(Thread.currentThread().getName() + " Holding lock2");
                    Thread.sleep(1000L);
                    System.out.println(Thread.currentThread().getName() + " Releasing lock2");
                }
            }

            System.out.println(Thread.currentThread().getName() + " Releasing lock1");
        }
    }
}