package com.reactnative.grpcserverdemo.gprc.intercept;

import android.util.Log;

import androidx.annotation.Nullable;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.ForwardingClientCallListener;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.Status;

/**
 * @Author qitao
 * @Date 2022/12/14 17:43
 */
public class MyClient2Interceptor implements ClientInterceptor {
    private final String TAG = "MyClient2Interceptor";
    private static final Metadata.Key<String> TOKEN = Metadata.Key.of("token", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {

        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)) {

            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                // 客户端传递链路追中数据，将数据放到headers中
                Log.d(TAG," interceptCall : currentThreadName = "+Thread.currentThread().getName()+"start = " + headers );
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
                            Log.d(TAG," interceptCall : onClose status = " + status);
                            super.onClose(status, trailers);
                        }

                        @Override
                        public void onMessage(RespT message) {
                            Log.d(TAG," interceptCall : onMessage message = " + message);
                            super.onMessage(message);
                        }



                    }, headers);
                }catch (Exception e){
                    Log.d(TAG," interceptCall : err = " + e.getMessage());
                }

            }

            @Override
            public void sendMessage(ReqT message) {
                Log.d(TAG," interceptCall : sendMessage currentThreadName = "+Thread.currentThread().getName()+ "message = " + message );
                super.sendMessage(message);
            }

            @Override
            public void cancel(@Nullable String message, @Nullable Throwable cause) {
                Log.d(TAG," interceptCall : currentThreadName = "+Thread.currentThread().getName()+" cancel  message = " + message + " " + cause.getMessage());
                super.cancel(message, cause);
            }
        };

    }
}
