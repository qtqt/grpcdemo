package com.reactnative.grpcserverdemo.gprc.intercept;

import android.util.Log;

import androidx.annotation.Nullable;

import com.reactnative.grpcserverdemo.gprc.tracer.CustomClientStreamTracerFactory;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ClientStreamTracer;
import io.grpc.ForwardingClientCall;
import io.grpc.ForwardingClientCallListener;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.ServerTransportFilter;
import io.grpc.Status;

/**
 * @Author qitao
 * @Date 2022/12/14 17:43
 */
public class MyClientInterceptor implements ClientInterceptor {
    private final String TAG = "MyClientInterceptor";
    private static final Metadata.Key<String> TOKEN = Metadata.Key.of("token", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
        Log.d(TAG,"channel  = " + next);
        callOptions = callOptions.withStreamTracerFactory(new CustomClientStreamTracerFactory<>(method, callOptions, next));


        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)) {

            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                // 客户端传递链路追中数据，将数据放到headers中
                Log.d(TAG," interceptCall start start : currentThreadName = "+Thread.currentThread().getName()+"start = " + headers );
                headers.put(TOKEN, "A2D05E5ED2414B1F8C6AEB19F40EF77C");
                try{
                    super.start(new ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT>(responseListener) {
                        @Override
                        public void onHeaders(Metadata headers) {
                            // 服务端传递回来的header
                            Log.d(TAG," interceptCall : onHeaders = " + headers);
                            super.onHeaders(headers);
                        }

                        @Override
                        public void onClose(Status status, Metadata trailers) {
                            Log.d(TAG," interceptCall : onClose status = " + status ) ;
                            super.onClose(status, trailers);
                        }

                        @Override
                        public void onMessage(RespT message) {
                            Log.d(TAG," interceptCall : onMessage message = " + message);
                            super.onMessage(message);
                        }

                        @Override
                        public void onReady() {
                            Log.d(TAG," interceptCall : onReady");
                            super.onReady();
                        }
                    }, headers);
                }catch (Exception e){
                    Log.e(TAG," interceptCall : err = " + e.getMessage());
                }
            }

            @Override
            public void sendMessage(ReqT message) {
                Log.d(TAG," interceptCall : sendMessage start currentThreadName = "+Thread.currentThread().getName()+ "message = " + message );
                super.sendMessage(message);
            }

            @Override
            public void cancel(@Nullable String message, @Nullable Throwable cause) {
                Log.d(TAG," interceptCall cancel : currentThreadName = "+Thread.currentThread().getName()+"  message = " + message + " " + cause.getMessage());
                super.cancel(message, cause);
            }


            @Override
            public void request(int numMessages) {
                Log.d(TAG," interceptCall request : currentThreadName = "+Thread.currentThread().getName()+" numMessages = " + numMessages );
                super.request(numMessages);
            }

            @Override
            public void halfClose() {
                Log.d(TAG," interceptCall halfClose ");
                super.halfClose();
            }
        };

    }


    class CustomClientStreamTracerFactory <ReqT, RespT> extends ClientStreamTracer.Factory {

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
    }

    class CustomClientStreamTracer<ReqT, RespT> extends ClientStreamTracer {

        public CustomClientStreamTracer(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next, StreamInfo info, Metadata headers) {
//            info.toString()
//            Log.d(TAG,"Method:" + method + " Next:" + next.authority() + " Header: " + headers.toString() +" info = " + info);
        }

        @Override
        public void outboundHeaders() {//发送 header 给 Server 端
            super.outboundHeaders();
            Log.d(TAG,"outboundHeaders ");
        }

        @Override
        public void outboundMessage(int seqNo) {//发送 message 给 Server 端
            super.outboundMessage(seqNo);
            Log.d(TAG,"outboundMessage  seqNo = " + seqNo);
        }

        @Override
        public void inboundHeaders() {//接收 Server 端返回的 headers
            super.inboundHeaders();
            Log.d(TAG,"inboundHeaders");
        }

        @Override
        public void inboundMessage(int seqNo) {//接收 Server 端返回的 message
            super.inboundMessage(seqNo);
            Log.d(TAG,"inboundMessage seqNo = " + seqNo);
        }

        @Override
        public void inboundTrailers(Metadata trailers) {//接收 Server 端返回的 trailers
            super.inboundTrailers(trailers);
            Log.d(TAG,"inboundTrailers trailers = " + trailers);
        }

        @Override
        public void streamClosed(Status status) {
            super.streamClosed(status);
            Log.d(TAG,"streamClosed status = " + status);
        }
    }
}
