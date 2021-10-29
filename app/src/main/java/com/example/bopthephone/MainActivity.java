package com.example.bopthephone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void playClick(View view)
    {
        Intent showPlayView = new Intent(MainActivity.this , PlayActivity.class);
        startActivity(showPlayView);
    }

    public void scoreboardClick(View view)
    {
        Intent showScoreboardView = new Intent(MainActivity.this , ScoreboardActivity.class);
        startActivity(showScoreboardView);
    }

    public void settingsClick(View view)
    {
        Intent showSettingsView = new Intent(MainActivity.this , SettingsActivity.class);
        startActivity(showSettingsView);
    }
}




