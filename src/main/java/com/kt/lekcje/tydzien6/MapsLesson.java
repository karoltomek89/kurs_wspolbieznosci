package com.kt.lekcje.tydzien6;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class MapsLesson {
    public static void main(String[] args) throws InterruptedException {

        print("Starting...");

        Map<String, Integer> map = new ConcurrentHashMap<>();
        map.put("test", 0);

        Map<String, Integer> map2 = Collections.synchronizedMap(map);

        Map<String, Integer> map3 = new ConcurrentHashMap<>();


        Runnable task = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i< 100_000; ++i) {
                    map.computeIfPresent("test", (key, value) -> value + 1);
                }
            }
        };

        Runnable task2 = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i< 100_000; ++i) {
                    map2.put(String.valueOf(map2.size()),map2.size());
                }
            }
        };

        Runnable task3 = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i< 100_000; ++i) {
                    map3.put(String.valueOf(map3.size()),map3.size());
                }
            }
        };

        ExecutorService service = Executors.newFixedThreadPool(4);
        service.submit(task);
        service.submit(task);
        service.submit(task);
        service.submit(task);
        service.submit(task2);
        service.submit(task2);
        service.submit(task2);
        service.submit(task2);
        service.submit(task3);
        service.submit(task3);
        service.submit(task3);
        service.submit(task3);

        service.shutdown();
        service.awaitTermination(10, TimeUnit.SECONDS);

        print("Result is: " + map.get("test"));
        print("Result is: " + map2.size());
        print("Result is: " + map3.size());

        print("DONE");

    }

    static void print(String output) {
        System.out.println(String.format("%s: %s - %s", LocalDateTime.now(), Thread.currentThread().getName(), output));
    }
}
