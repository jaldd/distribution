package org.shaotang.zk.server;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class ZkServer {

    private String connectString = "hadoop-pseudo:21811";
    private ZooKeeper zooKeeper;
    int sessionTimeout = 2000;

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ZkServer server = new ZkServer();
        server.getConnect();
        server.register(args[0]);
        server.business();
    }

    private void business() throws InterruptedException {
        Thread.sleep(Long.MAX_VALUE);
    }

    private void register(String hostname) throws InterruptedException, KeeperException {
        String server = zooKeeper.create("/servers/" + hostname, hostname.getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        log.info(hostname + " online");
    }

    private void getConnect() throws IOException {
        zooKeeper = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {

            }
        });
    }
}
