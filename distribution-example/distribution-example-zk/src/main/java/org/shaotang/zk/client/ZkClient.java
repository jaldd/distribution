package org.shaotang.zk.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ZkClient {

    private String connectString = "hadoop-pseudo:21811";
    private ZooKeeper zooKeeper;
    int sessionTimeout = 2000;

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ZkClient client = new ZkClient();
        client.getConnect();
        client.getServerList();
        client.business();
    }

    private void business() throws InterruptedException {
        Thread.sleep(Long.MAX_VALUE);
    }

    private void getServerList() throws InterruptedException, KeeperException {
        List<String> children = zooKeeper.getChildren("/servers", true);
        List<String> servers = new ArrayList<>();
        for (String child : children) {
            byte[] data = zooKeeper.getData("/servers/" + child, false, null);
            servers.add(new String(data));
        }
        log.info("servers:" + servers);
    }

    private void getConnect() throws IOException {
        zooKeeper = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {

                try {
                    getServerList();
                    Thread.sleep(3000);
                } catch (InterruptedException | KeeperException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
