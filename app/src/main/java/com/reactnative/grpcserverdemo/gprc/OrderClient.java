package com.reactnative.grpcserverdemo.gprc;


import android.annotation.SuppressLint;
import android.util.Log;

import com.google.protobuf.StringValue;
import com.reactnative.grpcserverdemo.gprc.intercept.MyClient2Interceptor;
import com.reactnative.grpcserverdemo.gprc.intercept.MyClientInterceptor;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
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
    private OrderManagementGrpc.OrderManagementBlockingStub blockingStub;
    public OrderClient(String ip,int port){
        channel = ManagedChannelBuilder
                .forAddress(ip, port)
                .usePlaintext()
                .intercept(new MyClientInterceptor(),new MyClient2Interceptor())
                .build();
        blockingStub = OrderManagementGrpc.
                newBlockingStub(channel)// 生成一个远端服务在client的存根，看名称应该是阻塞调用
                .withDeadlineAfter(5, TimeUnit.SECONDS);
        stub = OrderManagementGrpc.newStub(channel); // 生成一个远端服务在client的存根，看名称应该是阻塞调用
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
        if(stub == null){
            stub = OrderManagementGrpc.newStub(channel);
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
        observer.onNext(StringValue.newBuilder().setValue("1").build());
        observer.onNext(StringValue.newBuilder().setValue("2").build());
//        observer.onCompleted();
    }


    public void getOrderByTwoWayStream(){
        if(stub == null){
            stub = OrderManagementGrpc.newStub(channel);
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
