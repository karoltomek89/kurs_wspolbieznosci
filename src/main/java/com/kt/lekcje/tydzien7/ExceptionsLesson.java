package com.kt.lekcje.tydzien7;


import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

class ExceptionsLesson {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionsLesson.class);

    public static void main(String[] args) throws InterruptedException {
        logger.info("Starting...");

        Runnable task = new Runnable() {
            @Override
            public void run() {
//                try {
                logger.info("Doing hard work...");
//                    Thread.sleep(500);
                throw new RuntimeException("Ops...");
//                } catch (InterruptedException e) {
//                    logger.error("Got interrupted exception", e);
//                } catch (RuntimeException e) {
//                    logger.error("Got runtime exception", e);
//                } catch (Exception e) {
//                    logger.error("Got exception", e);
//                }
            }
        };

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                logger.error("Got uncaught exception from thread: {}, error {}", t.getName(), e.getMessage(), e);
            }
        });

//        Thread t0 = new Thread(task);

//        t0.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
//            @Override
//            public void uncaughtException(Thread t, Throwable e) {
//                logger.error("Custom exception handler", e);
//            }
//        });
//
//        t0.start();

        ExecutorService executorService = Executors.newFixedThreadPool(4, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t0 = new Thread(r);
                t0.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException(Thread t, Throwable e) {
                        logger.error("Custom exception handler", e);
                    }
                });
                return t0;
            }
        });

//        executorService.execute(task);


        Callable<Long> compute = new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                logger.info("Computing big number...");
                Thread.sleep(500);
                throw new RuntimeException("Can't compute a value");
            }
        };

        ExecutorService executorService1 = Executors.newFixedThreadPool(4);
        final Future<Long> submit = executorService1.submit(compute);

        logger.info("Going to sleep...");
        Thread.sleep(2_000);
        logger.info("OK!");
        try {
            submit.get();
        } catch (ExecutionException e) {
            logger.error("Exception from future {}" , e.getMessage(),e);
        }

        logger.info("DONE");
    }
}
