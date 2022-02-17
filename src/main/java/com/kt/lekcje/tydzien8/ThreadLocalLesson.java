package com.kt.lekcje.tydzien8;

import org.slf4j.Logger;
import org.slf4j.MDC;
import org.slf4j.LoggerFactory;
import java.util.Random;

class ThreadLocalLesson {

    private static final Logger logger = LoggerFactory.getLogger(ThreadLocalLesson.class);
    private static Random random = new Random();


    public static void main(String[] args) throws InterruptedException {
        Computation computation = new Computation();
        Thread t1 = new Thread(computation);
        Thread t2 = new Thread(computation);

        t1.start();
        t2.start();

        t1.join();
        t1.join();

        logger.info("DONE");
    }

    static class Computation implements Runnable {
        private ThreadLocal<Integer> localNumber = new ThreadLocal<>();

        @Override
        public void run() {
            final int value = random.nextInt(1_000);
            MDC.put("number", String.valueOf(value));

            logger.info("Setting local number as: {}", value);
            localNumber.set(value);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            logger.info("Local number is: {}", localNumber.get());
            MDC.remove("number");
        }
    }
}
