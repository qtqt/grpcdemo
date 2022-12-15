package com.reactnative.grpcserverdemo.gprc;


import android.annotation.SuppressLint;
import android.util.Log;

import com.google.protobuf.StringValue;

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
    public OrderClient(String ip,int port){
        channel = ManagedChannelBuilder
                .forAddress(ip, port)
                .usePlaintext()
                .build();
        stub = OrderManagementGrpc.newStub(channel); // 生成一个远端服务在client的存根，看名称应该是阻塞调用
    }

    public void getOrderByUnary(String id){
        Log.d(TAG,"getOrderByUnary start id = " + id);
        stub.getOrderByUnary(StringValue.newBuilder().setValue(id).build(), new StreamObserver<OrderManagementOuterClass.Order>() {
            @Override
            public void onNext(OrderManagementOuterClass.Order value) {
                Log.d(TAG,"getOrderByUnary onNext : id " + value.getId());
            }

            @Override
            public void onError(Throwable t) {
                Log.d(TAG,"getOrderByUnary onError: " +t.getMessage());
            }

            @Override
            public void onCompleted() {
                Log.d(TAG,"getOrderByUnary onCompleted.");
            }
        });
    }

    public void getOrderByServerStream(String id){
        Log.d(TAG,"getOrderByServerStream start id = " + id);
        stub.getOrderByServerStream(StringValue.newBuilder().setValue(id).build(), new StreamObserver<OrderManagementOuterClass.Order>() {
            @Override
            public void onNext(OrderManagementOuterClass.Order value) {
                Log.d(TAG,"getOrderByServerStream onNext : id " + value.getId());
            }

            @Override
            public void onError(Throwable t) {
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
        StreamObserver<StringValue> observer = stub.getOrderByClientStream(new StreamObserver<OrderManagementOuterClass.Order>() {

            @Override
            public void onNext(OrderManagementOuterClass.Order value) {
                Log.d(TAG,"getOrderByClientStream onNext : id = " + value.getId());
            }

            @Override
            public void onError(Throwable t) {
                Log.d(TAG,"getOrderByServerStream onError: " +t.getMessage());
            }

            @Override
            public void onCompleted() {
                Log.d(TAG,"getOrderByClientStream onCompleted.");
            }
        });
        observer.onNext(StringValue.newBuilder().setValue("1").build());
        observer.onNext(StringValue.newBuilder().setValue("2").build());
        observer.onCompleted();
    }


    public void getOrderByTwoWayStream(){
        StreamObserver<StringValue> observer = stub.getOrderByTwoWayStream(new StreamObserver<OrderManagementOuterClass.Order>() {

            @Override
            public void onNext(OrderManagementOuterClass.Order value) {
                Log.d(TAG,"getOrderByTwoWayStream onNext : id = " + value.getId());
            }

            @Override
            public void onError(Throwable t) {
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
