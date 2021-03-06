package com.kt.lekcje.tydzien4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

class ForkJoinPoolLesson {

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + " Starting...");

        ForkJoinPool joinPool = ForkJoinPool.commonPool();
//        ForkJoinPool joinPool = new ForkJoinPool(10);


        TreeNode tree = new TreeNode(10,
                new TreeNode(19,
                        new TreeNode(7)),
                new TreeNode(7,
                        new TreeNode(3),
                        new TreeNode(9)),
                new TreeNode(5));

        Integer sum = joinPool.invoke(new CountingTask(tree));

        System.out.println("Result is: " + sum);

        System.out.println(Thread.currentThread().getName() + " Done");
    }

    static class TreeNode {
        int value;
        List<TreeNode> children;

        TreeNode(int value, TreeNode... children) {
            this.value = value;
            this.children = Arrays.asList(children);
        }
    }

    static class CountingTask extends RecursiveTask<Integer> {

        TreeNode node;

        CountingTask(TreeNode node) {
            this.node = node;
        }

        @Override
        protected Integer compute() {
            System.out.println(Thread.currentThread().getName() + " Computing value... " + node.value);
            if (node.children.isEmpty()) {
                return node.value;
            } else {
                List<CountingTask> tasks = new ArrayList<>();
                for (TreeNode child : node.children) {
                    tasks.add((new CountingTask(child)));
                }
                for (CountingTask task : tasks) {
                    task.fork();
                }
                int sum = node.value;
                for (CountingTask task : tasks) {
                    sum += task.join();
                }
                return sum;
            }
        }
    }
}
