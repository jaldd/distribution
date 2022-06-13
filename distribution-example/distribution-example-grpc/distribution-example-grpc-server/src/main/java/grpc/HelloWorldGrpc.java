package grpc;

import io.grpc.MethodDescriptor;
import io.grpc.stub.ClientCalls;
import io.grpc.stub.ServerCalls;
import proto.com.example.grpc.Greeting;
import proto.com.example.grpc.HelloResp;
import proto.com.example.grpc.HelloWorldProto;


/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.21.0)",
    comments = "Source: hello_world.proto")
public final class HelloWorldGrpc {

  private HelloWorldGrpc() {}

  public static final String SERVICE_NAME = "HelloWorld";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<Greeting,
          HelloResp> getSayHelloMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "sayHello",
      requestType = Greeting.class,
      responseType = HelloResp.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<Greeting,
      HelloResp> getSayHelloMethod() {
    io.grpc.MethodDescriptor<Greeting, HelloResp> getSayHelloMethod;
    if ((getSayHelloMethod = HelloWorldGrpc.getSayHelloMethod) == null) {
      synchronized (HelloWorldGrpc.class) {
        if ((getSayHelloMethod = HelloWorldGrpc.getSayHelloMethod) == null) {
          HelloWorldGrpc.getSayHelloMethod = getSayHelloMethod = 
              io.grpc.MethodDescriptor.<Greeting, HelloResp>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(MethodDescriptor.generateFullMethodName(
                  "HelloWorld", "sayHello"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  Greeting.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  HelloResp.getDefaultInstance()))
                  .setSchemaDescriptor(new HelloWorldMethodDescriptorSupplier("sayHello"))
                  .build();
          }
        }
     }
     return getSayHelloMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static HelloWorldStub newStub(io.grpc.Channel channel) {
    return new HelloWorldStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static HelloWorldBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new HelloWorldBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static HelloWorldFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new HelloWorldFutureStub(channel);
  }

  /**
   */
  public static abstract class HelloWorldImplBase implements io.grpc.BindableService {

    /**
     */
    public void sayHello(Greeting request,
        io.grpc.stub.StreamObserver<HelloResp> responseObserver) {
      ServerCalls.asyncUnimplementedUnaryCall(getSayHelloMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getSayHelloMethod(),
            ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                Greeting,
                HelloResp>(
                  this, METHODID_SAY_HELLO)))
          .build();
    }
  }

  /**
   */
  public static final class HelloWorldStub extends io.grpc.stub.AbstractStub<HelloWorldStub> {
    private HelloWorldStub(io.grpc.Channel channel) {
      super(channel);
    }

    private HelloWorldStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected HelloWorldStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new HelloWorldStub(channel, callOptions);
    }

    /**
     */
    public void sayHello(Greeting request,
        io.grpc.stub.StreamObserver<HelloResp> responseObserver) {
      ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSayHelloMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class HelloWorldBlockingStub extends io.grpc.stub.AbstractStub<HelloWorldBlockingStub> {
    private HelloWorldBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private HelloWorldBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected HelloWorldBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new HelloWorldBlockingStub(channel, callOptions);
    }

    /**
     */
    public HelloResp sayHello(Greeting request) {
      return ClientCalls.blockingUnaryCall(
          getChannel(), getSayHelloMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class HelloWorldFutureStub extends io.grpc.stub.AbstractStub<HelloWorldFutureStub> {
    private HelloWorldFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private HelloWorldFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected HelloWorldFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new HelloWorldFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<HelloResp> sayHello(
        Greeting request) {
      return ClientCalls.futureUnaryCall(
          getChannel().newCall(getSayHelloMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SAY_HELLO = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final HelloWorldImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(HelloWorldImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SAY_HELLO:
          serviceImpl.sayHello((Greeting) request,
              (io.grpc.stub.StreamObserver<HelloResp>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class HelloWorldBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    HelloWorldBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return HelloWorldProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("HelloWorld");
    }
  }

  private static final class HelloWorldFileDescriptorSupplier
      extends HelloWorldBaseDescriptorSupplier {
    HelloWorldFileDescriptorSupplier() {}
  }

  private static final class HelloWorldMethodDescriptorSupplier
      extends HelloWorldBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    HelloWorldMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (HelloWorldGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new HelloWorldFileDescriptorSupplier())
              .addMethod(getSayHelloMethod())
              .build();
        }
      }
    }
    return result;
  }
}
