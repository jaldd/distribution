package server;

import io.grpc.stub.StreamObserver;
import proto.com.example.grpc.Greeting;
import proto.com.example.grpc.HelloResp;
import grpc.HelloWorldGrpc;

public class HelloWorldRpcService extends HelloWorldGrpc.HelloWorldImplBase {
 
    @Override
    public void sayHello(Greeting request, StreamObserver<HelloResp> responseObserver) {
        String name = request.getName();
        HelloResp resp = HelloResp.newBuilder()
                .setReply("Hello " + name + "!")
                .build();
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }
}