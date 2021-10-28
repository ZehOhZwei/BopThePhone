package com.example.bopthephone;

import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;
import java.util.Spliterator;

public class GameActivity extends AppCompatActivity {

    //Dies ist meine Branch, es gibt viele wie sie, aber diese ist meine

    SensorManager sensorManager;
    Sensor gyroSensor;
    Sensor accelSensor;
    float threshold = 2f;

    android.os.Handler gameHandler;
    Random random = new Random();

    TextView text;

    String TAP;
    String TWIST;
    String PULL;

    long countdown = 3000;
    boolean cont = false;
    boolean gameOver = false;
    int score = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        gameHandler = new android.os.Handler();
        gameHandler.postDelayed(gameThread, 0);

        text = findViewById(R.id.gameText);

        TAP = "Tap It!";
        TWIST = "Twist It!";
        PULL = "Pull It!";

        Button tapButton = findViewById(R.id.tapButton);


        tapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(text.toString() == TAP){
                    cont = true;
                }
            }
        });
    }

    private Runnable gameThread = new Runnable() {
        @Override
        public void run() {
            //The Text is randomly set to one of three options. The countdown is added behind for debugging purposes
            text.setText(chooseNextTask(random.nextInt(3)) + " " + countdown);
            if(cont){
                cont = false;
                score++;

            }


            gameHandler.postDelayed(this, countdown);
        }
    };

    public void onResume() {
        super.onResume();
        sensorManager.registerListener(gyroListener, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(accelListener, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onStop() {
        super.onStop();
        sensorManager.unregisterListener(gyroListener);
        sensorManager.unregisterListener(accelListener);
    }

    public SensorEventListener gyroListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) {
        }

        public void onSensorChanged(SensorEvent event) {
            if(event.values[1] >= threshold){
                text.setText("GyroSuccess");
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
                text.setText("AccelSuccess");
            }
        }
    };

    private String chooseNextTask(int task){
        switch (task) {
            case 0: return TAP;

            case 1: return TWIST;

            case 2: return PULL;
        }
        return TAP;
    }
}