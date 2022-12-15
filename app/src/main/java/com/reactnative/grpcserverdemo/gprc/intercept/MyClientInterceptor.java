package com.reactnative.grpcserverdemo.gprc.intercept;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.MethodDescriptor;

/**
 * @Author qitao
 * @Date 2022/12/14 17:43
 */
public class MyClientInterceptor implements ClientInterceptor {

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
        return null;
    }
}
