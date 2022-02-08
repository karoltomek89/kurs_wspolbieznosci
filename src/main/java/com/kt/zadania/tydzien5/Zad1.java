package com.kt.zadania.tydzien5;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

class Zad1 {
    public static void main(String[] args) {

        while (true) {
            System.out.println('\n' + Thread.currentThread().getName() + " Starting...");

            CompletableFuture<String> klockiAsync = new AsyncTask("klocki").fetch();
            CompletableFuture<String> tasmaAsync = new AsyncTask("tasma").fetch();
            CompletableFuture<String> papierAsync = new AsyncTask("papier").fetch();

            if (!klockiAsync.isCompletedExceptionally() && !tasmaAsync.isCompletedExceptionally() && !papierAsync.isCompletedExceptionally()) {

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

                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }

                System.out.println(Thread.currentThread().getName() + " DONE");
            } else {
                System.out.println(Thread.currentThread().getName() + " Errors have occurred, order cancelled :(");

            }
        }
    }

    static class AsyncTask {
        private String result;
        Random random = new Random();

        AsyncTask(String result) {
            this.result = result;
        }

        public CompletableFuture<String> fetch(){

            int i = random.nextInt(3);

            if (i > 0) {
                return CompletableFuture.supplyAsync(() -> {
                    try {
                        System.out.println(Thread.currentThread().getName() + " Preparing result: " + result + "...");
                        Thread.sleep(3000);
                        System.out.println(Thread.currentThread().getName() + " DONE " + result);
                        return result;
                    } catch (InterruptedException e) {
                        System.out.println(Thread.currentThread().getName() + " FETCH unknow error! ");
                        return result;
                    }
                });
            } else {
                System.out.println(Thread.currentThread().getName() + " FETCH error: " + result + " quantity is: " + i);
                return CompletableFuture.failedFuture(new IllegalStateException());
            }
        }
    }
}