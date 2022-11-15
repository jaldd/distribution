package org.shaotang.zk.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Before;
import org.junit.Test;

public class TestCuratorFramework {

    private CuratorFramework client;
    private String connectString = "hadoop-pseudo:21811";

    @Before
    public void before() throws InterruptedException {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
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

}
