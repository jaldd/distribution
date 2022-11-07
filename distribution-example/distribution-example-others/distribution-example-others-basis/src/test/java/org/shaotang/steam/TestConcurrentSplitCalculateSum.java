package org.shaotang.steam;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.concurrent.CountDownLatch;

public class TestConcurrentSplitCalculateSum {

    public static class ForkTask extends Thread {

        private int result = 0;

        private final Spliterator<Integer> spliterator;
        private final CountDownLatch latch;

        public ForkTask(Spliterator<Integer> spliterator, CountDownLatch latch) {
            this.spliterator = spliterator;
            this.latch = latch;
        }

        @Override
        public void run() {
            long start = System.currentTimeMillis();
            spliterator.forEachRemaining(num -> result = result + num);
            long end = System.currentTimeMillis();
            System.out.println("finish:" + result + ":use" + (end - start));
            latch.countDown();
        }

        public int getResult() {
            return result;
        }
    }

    private static int join(List<ForkTask> tasks) {
        int result = 0;
        for (ForkTask task : tasks) {
            result += task.getResult();
        }
        return result;
    }

    private static final int THREAD_NUM = 4;

    public static void main(String[] args) throws InterruptedException {

        List<Integer> source = new ArrayList<>();
        for (int i = 0; i < 101; i++) {
            source.add(i);
        }
        Spliterator<Integer> root = source.stream().spliterator();
        List<Spliterator<Integer>> spliteratorList = new ArrayList<>();
        Spliterator<Integer> x = root.trySplit();
        Spliterator<Integer> y = x.trySplit();
        Spliterator<Integer> z = root.trySplit();
        spliteratorList.add(root);
        spliteratorList.add(x);
        spliteratorList.add(y);
        spliteratorList.add(z);
        List<ForkTask> tasks = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(THREAD_NUM);
        for (int i = 0; i < THREAD_NUM; i++) {
            ForkTask task = new ForkTask(spliteratorList.get(i), latch);
            task.setName("fork-task-" + (i + 1));
            tasks.add(task);
        }
        tasks.forEach(Thread::start);
        latch.await();
        int result = join(tasks);
        System.out.println("result:" + result);
    }
}












