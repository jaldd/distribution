package server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
 
public class GrpcServer {
 
    private Server server;
    /**
     * @param port 服务端占用的端口
     */
    public GrpcServer(int port) {
        server = ServerBuilder.forPort(port)
              // 将具体实现的服务添加到gRPC服务中
                .addService(new HelloWorldRpcService())
                .build();
    }
 
    public void start() throws IOException {
        server.start();
    }
 
    public void shutdown() {
        server.shutdown();
    }
}