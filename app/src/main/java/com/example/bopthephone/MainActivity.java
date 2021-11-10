package com.example.bopthephone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity
{

    public static int highscore;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }


    public void gameClick(View view)
    {
        Intent showGameView = new Intent(MainActivity.this , GameActivity.class);
        startActivity(showGameView);
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

    public void testClick(View view)
    {

    }
}




