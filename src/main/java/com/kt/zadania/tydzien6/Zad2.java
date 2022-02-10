package com.kt.zadania.tydzien6;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Zad2 {
    public static void main(String[] args) throws InterruptedException {

        print("Starting...");

        ExecutorService executorService = Executors.newFixedThreadPool(16);
        start(1,5,executorService,10);

        print("DONE");

    }

    static List<Integer> numbers(int from, int to) {
        return IntStream.range(from, to).boxed().collect(Collectors.toList());
    }

    static void print(Object output) {
        System.out.printf("%s: %s - %s%n", LocalDateTime.now(), Thread.currentThread().getName(), output);
    }

    static void start(int numOfConsumers, int numOfProducers, ExecutorService executorService, int queueSize)
            throws InterruptedException {
        final Instant start = Instant.now();

        BlockingQueue<ConsumerGood> queue = new ArrayBlockingQueue<>(queueSize);

        Runnable producer = () -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Thread.sleep(500);
                    ConsumerGood consumerGood = new ConsumerGood();
                    print("ConsumerGood produced");
                    queue.put(consumerGood);
                    print("Queue size: " + queue.size());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        Runnable consumer = new Runnable() {
            @Override
            public void run() {
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        Thread.sleep(300);
                        queue.take();
                        print("Consuming good");
                        print("Queue size: " + queue.size());
                    }
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                }
            }
        };


        for (int i = 0; i < numOfConsumers; i++) {
            executorService.execute(consumer);
        }
        for (int i = 0; i < numOfProducers; i++) {
            executorService.execute(producer);
        }

        Thread.sleep(30_000);
        executorService.shutdownNow();
    }

    static class ConsumerGood {
    }
}


