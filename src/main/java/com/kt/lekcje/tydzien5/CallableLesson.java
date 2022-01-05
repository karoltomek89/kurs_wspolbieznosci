package com.kt.lekcje.tydzien5;

import java.util.concurrent.*;

class CallableLesson {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println(Thread.currentThread().getName() + " Starting...");

        Callable<Long> task = () -> {
            System.out.println(Thread.currentThread().getName() + " Doing hard work...");
            Thread.sleep(500);
            return 123L;
        };

        ExecutorService executor = Executors.newFixedThreadPool(4);

        final Future<Long> future = executor.submit(task);

        Long result = future.get();

        System.out.println("Computation result is: " + result);

        System.out.println("DONE");
    }

}
