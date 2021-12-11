package com.kt.lekcje.tydzien3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

class SemaphorLesson {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting..");
        Restaurant restaurant = new Restaurant(5);

        Runnable task = new Runnable() {
            @Override
            public void run() {
                try{
                    Person person = new Person();
                    boolean success = restaurant.stepInto(person);
                    if (success){
                        System.out.println(Thread.currentThread().getName() + " I enjoy this place");
                        Thread.sleep(1_000);
                        restaurant.leave(person);
                    }else {
                        System.out.println(Thread.currentThread().getName() + " no more seats :(");
                    }
                }catch (InterruptedException e){
                    throw new IllegalStateException(e);
                }
            }
        };

        Thread t0 = new Thread(task, "Person1");
        Thread t1 = new Thread(task, "Person2");
        Thread t2 = new Thread(task, "Person3");
        Thread t3 = new Thread(task, "Person4");
        Thread t4 = new Thread(task, "Person5");
        Thread t5 = new Thread(task, "Person6");
        Thread t6 = new Thread(task, "Person7");
        Thread t7 = new Thread(task, "Person8");
        Thread t8 = new Thread(task, "Person9");
        Thread t9 = new Thread(task, "Person10");

        t0.start();
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();
        t7.start();
        t8.start();
        t9.start();

        t0.join();
        t1.join();
        t2.join();
        t3.join();
        t4.join();
        t5.join();
        t6.join();
        t7.join();
        t8.join();
        t9.join();

        System.out.println("Done");
    }

    static class Person {

    }

    static class Restaurant {
        int seats;
        List<Person> personList = new ArrayList<>();
        Semaphore semaphore;

        public Restaurant(int seats) {
            this.seats = seats;
            semaphore = new Semaphore(seats);
        }

        public boolean stepInto(Person person) {
            System.out.println(Thread.currentThread().getName() + " stepping into restaurant");
            boolean accquired = semaphore.tryAcquire();
            if (accquired) {
                System.out.println(Thread.currentThread().getName() + " Eating...");
                personList.add(person);
            }else{
                System.out.println("There are already " + personList.size());
            }
            return accquired;
        }

        public void leave(Person person) {
            if (personList.remove(person)) {
                semaphore.release();
            }
        }
    }

}
