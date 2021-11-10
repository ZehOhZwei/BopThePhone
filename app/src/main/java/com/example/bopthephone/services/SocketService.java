package com.example.bopthephone.services;

import static android.os.Handler.createAsync;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import com.example.bopthephone.socketResources.Client;
import com.example.bopthephone.socketResources.Message;
import com.example.bopthephone.socketResources.SocketCallback;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketService extends Service {

    private Client client;
    ExecutorService executorService = Executors.newFixedThreadPool(4);
    Handler mainThreadHandler = createAsync(Looper.getMainLooper());

    private boolean connected = false;

    public SocketService() {

    }

    public void open() {
        client = new Client("mchdlp.de", 4666, executorService, mainThreadHandler);
        client.connect(
                s -> {
            if (s.data) {
                System.out.println("Connected Established");
                this.connected = true;
                sendMessage(new Message("connect", "myName"), response -> {
                    // TODO
                    System.out.println(response);
                });
            } else {
                // TODO what to do when connection fails
            }
        });
    }

    public boolean isConnected() {
        return connected;
    }

    public class SocketBinder extends Binder {
        public SocketService getService() {
            return SocketService.this;
        }
    }

    public void sendMessage(Message message, SocketCallback<Message> callback) {
        client.sendMessage(message, callback);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new SocketBinder();
    }

    @Override
    public void onDestroy() {
        client.close(response -> {

        });
    }
}