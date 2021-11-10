package com.example.bopthephone;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.bopthephone.socketHandler.Message;

public class MainActivity extends AppCompatActivity {

    public static int highscore;

    private SocketService socketService;

    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            socketService = ((SocketService.SocketBinder) service).getService();
            socketService.open();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            socketService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindService(new Intent(this, SocketService.class), mConnection, Context.BIND_AUTO_CREATE);

    }



    public void gameClick(View view) {
        Intent showGameView = new Intent(MainActivity.this, GameActivity.class);
        startActivity(showGameView);
    }

    public void scoreboardClick(View view) {
        Intent showScoreboardView = new Intent(MainActivity.this, ScoreboardActivity.class);
        startActivity(showScoreboardView);
    }

    public void settingsClick(View view) {
        Intent showSettingsView = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(showSettingsView);
    }

    public void testClick(View view) {
        socketService.sendMessage(new Message("click", null));
    }
}




