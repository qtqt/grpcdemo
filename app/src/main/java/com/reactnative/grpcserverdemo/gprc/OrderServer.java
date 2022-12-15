package com.reactnative.grpcserverdemo.gprc;

import android.util.Log;

import com.google.protobuf.StringValue;
import com.reactnative.grpcserverdemo.gprc.intercept.MyServerInterceptor;

import java.io.IOException;

import io.grpc.Grpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptors;
import io.grpc.netty.NettyServerBuilder;
import io.grpc.stub.StreamObserver;
import order.OrderManagementGrpc;
import order.OrderManagementOuterClass;

/**
 * @Author qitao
 * @Date 2022/12/14 10:59
 */
public class OrderServer extends OrderManagementGrpc.OrderManagementImplBase {
    private final String TAG = "OrderServer";
    private Server server;
    private int port;
    public OrderServer(int port){
        this.port = port;
    }

    public void start(){
        Log.d(TAG," start");
        try {
            server = NettyServerBuilder.forPort(port)
                    .addService(this)
//                    .addService(ServerInterceptors.intercept(this,new MyServerInterceptor()))
                    .build()
                    .start();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG," start exception = " + e.getMessage());
        }
        Log.d(TAG," port = " + port);
        //等待服务关闭,这样是服务一直等待使用的状态了
        try {
            server.awaitTermination();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void getOrderByUnary(StringValue request, StreamObserver<OrderManagementOuterClass.Order> responseObserver) {
        Log.d(TAG," getOrderByUnary request = " + request.getValue());
        OrderManagementOuterClass.Order order = OrderManagementOuterClass
                .Order.newBuilder().setId(request.getValue() +"-1").build();
        responseObserver.onNext(order);
//        order = OrderManagementOuterClass
//                .Order.newBuilder().setId(request.getValue() +"-2").build();
//        responseObserver.onNext(order);
        responseObserver.onCompleted();
    }

    @Override
    public void getOrderByServerStream(StringValue request, StreamObserver<OrderManagementOuterClass.Order> responseObserver) {
        Log.d(TAG," getOrderByServerStream request = " + request.getValue());
        OrderManagementOuterClass.Order order = OrderManagementOuterClass
                .Order.newBuilder().setId(request.getValue() +"-1").build();
        responseObserver.onNext(order);
        order = OrderManagementOuterClass
                .Order.newBuilder().setId(request.getValue() +"-2").build();
        responseObserver.onNext(order);
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<StringValue> getOrderByClientStream(StreamObserver<OrderManagementOuterClass.Order> responseObserver) {
        return new StreamObserver<StringValue>() {
            @Override
            public void onNext(StringValue value) {
                Log.d(TAG," getOrderByClientStream onNext request = " + value);
            }

            @Override
            public void onError(Throwable t) {
                Log.d(TAG," getOrderByClientStream onError = " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                Log.d(TAG," getOrderByClientStream onCompleted");
                responseObserver.onNext(OrderManagementOuterClass
                        .Order.newBuilder().setId("1").build());
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<StringValue> getOrderByTwoWayStream(StreamObserver<OrderManagementOuterClass.Order> responseObserver) {
        return new StreamObserver<StringValue>() {
            @Override
            public void onNext(StringValue value) {
                Log.d(TAG," getOrderByTwoWayStream onNext request = " + value);
                responseObserver.onNext(OrderManagementOuterClass
                        .Order.newBuilder().setId(value.getValue() + "-1").build());
            }

            @Override
            public void onError(Throwable t) {
                Log.d(TAG," getOrderByTwoWayStream onError = " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                Log.d(TAG," getOrderByTwoWayStream onCompleted");
                responseObserver.onCompleted();
            }
        };
    }
}
