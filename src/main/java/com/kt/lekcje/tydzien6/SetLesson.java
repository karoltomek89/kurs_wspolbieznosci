package com.kt.lekcje.tydzien6;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class SetLesson {
    public static void main(String[] args) throws InterruptedException {

        print("Starting...");

        Set<Integer> set = Collections.newSetFromMap(new ConcurrentHashMap<>());
        //Set<Integer> set = ConcurrentHashMap.newKeySet();
        set.addAll(numbers(0,100));;
        //Set<Integer> set = new CopyOnWriteArraySet<>numbers(0, 1000));

        AtomicInteger removals = new AtomicInteger();

        Runnable remover = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    if (set.remove(i)) {
                        removals.incrementAndGet();
                    }
                }
            }
        };


        ExecutorService executorService = Executors.newFixedThreadPool(4);

        executorService.execute(remover);
        executorService.execute(remover);
        executorService.execute(remover);
        executorService.execute(remover);

        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);

        print("Removals: " + removals.get());

        print("DONE");

    }

    static List<Integer> numbers(int from, int to) {
        return IntStream.range(from, to).boxed().collect(Collectors.toList());
    }

    static void print(Object output) {
        System.out.printf("%s: %s - %s%n", LocalDateTime.now(), Thread.currentThread().getName(), output);
    }
}

