package com.kt.lekcje.tydzien7;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

class PerformanceLesson {

    private static final Logger logger = LoggerFactory.getLogger(PerformanceLesson.class);
    private static Random random = new Random();

    public static void main(String[] args) throws InterruptedException {
        logger.info("Starting...");

        Map<Integer, String> synchronizedMap = Collections.synchronizedMap(new HashMap<>());
        Map<Integer, String> concurrentMap = new ConcurrentHashMap<>();

        int loops = 1_000;
        int threads = 100;
        int repeats = 20;

        logger.info("Starting warmup...");

        Duration benchmark1 = benchmark(synchronizedMap, loops, threads);
        Duration benchmark2 = benchmark(concurrentMap, loops, threads);

        LongAdder synchronizedAdder = new LongAdder();
        LongAdder concurrentAdder = new LongAdder();

        logger.info("Starting benchmark...");
        for (int i = 0; i < repeats; i++) {
            synchronizedAdder.add(benchmark(synchronizedMap, loops, threads).toMillis());
            concurrentAdder.add(benchmark(concurrentMap, loops, threads).toMillis());
        }
//        Duration benchmark1 = benchmark(synchronizedMap, loops, threads);
//        Duration benchmark2 = benchmark(concurrentMap, loops, threads);

//        logger.info("Synchronized map: {} " + benchmark1.toMillis());
//        logger.info("Concurrent map: {} " + benchmark2.toMillis());

        logger.info("Synchronized map: {} " + synchronizedAdder.doubleValue() / repeats);
        logger.info("Concurrent map: {} " + concurrentAdder.doubleValue() / repeats);

        logger.info("DONE");
    }

    static Duration benchmark(Map<Integer, String> map, int loops, int threads) throws InterruptedException {
        Instant start = Instant.now();
        ExecutorService executorService = Executors.newFixedThreadPool(threads);
        for (int i = 0; i < threads; i++) {
            executorService.execute(new PerformanceTask(map, loops));
        }
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);
        Instant end = Instant.now();
        return Duration.between(start, end);
    }

    static class PerformanceTask implements Runnable {
        private Map<Integer, String> map;
        private int loop;

        PerformanceTask(Map<Integer, String> map, int loop) {
            this.map = map;
            this.loop = loop;
        }

        @Override
        public void run() {
            for (int i = 0; i < loop; i++) {
                map.compute(random.nextInt(), (k, v) -> k.toString());
            }
        }
    }
}
