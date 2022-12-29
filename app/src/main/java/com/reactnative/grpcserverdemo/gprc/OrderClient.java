package com.reactnative.grpcserverdemo.gprc;


import android.annotation.SuppressLint;
import android.util.Log;

import com.google.protobuf.StringValue;
import com.reactnative.grpcserverdemo.gprc.intercept.MyClient2Interceptor;
import com.reactnative.grpcserverdemo.gprc.intercept.MyClientInterceptor;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import io.grpc.BinaryLog;
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientStreamTracer;
import io.grpc.ForwardingClientCall;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.ServerMethodDefinition;
import io.grpc.stub.AbstractStub;
import io.grpc.stub.StreamObserver;
import order.OrderManagementGrpc;
import order.OrderManagementOuterClass;

/**
 * @Author qitao
 * @Date 2022/12/14 11:04
 */
public class OrderClient {
    private final String TAG = "OrderClient";
    private ManagedChannel channel;
    private OrderManagementGrpc.OrderManagementStub stub;
//    private OrderManagementGrpc.OrderManagementBlockingStub blockingStub;
    public OrderClient(String ip,int port){
        channel = ManagedChannelBuilder
                .forAddress(ip, port)
                .usePlaintext()
                .intercept(new MyClientInterceptor(),new MyClient2Interceptor())
//                .intercept(new MyClientInterceptor())
                .setBinaryLog(new BinaryLog() {
                    @Override
                    public <ReqT, RespT> ServerMethodDefinition<?, ?> wrapMethodDefinition(ServerMethodDefinition<ReqT, RespT> oMethodDef) {
                        Log.d(TAG,"OrderClient setBinaryLog wrapMethodDefinition " + oMethodDef);
                        return oMethodDef;
                    }

                    @Override
                    public Channel wrapChannel(Channel channel) {
                        Log.d(TAG,"OrderClient setBinaryLog wrapChannel " + channel);
                        return channel;
//                        return new Channel() {
//                            @Override
//                            public <RequestT, ResponseT> ClientCall<RequestT, ResponseT> newCall(MethodDescriptor<RequestT, ResponseT> methodDescriptor, CallOptions callOptions) {
////                                try{
////                                    Log.d(TAG,"OrderClient setBinaryLog wrapChannel clientCall start ");
////                                    ClientCall clientCall = channel.newCall(methodDescriptor,callOptions);
////                                    Log.d(TAG,"OrderClient setBinaryLog wrapChannel clientCall end ");
////                                    return clientCall;
////                                }catch (Exception e){
////                                    Log.e(TAG,"OrderClient setBinaryLog wrapChannel clientCall err " + e.getMessage());
////                                }
//
//                                return new ForwardingClientCall.SimpleForwardingClientCall<RequestT, ResponseT>(channel.newCall(methodDescriptor,callOptions)) {
//                                    @Override
//                                    protected ClientCall<RequestT, ResponseT> delegate() {
//                                        return super.delegate();
//                                    }
//
//                                    @Override
//                                    public void sendMessage(RequestT message) {
//                                        Log.d(TAG,"OrderClient setBinaryLog sendMessage start ");
//                                        super.sendMessage(message);
//                                        Log.d(TAG,"OrderClient setBinaryLog sendMessage end ");
//                                    }
//                                };
//                            }
//
//                            @Override
//                            public String authority() {
//                                return channel.authority();
//                            }
//                        };
                    }

                    @Override
                    public void close() throws IOException {
                        Log.d(TAG,"OrderClient close");
                    }
                })
                .build();
//        blockingStub = OrderManagementGrpc.
//                newBlockingStub(channel)// 生成一个远端服务在client的存根，看名称应该是阻塞调用
//                .withDeadlineAfter(5, TimeUnit.SECONDS);
        stub = OrderManagementGrpc.newStub(channel);// 生成一个远端服务在client的存根，看名称应该是阻塞调用
//        AbstractStub.StubFactory<OrderManagementGrpc.OrderManagementStub> factory =
//                new io.grpc.stub.AbstractStub.StubFactory<OrderManagementGrpc.OrderManagementStub>() {
//                    @SuppressLint("CheckResult")
//                    @Override
//                    public OrderManagementGrpc.OrderManagementStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
//                        callOptions.withStreamTracerFactory(new );
//                        return OrderManagementGrpc.OrderManagementStub.newStub(this,channel, callOptions);
//                    }
//                };
//
//        stub = OrderManagementGrpc.OrderManagementStub.newStub(factory,channel);
//                .withInterceptors(new MyClientInterceptor());
    }

