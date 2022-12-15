package com.reactnative.grpcserverdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.reactnative.grpcserverdemo.gprc.OrderClient;
import com.reactnative.grpcserverdemo.gprc.OrderServer;

import java.lang.ref.ReferenceQueue;
import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class MainServerActivity extends AppCompatActivity {
    private final String TAG = "MainServerActivity";
    private Scheduler.Worker workerServer;
    private Scheduler.Worker workerClient;
    int port = 3452;
    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Scheduler scheduler = Schedulers.from(Executors.newSingleThreadScheduledExecutor());
        workerServer = scheduler.createWorker();
        workerServer.schedule(() ->{
            startServer();
        });

        scheduler = Schedulers.from(Executors.newSingleThreadScheduledExecutor());
        workerClient = scheduler.createWorker();
        client = new OrderClient("127.0.0.1",port);
        findViewById(R.id.bt_get_order_unary).setOnClickListener(v -> {
            workerClient.schedule(() ->{
                getOrderByUnary(id++ +"");
            });
        });

        findViewById(R.id.bt_get_order_server_stream).setOnClickListener(v -> {
            workerClient.schedule(() ->{
                getOrderByServerStream(id++ +"");
            });
        });

        findViewById(R.id.bt_get_order_client_stream).setOnClickListener(v -> {
            workerClient.schedule(() ->{
                getOrderByClientStream();
            });
        });

        findViewById(R.id.bt_get_order_two_way_stream).setOnClickListener(v -> {
            workerClient.schedule(() ->{
                getOrderByTwoWayStream();
            });
        });
    }

    OrderServer server;
    public void startServer(){
        server = new OrderServer(port);
        server.start();

    }

    OrderClient client;
    public void getOrderByUnary(String id){
        client.getOrderByUnary(id);

    }

    public void getOrderByServerStream(String id){
        client.getOrderByServerStream(id);
    }


    public void getOrderByClientStream(){
        client.getOrderByClientStream();
    }

    public void getOrderByTwoWayStream(){
        client.getOrderByTwoWayStream();
    }

}