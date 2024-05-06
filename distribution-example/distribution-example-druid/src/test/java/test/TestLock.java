package test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class TestLock {

    private static Map<String, ReentrantLock> lockMap = new ConcurrentHashMap<>();

    public static void main(String[] args) throws InterruptedException {

        ReentrantLock lock = lockMap.computeIfAbsent("abc", k -> new ReentrantLock());

        System.out.println(lockMap);
        lock.lock();

        Thread.sleep(2000);

        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
        System.out.println(lockMap);
    }
}
