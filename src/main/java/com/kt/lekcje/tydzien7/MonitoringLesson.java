package com.kt.lekcje.tydzien7;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

class MonitoringLesson {

    private static final Logger logger = LoggerFactory.getLogger(MonitoringLesson.class);
    private static Random random = new Random();

    public static void main(String[] args) throws InterruptedException {
        logger.info("Starting...");

        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(10);

        MetricRegistry metricRegistry = new MetricRegistry();
        metricRegistry.register("executor.quene.lenght", new Gauge<Integer>() {

            @Override
            public Integer getValue() {
                return workQueue.size();
            }
        });

        ConsoleReporter consoleReporter = ConsoleReporter.forRegistry(metricRegistry).build();
        consoleReporter.start(1, TimeUnit.SECONDS);

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                1, 2, 10, TimeUnit.MILLISECONDS, workQueue);

        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    logger.info("Starting doing hard work...");
                    Thread.sleep(5_000);
                    logger.info("DONE");
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
            }
        };
        for (int i = 0; i < 100; i++) {
            executor.execute(task);
        }
        logger.info("DONE");
    }
}
