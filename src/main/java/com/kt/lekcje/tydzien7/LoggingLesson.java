package com.kt.lekcje.tydzien7;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class LoggingLesson {

    private static final Logger logger = LoggerFactory.getLogger(LoggingLesson.class);

    public static void main(String[] args) {
        logger.info("Starting...");

        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    logger.info("Starting hard work...");
                    Thread.sleep(100);
                    logger.info("DONE");
                } catch (InterruptedException e) {
                    throw new IllegalStateException();
                }
            }
        };

        Thread t0 = new Thread(task, "heavy-load-thread");
        t0.start();

        logger.info("DONE");
    }

}
