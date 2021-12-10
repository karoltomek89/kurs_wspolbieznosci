package com.kt.lekcje.tydzien3;

class LiveLockLesson {
    LiveLockLesson() {
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting...");
        Object spoon = new Object();
        final LiveLockLesson.Person wife = new LiveLockLesson.Person(spoon);
        final LiveLockLesson.Person housband = new LiveLockLesson.Person(spoon);
        Thread t0 = new Thread(new Runnable() {
            public void run() {
                wife.eatWith(housband);
            }
        }, "WIFE");
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                housband.eatWith(wife);
            }
        }, "HOUSBAND");
        t0.start();
        t1.start();
        t0.join();
        t1.join();
        System.out.println("DONE");
    }

    static class Person {
        Object spoon;
        boolean isHungry = true;

        Person(Object spoon) {
            this.spoon = spoon;
        }

        public void eatWith(LiveLockLesson.Person partner) {
            while(this.isHungry) {
                synchronized(this.spoon) {
                    System.out.println(Thread.currentThread().getName() + "Eating...");
                    this.isHungry = false;
                }
            }

        }
    }
}
