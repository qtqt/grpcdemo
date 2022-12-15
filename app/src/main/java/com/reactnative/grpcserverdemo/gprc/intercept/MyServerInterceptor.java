package com.reactnative.grpcserverdemo.gprc.intercept;

import android.util.Log;

import io.grpc.ForwardingServerCall;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;

/**
 * @Author qitao
 * @Date 2022/12/14 17:27
 */
public class MyServerInterceptor implements ServerInterceptor {
    private final String TAG = "MyServerInterceptor";
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
        Log.d(TAG," interceptCall : headers = " + headers);
        return next.startCall(new ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(call){
            @Override
            public void sendHeaders(Metadata headers) {
                Log.d(TAG," interceptCall : sendHeaders = " + headers);
                super.sendHeaders(headers);
            }

            @Override
            public void sendMessage(RespT message) {
                Log.d(TAG," interceptCall : sendMessage = " + headers);
                super.sendMessage(message);
            }
        }, headers);
    }
}
