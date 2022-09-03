package org.shaotang.hadoop.hdfs.rpc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

import java.io.IOException;

public class NNServer implements RPCProtocol {

    @Override
    public void mkdirs(String path) {

        System.out.println("mkdirs");
    }

    public static void main(String[] args) throws IOException {

        RPC.Server server = new RPC.Builder(new Configuration())
                .setBindAddress("localhost")
                .setPort(8888)
                .setProtocol(RPCProtocol.class)
                .setInstance(new NNServer())
                .build();
        System.out.println("start");
        server.start();
    }
}
