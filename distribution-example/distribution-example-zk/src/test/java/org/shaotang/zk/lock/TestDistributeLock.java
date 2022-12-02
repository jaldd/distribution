package org.shaotang.zk.lock;

import org.apache.zookeeper.KeeperException;

import java.io.IOException;

public class TestDistributeLock {

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        DistributeLock lock1 = new DistributeLock();
        DistributeLock lock2 = new DistributeLock();
        new Thread(() -> {
            try {
                lock1.lock();
                System.out.println("1");
                Thread.sleep(5000);
                System.out.println("1 1");
                lock1.unlock();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        new Thread(() -> {
            try {
                lock2.lock();
                System.out.println("2");
                Thread.sleep(5000);
                lock2.unlock();
                System.out.println("2 2");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
