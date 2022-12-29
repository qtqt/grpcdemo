package com.reactnative.grpcserverdemo.gprc.tracer;


import android.util.Log;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientStreamTracer;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;

/**
 * @Author qitao
 * @Date 2022/12/24 15:03
 */
public class CustomClientStreamTracerFactory <ReqT, RespT> extends ClientStreamTracer.Factory {
    private static final String TAG = "StreamTracerFactory";

    private final MethodDescriptor<ReqT, RespT> method;
    private final CallOptions callOptions;
    private final Channel next;

    public CustomClientStreamTracerFactory(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
        this.method = method;
        this.callOptions = callOptions;
        this.next = next;

    }

    @Override
    public ClientStreamTracer newClientStreamTracer(ClientStreamTracer.StreamInfo info, Metadata headers) {
        return new CustomClientStreamTracer<>(method, callOptions, next, info, headers);
    }

    class CustomClientStreamTracer<ReqT, RespT> extends ClientStreamTracer {

        public CustomClientStreamTracer(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next, StreamInfo info, Metadata headers) {
            Log.d(TAG,"Method:" + method.getFullMethodName() + " Next:" + next.authority() + " Header: " + headers.toString() +" info = " + info);
        }
    }

}
