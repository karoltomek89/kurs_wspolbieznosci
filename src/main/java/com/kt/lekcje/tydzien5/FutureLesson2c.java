package com.kt.lekcje.tydzien5;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

class FutureLesson2c {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        System.out.println(Thread.currentThread().getName() + " Starting...");

        ExecutorService es = Executors.newFixedThreadPool(4);

        MyTask myTask3 = new MyTask(9);
        MyTask myTask4 = new MyTask(10);
        MyTask myTask5 = new MyTask(11);

        final Long result = es.invokeAny(Arrays.asList(myTask3, myTask4, myTask5));

        System.out.println(Thread.currentThread().getName() + " First computed result is: " + result);

        final List<Future<Long>> results = es.invokeAll(Arrays.asList(myTask3, myTask4, myTask5));

        for (Future<Long> future : results) {
            System.out.println(Thread.currentThread().getName() + " Result is: " + future.get());
        }

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
