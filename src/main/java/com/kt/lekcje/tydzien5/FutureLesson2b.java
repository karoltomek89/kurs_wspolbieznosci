package com.kt.lekcje.tydzien5;

import java.util.concurrent.*;

class FutureLesson2b {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        System.out.println(Thread.currentThread().getName() + " Starting...");

        ExecutorService es = Executors.newFixedThreadPool(4);

        Future<Long> future1 = es.submit(new MyTask(10));
        Future<Long> future2 = es.submit(new MyTask(-4));

        var value1 = future1.get();
        System.out.println("Value1: " + value1);
        var value2 = future2.get();
        System.out.println("Value2: " + value2);

        System.out.println(Thread.currentThread().getName() + " DONE");
    }

    static class MyTask implements Callable<Long> {
        long value;

        MyTask(long value) {
            this.value = value;
        }

        @Override
        public Long call() throws Exception {
            if (value <= 0) {
                throw new IllegalArgumentException("Value cannot be negative or zero");
            } else {
                System.out.println(Thread.currentThread().getName() + " Starting hard work...");
                Thread.sleep(500);
                System.out.println(Thread.currentThread().getName() + " Done, returning result...");
                return value * value;
            }
        }
    }
}
