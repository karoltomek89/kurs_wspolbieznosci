package com.kt.lekcje.tydzien6;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class FancyQueuesLesson {
    public static void main(String[] args) throws InterruptedException {

        Random random = new Random();

        print("Starting...");

//        SynchronousQueue<Integer> queue = new SynchronousQueue<>();
//        TransferQueue<Integer> queue = new LinkedTransferQueue<>();
        Exchanger<Integer> exchanger = new Exchanger<>();

//        Runnable writer = new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    for (int i = 0; i < 10; i++) {
//                        print("Putting value to quene");
//                        queue.put(random.nextInt(100));
//                        print("OK");
//                        Thread.sleep(500);
//                    }
//
//                } catch (InterruptedException e) {
//                    throw new IllegalStateException(e);
//                }
//            }
//        };

//        Runnable reader = new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    for (int i = 0; i < 10; i++) {
//                        print("Reading value from queue");
//                        final Integer take = queue.take();
//                        print("Taken value is: " + take);
//                    }
//                } catch (
//                        InterruptedException e) {
//                    throw new IllegalStateException(e);
//                }
//            }
//        };

        Runnable writer = new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 10; i++) {
                        final int value = random.nextInt(100);
                        print("Putting value to exchanger " + value);
                        int newValue = exchanger.exchange(value);
                        print("Received value is: " + newValue);
                    }

                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
            }
        };

        ExecutorService service = Executors.newFixedThreadPool(4);
        service.execute(writer);
        service.execute(writer);
//        service.execute(reader);
//        service.execute(reader);

        print("DONE");

    }

    static List<Integer> numbers(int from, int to) {
        return IntStream.range(from, to).boxed().collect(Collectors.toList());
    }

    static void print(Object output) {
        System.out.printf("%s: %s - %s%n", LocalDateTime.now(), Thread.currentThread().getName(), output);
    }
}

