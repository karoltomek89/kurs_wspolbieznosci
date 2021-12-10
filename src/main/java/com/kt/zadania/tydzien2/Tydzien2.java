package com.kt.zadania.tydzien2;

import java.util.Random;

class Tydzien2 {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Program is starting...");
        Data data = new Data();
        Thread t1 = new Thread(new FirstWriteTask(data), "First_generator");
        Thread t2 = new Thread(new SecondWriteTask(data), "Second_generator");
        Thread t3 = new Thread(new ComputeTask(data), "Sum computer");
        Thread t4 = new Thread(new ThreadMonitor(data, t1, t2, t3));

        t4.setDaemon(true);
        t4.start();
        t3.start();
        t1.start();
        t2.start();

        System.out.println("Done");
        Thread.sleep(30000);
        t1.interrupt();
        t2.interrupt();
        t3.interrupt();
    }

    static class Data {

        private int randomNumber = 0;

        public synchronized int readRandomNumber() throws InterruptedException {
            while (randomNumber == 0) {
                wait();
            }
            System.out.println("Number was read");
            int tmp = randomNumber;
            randomNumber = 0;
            System.out.println("Number was set to 0.");
            notifyAll();
            Thread.sleep(1000);
            return tmp;
        }

        public synchronized void writeRandomNumber(int number) throws InterruptedException {
            while (randomNumber != 0) {
                wait();
            }
            randomNumber = number;
            System.out.println("Number was set.");
            Thread.sleep(1000);
            notifyAll();
        }
    }

    static class RandomGenerator {

        private Random random = new Random();

        public int generateFirstNumber() {
            int generatedNumber = random.nextInt(49) + 1;
            System.out.println(Thread.currentThread().getName() + " : " + "New number generated. " + "The number is: " + generatedNumber);
            return generatedNumber;
        }

        public int generateSecondNumber() {
            int generatedNumber = random.nextInt(499) + 1;
            System.out.println(Thread.currentThread().getName() + " : " + "New number generated. " + "The number is: " +generatedNumber);
            return generatedNumber;
        }
    }

    static class Calculator {

        public static int calculateSum(int number) {
            int sum = 0;
            for (int i = 1; i <= number; i++) {
                sum += i;
            }
            return sum;
        }
    }

    static class FirstWriteTask implements Runnable {
        Data data;
        RandomGenerator randomGenerator = new RandomGenerator();

        FirstWriteTask(Data data) {
            this.data = data;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    data.writeRandomNumber(randomGenerator.generateFirstNumber());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class SecondWriteTask implements Runnable {
        Data data;
        RandomGenerator randomGenerator = new RandomGenerator();

        SecondWriteTask(Data data) {
            this.data = data;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    data.writeRandomNumber(randomGenerator.generateSecondNumber());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class ComputeTask implements Runnable {
        Data data;

        ComputeTask(Data data) {
            this.data = data;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    System.out.println("Calculated sum: " + Calculator.calculateSum(data.readRandomNumber()));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class ThreadMonitor implements Runnable {

        Data data;
        Thread t1;
        Thread t2;
        Thread t3;

        ThreadMonitor(Data data, Thread t1, Thread t2, Thread t3) {
            this.data = data;
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    System.out.println(data.randomNumber);
                    System.out.println(t1.getName() + " Status: " + t1.getState());
                    System.out.println(t2.getName() + " Status: " + t2.getState());
                    System.out.println(t3.getName() + " Status: " + t3.getState());
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}