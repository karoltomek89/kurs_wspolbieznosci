package com.kt.lekcje.tydzien6;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class ListLesson {
    public static void main(String[] args) throws InterruptedException {

        print("Starting...");

        Random random = new Random();

//        List<Integer> list = IntStream.range(0, 10).boxed().collect(Collectors.toList());
//        List<Integer> list = Collections.synchronizedList(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
//        List<Integer> list = Collections.unmodifiableList(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
        List<Integer> list = new CopyOnWriteArrayList<>(new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9});


        print(list);

        Runnable reader = new Runnable() {
            @Override
            public void run() {
                try {
//                    for (Integer integer : list) {
//                        print("Read item from list: " + integer);
//                        Thread.sleep(100);
                    final Iterator<Integer> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        Integer integer = iterator.next();
                        print("Read item from list: " + integer);
                        Thread.sleep(100);
                    }
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }

            }
        };

        Runnable writer = new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 10; ++i) {
                        list.add(i, random.nextInt(20));
                        Thread.sleep(100);
                    }
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
            }
        };

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        executorService.execute(reader);
        executorService.execute(writer);
        executorService.execute(reader);
        executorService.execute(writer);
        executorService.execute(reader);
        executorService.execute(writer);
        executorService.execute(reader);
        executorService.execute(writer);

        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);
        print(list);
        print("DONE");

    }

    static void print(Object output) {
        System.out.printf("%s: %s - %s%n", LocalDateTime.now(), Thread.currentThread().getName(), output);
    }
}

