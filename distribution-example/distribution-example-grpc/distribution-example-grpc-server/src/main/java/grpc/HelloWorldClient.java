package grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import proto.com.example.grpc.Greeting;
import proto.com.example.grpc.HelloResp;

public class HelloWorldClient {
 
    private final HelloWorldGrpc.HelloWorldBlockingStub blockingStub;
   /**
    * @param host gRPC服务的主机名
    * @param port gRPC服务的端口
    */
    public HelloWorldClient(String host, int port) {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(host, port)
              // 使用非安全机制传输
                .usePlaintext()
                .build();
 
        blockingStub = HelloWorldGrpc.newBlockingStub(managedChannel);
    }
 
    public String sayHello(String name) {
        Greeting greeting = Greeting.newBuilder()
                .setName(name)
                .build();
        HelloResp resp = blockingStub.sayHello(greeting);
 
        return resp.getReply();
    }
}