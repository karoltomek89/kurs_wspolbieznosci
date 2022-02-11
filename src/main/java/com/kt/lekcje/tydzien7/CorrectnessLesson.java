package com.kt.lekcje.tydzien7;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class CorrectnessLesson {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionsLesson.class);

    public static void main(String[] args) {
        logger.info("Starting");

        Store store = new Store();

        Set<Integer> ids =new HashSet<>();

        ids.add(store.add("ksiazka"));
        ids.add(store.add("rower"));
        ids.add(store.add("zabawka"));
        ids.add(store.add("koszulka"));
        ids.add(store.add("jeansy"));
        ids.add(store.add("kurtka"));
        ids.add(store.add("buty"));

       logger.info("Ids size correctness: {}", ids.size() == 7);
       logger.info("Store size: {}", store.size());
       logger.info("Store: {}", store.inventory);

        logger.info("DONE");

    }

    static class Store {
        private Map<Integer, String> inventory = new ConcurrentHashMap<>();

        public Integer add(String name) {
            int id = inventory.size();
            inventory.put(id, name);
            return id;
        }

        public String getById(Integer id) {
            return inventory.get(id);
        }

        public Integer size(){
            return inventory.size();
        }
    }
}
