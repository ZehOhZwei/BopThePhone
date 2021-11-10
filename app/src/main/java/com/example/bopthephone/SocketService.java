package com.example.bopthephone;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.example.bopthephone.socketHandler.Client;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketService extends Service {

    private Client client;
    ExecutorService executorService = Executors.newFixedThreadPool(4);

    public SocketService() {

    }

    public void open() {
        System.out.println("Trying to open socket");
        try {
            client = new Client("192.168.2.101", 4666, executorService);
            client.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("asdfgh", "Success");
    }

    public class SocketBinder extends Binder {
        SocketService getService() {
            return SocketService.this;
        }
    }

    public void sendMessage(String msg) throws IOException {
        client.sendMessage(msg);
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
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}