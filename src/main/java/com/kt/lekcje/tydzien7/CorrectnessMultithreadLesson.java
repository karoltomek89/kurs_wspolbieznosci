package com.kt.lekcje.tydzien7;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class CorrectnessMultithreadLesson {

    private static final Logger logger = LoggerFactory.getLogger(CorrectnessMultithreadLesson.class);

    public static void main(String[] args) throws InterruptedException {

        Store store = new Store();

        Set<Integer> ids = new HashSet<>();

        ExecutorService executorService = Executors.newFixedThreadPool(100);

        CountDownLatch countDownLatch = new CountDownLatch(1);

        Runnable task = () -> {
            try {
                countDownLatch.await();
                store.add("product");
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        };

        int items = 100;
        for (int i = 0; i < items; i++) {
            executorService.execute(task);
        }

        countDownLatch.countDown();
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);

        logger.info("Store items: {}, expected: {}", store.size(), items);
    }

    static class Store {
        private Map<Integer, String> inventory = new ConcurrentHashMap<>();

        public synchronized Integer add(String name) {
            int id = inventory.size();
            inventory.put(id, name);
            return id;
        }

        public String getById(Integer id) {
            return inventory.get(id);
        }

        public Integer size() {
            return inventory.size();
        }
    }
}
