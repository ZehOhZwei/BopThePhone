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
import android.widget.TextView;

import java.util.Random;

public class GameActivity extends AppCompatActivity
{

    SensorManager sensorManager;
    Sensor gyroSensor;
    Sensor accelSensor;
    float threshold = 2f;

    Random random = new Random();

    TextView text;

    String TAP = "Tap it!";
    String TWIST = "Twist it!";
    String PULL = "Pull it!";

    long countdown = 3000;
    boolean cont = false;
    boolean gameOver = false;
    int score = 0;

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        text = findViewById(R.id.gameText);
        text.setText(TAP);

        Button tapButton = findViewById(R.id.tapButton);

        tapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(text.toString() == TAP){
                    cont = true;
                }
            }
        });

        CountDownTimer countDownTimer = new CountDownTimer(countdown, 250) {
            @Override
            public void onTick(long l) {
                if(cont) {
                    cont = false;
                    score++;
                    text.setText(chooseNextTask(random.nextInt(3)) + "" + score);
                    //countDownTimer.start();
                    cancel();
                }

                text.setText(i);
                i++;
            }

            @Override
            public void onFinish() {

            }
        };
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

    public SensorEventListener gyroListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) {
        }

        public void onSensorChanged(SensorEvent event) {
            if(event.values[1] >= threshold){
                ;
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
                ;
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