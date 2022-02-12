package com.kt.lekcje.tydzien7;

import net.jodah.concurrentunit.Waiter;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public class AsyncTestingLesson {

    private static final Logger logger = LoggerFactory.getLogger(AsyncTestingLesson.class);

    private static Random random = new Random();

    static class AsyncCalculation {
        private volatile int number = 0;
        private final Executor executor;

        AsyncCalculation(Executor executor) {
            this.executor = executor;
        }

        public void calculate() {
            executor.execute(() -> {
                try {
                    Thread.sleep(1_000);
                    number = random.nextInt(100) + 1;
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
            });
        }
    }

    @Test
    public void shouldUpdateANumber() throws InterruptedException {
        AsyncCalculation calculation = new AsyncCalculation(Executors.newSingleThreadExecutor());
        calculation.calculate();
        Thread.sleep(2_000);
        Assertions.assertTrue(calculation.number > 0, "number is greater then 0");
    }

    @Test
    public void shouldUpdateANumberAwait() {
        AsyncCalculation calculation = new AsyncCalculation(Executors.newSingleThreadExecutor());
        calculation.calculate();
        ConditionFactory await = Awaitility.await();
        await.atMost(2_000, TimeUnit.MILLISECONDS);
        await.until(()-> calculation.number > 0);
    }

    static class MessageBugs {
        private final List<Consumer<String>> listeners = new ArrayList<>();
        private final Executor executor;

        MessageBugs(Executor executor) {
            this.executor = executor;
        }

        public void send(String message) {
            executor.execute(() -> {
                listeners.forEach(c -> {
                    try {
                        Thread.sleep(500);
                        c.accept(message);
                    } catch (InterruptedException e) {
                        throw new IllegalStateException(e);
                    }
                });
            });
        }

        public void register(Consumer<String> consumer) {
            listeners.add(consumer);
        }
    }

    @Test
    public void shouldGetMessage() throws TimeoutException, InterruptedException {
        Waiter waiter = new Waiter();
        MessageBugs messageBugs = new MessageBugs(Executors.newSingleThreadExecutor());
        messageBugs.register(m->{
           waiter.assertEquals(m, "secret");
           waiter.resume();
        });
        messageBugs.send("secret");
        waiter.await(1_000, TimeUnit.MILLISECONDS);
    }
}