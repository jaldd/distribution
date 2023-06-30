package org.shaotang.zk.lock;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class DistributeLock {

    private String connectString = "hadoop-pseudo:21811";
    private ZooKeeper zooKeeper;
    int sessionTimeout = 2000;
    private String waitPath;
    String currentMode;

    private CountDownLatch countDownLatch = new CountDownLatch(1);
    private CountDownLatch waitLatch = new CountDownLatch(1);

    public DistributeLock() throws IOException, InterruptedException, KeeperException {
        zooKeeper = new ZooKeeper(connectString, sessionTimeout, watchedEvent -> {

            if (Watcher.Event.KeeperState.SyncConnected.equals(watchedEvent.getState())) {
                countDownLatch.countDown();
            }
            if (Watcher.Event.EventType.NodeDeleted.equals(watchedEvent.getType()) && watchedEvent.getPath().equals(waitPath)) {
                waitLatch.countDown();
            }
        });
        countDownLatch.await();
        Stat exists = zooKeeper.exists("/locks", false);
        if (exists == null) {
            zooKeeper.create("/locks", "locks".getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
    }

    public void lock() {
        try {
            currentMode = zooKeeper.create("/locks/seq-", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            List<String> children = zooKeeper.getChildren("/locks", false);
            if (children.size() == 1) {
                return;
            }
            Collections.sort(children);
            String thisNode = currentMode.substring("/locks/".length());
            int index = children.indexOf(thisNode);

            if (index > 0) {
                waitPath = "/locks/" + children.get(index - 1);
                zooKeeper.getData(waitPath, true, null);
                waitLatch.await();
            }
        } catch (KeeperException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void unlock() {
        try {
            zooKeeper.delete(currentMode,-1);
        } catch (InterruptedException | KeeperException e) {
            throw new RuntimeException(e);
        }
    }
}
