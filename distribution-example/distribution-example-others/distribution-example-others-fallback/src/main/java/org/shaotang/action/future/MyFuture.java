package org.shaotang.action.future;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MyFuture {

    public static void main(String[] args) throws InterruptedException {

        ExecutorService executorService = Executors.newCachedThreadPool();
        Runnable task1 = () -> System.out.println("this is task1");
        Callable<Integer> task2 = () -> 2;

        Future<?> f1 = executorService.submit(task1);
        Future<Integer> f2 = executorService.submit(task2);
        System.out.println("f1:" + f1.isDone() + ";f2:" + f2.isDone());
        while (!f1.isDone()) {
            System.out.println("t1 is not done");
            Thread.sleep(100L);
        }
        while (!f2.isDone()) {
            System.out.println("t2 is not done");
            Thread.sleep(100L);
        }
        System.out.println("f1:" + f1.isDone() + ";f2:" + f2.isDone());
    }
}
