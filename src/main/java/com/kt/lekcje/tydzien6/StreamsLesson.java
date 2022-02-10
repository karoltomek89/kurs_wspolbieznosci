package com.kt.lekcje.tydzien6;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class StreamsLesson {
    public static void main(String[] args) throws InterruptedException, ExecutionException {

        print("Starting...");

        Stream<Integer> numbers = Stream.of(4, 12, 42, 0, 9, 87, 11, 34, 57, 17);

        ForkJoinPool pool = new ForkJoinPool(16);

        Instant now = Instant.now();
//        numbers
//                .parallel()
//                .sequential()
//                .map(StreamsLesson::doubleIt)
//                .forEach(StreamsLesson::print);

        pool.submit(()->
                    numbers
                .parallel()
                .map(StreamsLesson::doubleIt)
                .forEach(StreamsLesson::print)
        ).get();
        Instant end = Instant.now();

        Duration duration = Duration.between(now, end);
        print("Took: " + duration.toMillis() + " ms ");
        print("DONE");

    }

    static Integer doubleIt(int value) {
        try {
            print("Doubling value: " + value);
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new IllegalStateException();
        }

        return value * 2;
    }

    static void print(Object output) {
        System.out.printf("%s: %s - %s%n", LocalDateTime.now(), Thread.currentThread().getName(), output);
    }
}

