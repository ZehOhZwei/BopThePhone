package com.example.bopthephone.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.bopthephone.R;
import com.example.bopthephone.services.SocketService;
import com.example.bopthephone.socketResources.Message;

public class MainActivity extends AppCompatActivity {

    public static int highscore;

    private SocketService socketService;

    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            socketService = ((SocketService.SocketBinder) service).getService();
            if (!socketService.isConnected()) {
                socketService.open();
            }
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
        startService(new Intent(this, SocketService.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(mConnection);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindService(new Intent(this, SocketService.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        long time1 = System.currentTimeMillis();
        socketService.sendMessage(new Message("click", null), response -> {
            if (response == null) {
                Toast.makeText(getApplicationContext(), "Error while sending", Toast.LENGTH_SHORT).show();
            } else {
                long time2 = System.currentTimeMillis();
                long time = time2 - time1;
                Toast.makeText(getApplicationContext(), time + "ms", Toast.LENGTH_SHORT).show();
            }
        });
    }
}




