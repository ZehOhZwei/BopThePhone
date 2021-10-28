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

import java.util.Spliterator;

public class GameActivity extends AppCompatActivity {

    //Dies ist meine Branch, es gibt viele wie sie, aber diese ist meine

    SensorManager sensorManager;
    Sensor gyroSensor;
    Sensor accelSensor;

    TextView text;

    String TAP;
    String TWIST;
    String PULL;

    float countdown;
    boolean cont = false;
    boolean gameOver = false;
    int score = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        text = findViewById(R.id.gameText);

        TAP = "Tap It!";
        TWIST = "Twist It!";
        PULL = "Pull It!";

        Button tapButton = findViewById(R.id.tapButton);

        tapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(text.toString() == PULL){
                    cont = true;
                }
            }
        });
    }

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
            text.setText("GyroSuccess");
        }
    };

    public SensorEventListener accelListener = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int acc) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            text.setText("AccelSuccess");
        }

    };
}