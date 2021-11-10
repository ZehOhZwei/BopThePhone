package com.example.bopthephone;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.bopthephone.socketHandler.Client;

import java.io.IOException;

public class SocketService extends Service {

    Client client;

    public SocketService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            client = new Client("127.0.0.1", 4666);
            Thread t = new Thread(client);
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Error", "Critical Failure");
        }
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
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