package com.example.bopthephone;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.example.bopthephone.socketHandler.Client;
import com.example.bopthephone.socketHandler.Message;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketService extends Service {

    private Client client;
    ExecutorService executorService = Executors.newFixedThreadPool(4);

    public SocketService() {

    }

    public void open() {
        client = new Client("192.168.2.101", 4666, executorService);
        client.connect();
        sendMessage(new Message("connect", "MyName"));
    }

    public class SocketBinder extends Binder {
        SocketService getService() {
            return SocketService.this;
        }
    }

    public void sendMessage(Message message) {
        client.sendMessage(message);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("onStartCommand");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new SocketBinder();
    }

    @Override
    public void onDestroy() {
        client.close();
    }
}