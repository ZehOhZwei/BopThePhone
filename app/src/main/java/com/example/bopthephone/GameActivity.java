package com.example.bopthephone;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    //Dies ist meine Branch, es gibt viele wie sie, aber diese ist meine

    SensorManager sensorManager;
    Sensor gyroSensor;
    Sensor accelSensor;
    float threshold = 2f;

    Random random = new Random();

    TextView taskText;
    TextView scoreText;
    Button startGameButton;
    Button tapItButton;
    Button twistItButton;
    Button pullItButton;
    ProgressBar countDownBar;

    final String TAP = "Tap it!";
    final String TWIST = "Twist it!";
    final String PULL = "Pull it!";

    String currentTask;
    boolean cont = false;
    boolean gameOver = false;
    int score = 0;
    int cd = 3000;
    int interval = 100;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        taskText = findViewById(R.id.TaskText);
        scoreText = findViewById(R.id.ScoreText);
        currentTask = TAP;
        scoreText.setText(score + "");
        startGameButton = findViewById(R.id.StartGameButton);
        tapItButton = findViewById(R.id.TapItButton);
        twistItButton = findViewById(R.id.TwistItButton);
        pullItButton = findViewById(R.id.PullItButton);
        countDownBar = findViewById(R.id.CountDownBar);

    }


    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(gyroListener, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(accelListener, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onStop() {
        super.onStop();
        sensorManager.unregisterListener(gyroListener);
        sensorManager.unregisterListener(accelListener);
    }

    public void startGameClick(View view){
       startGameButton.setVisibility(View.INVISIBLE);
       tapItButton.setVisibility(View.VISIBLE);
       twistItButton.setVisibility(View.VISIBLE);
       pullItButton.setVisibility(View.VISIBLE);
       i = cd;
       score = 0;
       gameRound(cd);
    }

    public void gameRound(int countdown){
        CountDownTimer countDownTimer = new CountDownTimer(countdown, interval) {
            @Override
            public void onTick(long l) {
                i = i - interval;
                taskText.setText(currentTask);
                scoreText.setText(score + "");
                countDownBar.setProgress(i);
                if(cont) {
                    cont = false;
                    score++;
                    currentTask = chooseNextTask(random.nextInt(3));
                    i = countdown - (countdown/100);
                    countDownBar.setMax(i);
                    gameRound(countdown - (countdown/100));
                    cancel();
                }
            }

            @Override
            public void onFinish() {
                taskText.setText("Game Over!");
                startGameButton.setVisibility(View.VISIBLE);
                tapItButton.setVisibility(View.INVISIBLE);
                twistItButton.setVisibility(View.INVISIBLE);
                pullItButton.setVisibility(View.INVISIBLE);
                startGameButton.setText("Play Again?");
            }
        }.start();
    }

    public void tapItClick(View view){
        if(currentTask == TAP){
            cont = true;
        }
    }

    public void twistItClick(View view){

        if(currentTask == TWIST){
            cont = true;
        }
    }

    public void pullItClick(View view){

        if(currentTask == PULL){
            cont = true;
        }
    }

    public SensorEventListener gyroListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) {
        }

        public void onSensorChanged(SensorEvent event) {
            if(event.values[1] >= threshold){
                if(currentTask == TWIST){
                    cont = true;
                }
            }
        }
    };

    public SensorEventListener accelListener = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int acc) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if((event.values[1] - 9.81f) >= threshold){
                if(currentTask == PULL){
                    cont = true;
                }
            }
        }
    };

    private String chooseNextTask(int task) {
        switch (task) {
            case 0:
                return TAP;

            case 1:
                return TWIST;

            case 2:
                return PULL;
        }
        return TAP;

    }

}