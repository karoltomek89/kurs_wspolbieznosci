package com.kt.lekcje.tydzien5;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

class CompletableFutureLesson6 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println(Thread.currentThread().getName() + " Starting...");

        CompletableFuture<String> klockiAsync = new AsyncTask("klocki").fetch();
        CompletableFuture<String> tasmaAsync = new AsyncTask("tasma").fetch();
        CompletableFuture<String> papierAsync = new AsyncTask("papier").fetch();

        final CompletableFuture<List<String>> future = klockiAsync.thenCombine(tasmaAsync, (klocki, tasma) -> {
            List<String> list = new ArrayList<>();
            list.add(klocki);
            list.add(tasma);
            return list;
        }).thenCombine(papierAsync, (list, papier) -> {
            list.add(papier);
            return list;
        }).whenComplete((list, error) -> {
            if (error != null) {
                System.err.println(Thread.currentThread().getName() + " Something went wrong. Unable to proceed with your order.");
            } else {
                System.out.println(Thread.currentThread().getName() + " Parcel: " + String.join(", ", list));
            }
        });

        future.get();

        System.out.println(Thread.currentThread().getName() + " DONE");


    }

    static class AsyncTask {
        private String result;

        AsyncTask(String result) {
            this.result = result;
        }

        public CompletableFuture<String> fetch() {
            return CompletableFuture.supplyAsync(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + " Preparing result: " + result + "...");
                    Thread.sleep(500);
                    System.out.println(Thread.currentThread().getName() + " DONE " + result);
                    return result;
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
            });
        }
    }
}