package test;

public class TestSyn {

    public static void main(String[] args) {
        String key = "abc";
        String key1 = "abc1";
        String key2 = "abc";
        String key3 = "abc";
        // 创建一个共享资源
        SharedResource sharedResource = new SharedResource();    // 创建两个线程来并发访问共享资源
        Thread thread1 = new Thread(() -> {
            sharedResource.incrementCount(key); // 传递锁对象
        }, "Thread-1");
        Thread thread2 = new Thread(() -> {
            sharedResource.incrementCount(key); // 传递锁对象
        }, "Thread-2");
        Thread thread11 = new Thread(() -> {
            sharedResource.incrementCount(key2); // 传递锁对象
        }, "Thread-11");
        Thread thread12 = new Thread(() -> {
            sharedResource.incrementCount(key); // 传递锁对象
        }, "Thread-12");
        Thread thread111 = new Thread(() -> {
            sharedResource.incrementCount(key3); // 传递锁对象
        }, "Thread-111");
        Thread thread112 = new Thread(() -> {
            sharedResource.incrementCount(key1); // 传递锁对象
        }, "Thread-1112");
        thread1.start();
        thread2.start();
        thread11.start();
        thread12.start();
        thread111.start();
        thread112.start();
        try {
            thread1.join();
            thread2.join();
            thread11.join();
            thread12.join();
            thread111.join();
            thread112.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Final count: " + sharedResource.getCount());
    }

    static class SharedResource {
        private int count = 0;

        public void incrementCount(Object lock) {
            synchronized (lock) { // 使用对象作为锁
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                count++;
                System.out.println(Thread.currentThread().getName() + ": Incremented count to " + count);
            }
        }

        public int getCount() {
            return count;
        }
    }

}
