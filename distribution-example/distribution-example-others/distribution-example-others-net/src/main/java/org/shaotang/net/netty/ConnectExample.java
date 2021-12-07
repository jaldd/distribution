package org.shaotang.net.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class ConnectExample {

    private static final Channel CHANNEL_FROM_SOMEWHERE = new NioSocketChannel();

    public static void connect() {

        Channel channel = CHANNEL_FROM_SOMEWHERE;
        ChannelFuture future = channel.connect(new InetSocketAddress("127.0.0.1", 25));
        future.addListener((ChannelFutureListener) channelFuture -> {
            if (future.isSuccess()) {
                ByteBuf byteBuf = Unpooled.copiedBuffer("Hello", Charset.defaultCharset());
                ChannelFuture cf = future.channel().writeAndFlush(byteBuf);
            } else {

                Throwable cause = future.cause();
                cause.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        connect();
    }
}
