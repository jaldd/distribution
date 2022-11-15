package org.shaotang.zk;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
public class TestZkClient {

    private String connectString = "hadoop-pseudo:21811";
    private ZooKeeper zooKeeper;

    @Before
    public void init() throws IOException {
        zooKeeper = new ZooKeeper(connectString, 2000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {

            }
        });
    }

    @Test
    public void create() throws IOException, InterruptedException, KeeperException {
        String hello = zooKeeper.create("/hello", "hello".getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(hello);
    }

    @Test
    public void createChildren() throws InterruptedException, KeeperException {
        List<String> children = zooKeeper.getChildren("/", true);
        for (String child : children) {
            System.out.println(child);
        }
        Thread.sleep(Long.MAX_VALUE);
    }

    @Test
    public void exists() throws InterruptedException, KeeperException {

        Stat exists = zooKeeper.exists("/hello", false);
        System.out.println(exists);
    }
}