    public void getOrderByUnary(String id){
//        if(blockingStub == null){
//            blockingStub = OrderManagementGrpc.newBlockingStub(channel).withDeadlineAfter(5, TimeUnit.SECONDS);
//        }
//        Log.d(TAG,"getOrderByUnary start id = " + id);
//        try{
//            OrderManagementOuterClass.Order order = blockingStub.getOrderByUnary(StringValue.newBuilder().setValue(id).build());
//            Log.d(TAG,"getOrderByUnary onNext : id " + order.getId());
//        }catch (Exception e){
//            blockingStub = null;
//            Log.e(TAG,"getOrderByUnary err = " + e.getMessage());
//        }


        if(stub == null){
            stub = OrderManagementGrpc.newStub(channel);
//            .withInterceptors(new MyClientInterceptor());
        }
        stub.getOrderByUnary(StringValue.newBuilder().setValue(id).build(), new StreamObserver<OrderManagementOuterClass.Order>() {
            @Override
            public void onNext(OrderManagementOuterClass.Order value) {
                Log.d(TAG,"getOrderByUnary onNext : id " + value.getId());
            }

            @Override
            public void onError(Throwable t) {
                stub = null;
                Log.d(TAG,"getOrderByUnary onError: " +t.getMessage());
            }

            @Override
            public void onCompleted() {
                Log.d(TAG,"getOrderByUnary onCompleted.");
            }
        });
    }

    public void getOrderByServerStream(String id){
//        if(blockingStub == null){
//            blockingStub = OrderManagementGrpc.newBlockingStub(channel).withDeadlineAfter(5, TimeUnit.SECONDS);
//        }
//        Log.d(TAG,"getOrderByUnary start id = " + id);
//        try{
//            Iterator<OrderManagementOuterClass.Order> iterator = blockingStub.getOrderByServerStream(StringValue.newBuilder().setValue(id).build());
//            Log.d(TAG,"getOrderByUnary onNext : iterator " + iterator);
//        }catch (Exception e){
//            blockingStub = null;
//            Log.e(TAG,"getOrderByUnary err = " + e.getMessage());
//        }


        if(stub == null){
            stub = OrderManagementGrpc.newStub(channel);
//            .withInterceptors(new MyClientInterceptor());
        }
        Log.d(TAG,"getOrderByServerStream start id = " + id);
        stub.getOrderByServerStream(StringValue.newBuilder().setValue(id).build(), new StreamObserver<OrderManagementOuterClass.Order>() {
            @Override
            public void onNext(OrderManagementOuterClass.Order value) {
                Log.d(TAG,"getOrderByServerStream onNext : id " + value.getId());
            }

            @Override
            public void onError(Throwable t) {
                stub = null;
                Log.d(TAG,"getOrderByServerStream onError: " +t.getMessage());
            }

            @Override
            public void onCompleted() {
                Log.d(TAG,"getOrderByServerStream onCompleted.");
            }
        });


    }

    @SuppressLint("CheckResult")
    public void getOrderByClientStream(){
        if(stub == null){
            stub = OrderManagementGrpc.newStub(channel);
//                    .withInterceptors(new MyClientInterceptor());
        }
        StreamObserver<StringValue> observer = stub.getOrderByClientStream(new StreamObserver<OrderManagementOuterClass.Order>() {

            @Override
            public void onNext(OrderManagementOuterClass.Order value) {
                Log.d(TAG,"getOrderByClientStream onNext : id = " + value.getId());
            }

            @Override
            public void onError(Throwable t) {
                stub = null;
                Log.d(TAG,"getOrderByServerStream onError: " +t.getMessage());
            }

            @Override
            public void onCompleted() {
                Log.d(TAG,"getOrderByClientStream onCompleted.");
            }
        });
        Log.d(TAG,"getOrderByClientStream onNext : start");
        observer.onNext(StringValue.newBuilder().setValue("1").build());
        Log.d(TAG,"getOrderByClientStream onNext : 1 end");
        observer.onNext(StringValue.newBuilder().setValue("2").build());
        Log.d(TAG,"getOrderByClientStream onNext : 2 end");
        observer.onNext(StringValue.newBuilder().setValue("3").build());
        observer.onCompleted();
    }


    public void getOrderByTwoWayStream(){
        if(stub == null){
            stub = OrderManagementGrpc.newStub(channel);
//                    .withInterceptors(new MyClientInterceptor());
        }
        StreamObserver<StringValue> observer = stub.getOrderByTwoWayStream(new StreamObserver<OrderManagementOuterClass.Order>() {

            @Override
            public void onNext(OrderManagementOuterClass.Order value) {
                Log.d(TAG,"getOrderByTwoWayStream onNext : id = " + value.getId());
            }

            @Override
            public void onError(Throwable t) {
                stub = null;
                Log.d(TAG,"getOrderByTwoWayStream onError: " +t.getMessage());
            }

            @Override
            public void onCompleted() {
                Log.d(TAG,"getOrderByTwoWayStream onCompleted.");
            }
        });
        observer.onNext(StringValue.newBuilder().setValue("1").build());
        observer.onNext(StringValue.newBuilder().setValue("2").build());
        observer.onCompleted();
    }
}
