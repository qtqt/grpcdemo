package com.reactnative.grpcserverdemo.gprc.intercept;

import android.util.Log;

import io.grpc.ForwardingServerCall;
import io.grpc.ForwardingServerCallListener;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;

/**
 * @Author qitao
 * @Date 2022/12/14 17:27
 */
public class MyServer2Interceptor implements ServerInterceptor {
    private final String TAG = "MyServer2Interceptor";
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
        Log.d(TAG," interceptCall : headers = " + headers);

        ServerCall.Listener<ReqT> delegate = next.startCall(new ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(call) {
            @Override
            public void sendHeaders(Metadata headers) {
                Log.d(TAG, " interceptCall response : sendHeaders = " + headers);
                super.sendHeaders(headers);
            }

            @Override
            public void sendMessage(RespT message) {
                Log.d(TAG, " interceptCall response : sendMessage = " + headers);
                super.sendMessage(message);
            }

        }, headers);


        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(delegate) {
            @Override
            public void onMessage(ReqT message) {
                super.onMessage(message);
                Log.d(TAG," interceptCall request : onMessage message = " + message);
            }

            @Override
            public void onReady() {
                super.onReady();
                Log.d(TAG," interceptCall request : onReady");
            }

            @Override
            public void onCancel() {
                super.onCancel();
                Log.d(TAG," interceptCall request : onCancel");
            }

            @Override
            public void onComplete() {
                super.onComplete();
                Log.d(TAG," interceptCall request : onComplete");
            }
        };
    }
}
