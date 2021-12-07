package org.shaotang.net.netty.echoserver;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

public class EchoServer {

    private int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public void start() {
        EchoServerHandler serverHandler = new EchoServerHandler();
        EventLoopGroup group = new NioEventLoopGroup();
    }

    public static void main(String[] args) {

    }
}
