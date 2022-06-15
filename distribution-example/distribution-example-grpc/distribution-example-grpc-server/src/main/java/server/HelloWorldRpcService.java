package server;

import io.grpc.stub.StreamObserver;

public class HelloWorldRpcService extends com.example.grpc.HelloWorldGrpc.HelloWorldImplBase {
 
    @Override
    public void sayHello(com.example.grpc.Greeting request, StreamObserver<com.example.grpc.HelloResp> responseObserver) {
        String name = request.getName();
        com.example.grpc.HelloResp resp = com.example.grpc.HelloResp.newBuilder()
                .setReply("Hello " + name + "!")
                .build();
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }
}