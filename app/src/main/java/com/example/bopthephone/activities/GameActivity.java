package com.example.bopthephone.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bopthephone.R;
import com.example.bopthephone.services.SocketService;
import com.example.bopthephone.socketResources.Message;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    //Dies ist meine Branch, es gibt viele wie sie, aber diese ist meine

    private SensorManager sensorManager;
    private Sensor gyroSensor;
    private Sensor accelSensor;

    private final float gyroThreshold = 2f;
    private final float accelThreshold = 1.5f;
    private final Random random = new Random();

    private TextView taskText;
    private TextView scoreText;
    private Button startGameButton;
    private Button tapItButton;
    private Button twistItButton;
    private Button pullItButton;
    private ProgressBar countDownBar;

    private final String TAP = "Tap it!";
    private final String TWIST = "Twist it!";
    private final String PULL = "Pull it!";

    private String currentTask;
    private boolean cont = false;
    private int score = 0;
    private int cd = 3000;
    private final int interval = 100;
    private int progessBarValue;

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
        setContentView(R.layout.activity_game);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        taskText = findViewById(R.id.TaskText);
        scoreText = findViewById(R.id.ScoreText);
        startGameButton = findViewById(R.id.StartGameButton);
        tapItButton = findViewById(R.id.TapItButton);
        twistItButton = findViewById(R.id.TwistItButton);
        pullItButton = findViewById(R.id.PullItButton);
        countDownBar = findViewById(R.id.CountDownBar);
        currentTask = chooseNextTask(random.nextInt(3));
        scoreText.setText(score + "");

        bindService(new Intent(this, SocketService.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(mConnection);
    }


    @Override
    public void onResume() {
        super.onResume();
        bindService(new Intent(this, SocketService.class), mConnection, Context.BIND_AUTO_CREATE);
        sensorManager.registerListener(gyroListener, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(accelListener, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
        sensorManager.unregisterListener(gyroListener);
        sensorManager.unregisterListener(accelListener);
    }

    public void startGameClick(View view) {
        startGameButton.setVisibility(View.INVISIBLE);
        tapItButton.setVisibility(View.VISIBLE);
        //twistItButton.setVisibility(View.VISIBLE);
        //pullItButton.setVisibility(View.VISIBLE);
        countDownBar.setVisibility(View.VISIBLE);
        progessBarValue = cd;
        score = 0;
        socketService.sendMessage(new Message("start-game", null), response -> {

        });
        gameRound(cd);
    }

    public void gameRound(int countdown) {
        CountDownTimer countDownTimer = new CountDownTimer(countdown, interval) {
            @Override
            public void onTick(long l) {
                progessBarValue -= interval;
                taskText.setText(currentTask);
                scoreText.setText("score = " + score);
                countDownBar.setProgress(progessBarValue);
                if (cont) {
                    cont = false;
                    score++;
                    currentTask = chooseNextTask(random.nextInt(3));
                    progessBarValue = countdown - (countdown / 100);
                    countDownBar.setMax(progessBarValue);
                    gameRound(countdown - (countdown / 100));
                    cancel();
                }
            }

            @Override
            public void onFinish() {
                if (score > MainActivity.highscore) {
                    MainActivity.highscore = score;
                }
                taskText.setText("Game Over!");
                startGameButton.setVisibility(View.VISIBLE);
                tapItButton.setVisibility(View.INVISIBLE);
                twistItButton.setVisibility(View.INVISIBLE);
                pullItButton.setVisibility(View.INVISIBLE);
                countDownBar.setVisibility(View.INVISIBLE);
                startGameButton.setText("Play Again?");
            }
        }.start();
    }

    public void tapItClick(View view) {
        if (currentTask == TAP) {
            cont = true;
        }
    }

    public void twistItClick(View view) {

        if (currentTask == TWIST) {
            cont = true;
        }
    }

    public void pullItClick(View view) {

        if (currentTask == PULL) {
            cont = true;
        }
    }

    public SensorEventListener gyroListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) {
        }

        public void onSensorChanged(SensorEvent event) {
            if (event.values[1] >= gyroThreshold) {
                if (currentTask == TWIST) {
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
            if ((event.values[1] - 9.81f) >= accelThreshold) {
                if (currentTask == PULL) {
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

            default:
                return TAP;
        }
    }
}