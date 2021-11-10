package com.example.bopthephone;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.example.bopthephone.socketHandler.Client;

import java.io.IOException;

public class SocketService extends Service {

    private Client client;

    public SocketService() {

    }

    public void open() {
        try {
            client = new Client("192.168.2.101", 4666);
            Log.d("asdfgh","Success");
            Thread t = new Thread(client);
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Error", "Critical Failure");
        }
    }

    public class SocketBinder extends Binder {
        SocketService getService() {
            return SocketService.this;
        }
    }

    public String sendMessage(String msg) throws IOException {
        return client.sendMessage(msg);
    }

    public void testClick(View view) {
        try {
            sendMessage("test444");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}