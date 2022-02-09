package com.kt.lekcje.tydzien6;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class QueueLesson {
    public static void main(String[] args) throws InterruptedException {

        print("Starting...");

        Random random = new Random();

        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(1);

        Runnable producer = () -> {
            try {
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(500);
                    int number = random.nextInt(100);
                    print("Adding number: " + number);
                    queue.put(number);
                }
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }

        };

        Runnable consumer = new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 100; i++) {
                        int number = queue.take();
                        print("Read number: " + number);
                    }
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            }
        };

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        executorService.execute(producer);
        executorService.execute(producer);
        executorService.execute(consumer);

        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);
        print("DONE");

    }

    static List<Integer> numbers(int from, int to) {
        return IntStream.range(from, to).boxed().collect(Collectors.toList());
    }

    static void print(Object output) {
        System.out.printf("%s: %s - %s%n", LocalDateTime.now(), Thread.currentThread().getName(), output);
    }
}

