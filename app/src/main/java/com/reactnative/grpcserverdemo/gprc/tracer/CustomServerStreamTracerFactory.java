package com.reactnative.grpcserverdemo.gprc.tracer;

import android.util.Log;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientStreamTracer;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.ServerStreamTracer;
import io.grpc.Status;

/**
 * @Author qitao
 * @Date 2022/12/26 11:41
 */
public class CustomServerStreamTracerFactory extends ServerStreamTracer.Factory {
    private final String TAG = "ServerStreamTracer";

    @Override
    public ServerStreamTracer newServerStreamTracer(String fullMethodName, Metadata headers) {
        return new CustomServerStreamTracer(fullMethodName, headers);
    }

    class CustomServerStreamTracer<ReqT, RespT> extends ServerStreamTracer {
        private final String fullMethodName;

        private final Metadata headers;
        public CustomServerStreamTracer(String fullMethodName, Metadata headers) {
            this.fullMethodName = fullMethodName;
            this.headers = headers;
        }


        @Override
        public void outboundMessage(int seqNo) {//发送 message 给 Server 端
            super.outboundMessage(seqNo);
            Log.d(TAG,"outboundMessage  seqNo = " + seqNo);
        }


        @Override
        public void inboundMessage(int seqNo) {//接收 Server 端返回的 message
            super.inboundMessage(seqNo);
            Log.d(TAG,"inboundMessage seqNo = " + seqNo);
        }


        @Override
        public void streamClosed(Status status) {
            super.streamClosed(status);
            Log.d(TAG,"streamClosed status = " + status);
        }
    }
}

