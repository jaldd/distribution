package org.shaotang.zk.curator;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Before;
import org.junit.Test;

@Slf4j
public class TestCuratorFramework {

    private CuratorFramework client;

    @Before
    public void before() throws InterruptedException {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3, 5000);
        String connectString = "hadoop-pseudo:21811";
        client = CuratorFrameworkFactory.builder()
                .connectString(connectString)
                .sessionTimeoutMs(3000)
                .connectionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .build();
        client.start();
        client.blockUntilConnected();

    }

    @Test
    public void forPath() throws Exception {
        String path = "/test1";
        client.create().forPath(path);
    }

    @Test
    public void deletePath() throws Exception {
        client.delete().forPath("/test1");
    }

    @Test
    public void testLock() throws InterruptedException {
        String lockPath = "/lockTest";
        InterProcessMutex lock = new InterProcessMutex(client, lockPath);
        for (int i = 0; i < 50; i++) {
            System.out.println("i:" + i);
            new Thread(new InterProcess(i, lock)).start();
        }
        Thread.sleep(10000);
    }

    static class InterProcess implements Runnable {

        private Integer current;
        private InterProcessMutex lock;

        public InterProcess(Integer current, InterProcessMutex lock) {
            this.current = current;
            this.lock = lock;
        }


        @Override
        public void run() {

            try {
                lock.acquire();
                System.out.println("current:" + current);
                log.info("current:" + current);
                Thread.sleep(100);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                try {
                    lock.release();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

}
