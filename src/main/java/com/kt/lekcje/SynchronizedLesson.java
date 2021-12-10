package com.kt.lekcje;

class SynchronizedLesson {
    SynchronizedLesson() {
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting...");
        final SynchronizedLesson.Counter counter = new SynchronizedLesson.Counter();
        final SynchronizedLesson.Counter otherCounter = new SynchronizedLesson.Counter();
        Runnable incrementTask = new Runnable() {
            public synchronized void run() {
                for(int i = 0; i < 100000; ++i) {
                    counter.increment();
                    SynchronizedLesson.Counter var10000 = counter;
                    SynchronizedLesson.Counter.incrementInstances();
                    var10000 = otherCounter;
                    SynchronizedLesson.Counter.incrementInstances();
                }

            }

            public void decrement() {
                synchronized(this) {
                    for(int i = 0; i < 100000; ++i) {
                        counter.decrement();
                    }

                }
            }
        };
        Thread t0 = new Thread(incrementTask);
        Thread t1 = new Thread(incrementTask);
        Thread t2 = new Thread(incrementTask);
        t0.start();
        t1.start();
        t2.start();
        t0.join();
        t1.join();
        t2.join();
        System.out.println("Counter value is: " + counter.value + ", expected: 300000 ");
        System.out.println("Instances value is: " + SynchronizedLesson.Counter.instances);
        System.out.println("Counter value is: " + counter.value + ", expected: -300000 ");
        System.out.println("Done");
    }

    static class Counter {
        private long value = 0L;
        private long otherValue = 0L;
        private static long instances = 0L;

        Counter() {
        }

        public void increment() {
            ++this.value;
        }

        public void decrement() {
            --this.value;
        }

        public static synchronized void incrementInstances() {
            ++instances;
        }
    }
}

