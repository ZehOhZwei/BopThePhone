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
    private boolean isConnected = false;

    public SocketService() {

    }

    public void open() {
        client = new Client("mchdlp.de", 4666, executorService);
        client.connect(s -> {
            if (s.equals("success")) {
                System.out.println("Connected Established");
                this.isConnected = true;
                sendMessage(new Message("connect","myName"));
            }
        });
        sendMessage(new Message("connect", "MyName"));
    }

    public class SocketBinder extends Binder {
        SocketService getService() {
            return SocketService.this;
        }
    }

    public void sendMessage(Message message) {
        if(!isConnected) return;
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