package com.kt.lekcje.tydzien7;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class ErrorHandlingLesson {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionsLesson.class);

    public static void main(String[] args) throws InterruptedException {
        logger.info("Starting");

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    logger.info("Starting long task...");
                    Thread.sleep(1_000);
                    logger.info("DONE");
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
            }
        };

        ThreadPoolExecutor executor =
                (new ThreadPoolExecutor(
                        1,
                        1,
                        0L, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<Runnable>(1), new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        logger.error("Task can not be executed on thread pool: {}", executor);
                    }
                }));

        executor.execute(runnable);
        executor.execute(runnable);
        executor.execute(runnable);

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }

}
