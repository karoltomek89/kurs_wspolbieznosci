package com.kt.zadania.tydzien4;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

class Zad2 {

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + " Starting...");

        ForkJoinPool joinPool = ForkJoinPool.commonPool();

        String numberToCompute = "0901800";

        Integer sum = joinPool.invoke(new Zad2.CountingTask(numberToCompute));

        System.out.println("Sum of digits for " + numberToCompute + " is: " + sum);

        System.out.println(Thread.currentThread().getName() + " Done");
    }

    static class CountingTask extends RecursiveTask<Integer> {

        String number;

        CountingTask(String number) {
            this.number = number;
            System.out.println("Created counting task for Sring: " + number);
        }

        @Override
        protected Integer compute() {
            System.out.println(Thread.currentThread().getName() + " Computing sum of digits for String: " + number);
            if (number.length() == 1) {
                System.out.println("Only 1 digit, return its value: " + number);
                return Integer.valueOf(number);
            } else {
                List<Zad2.CountingTask> tasks = new ArrayList<>();
                System.out.println("Longer String, creating two additional counting tasks for string: " + number);
                tasks.add((new Zad2.CountingTask(number.substring(0, number.length() / 2))));
                tasks.add((new Zad2.CountingTask(number.substring(number.length() / 2))));
                for (Zad2.CountingTask task : tasks) {
                    task.fork();
                }
                int sum = 0;
                for (Zad2.CountingTask task : tasks) {
                    sum += task.join();
                }
                return sum;
            }
        }
    }
}
