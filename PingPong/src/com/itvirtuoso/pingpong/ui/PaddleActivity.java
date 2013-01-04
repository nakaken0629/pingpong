package com.itvirtuoso.pingpong.ui;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MotionEvent;
import android.widget.TextView;

import com.itvirtuoso.pingpong.R;
import com.itvirtuoso.pingpong.controller.GameController;
import com.itvirtuoso.pingpong.controller.GameControllerEvent;
import com.itvirtuoso.pingpong.controller.GameControllerListener;
import com.itvirtuoso.pingpong.controller.GameMode;

public class PaddleActivity extends Activity implements SensorEventListener,
        GameControllerListener {
    private static final boolean DEBUG = true;
    private Handler mHandler;
    private TextView debugView;

    private GameController observer;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private float maxAcceleration = (float) (9.8 * 2);
    private float[] acceleration = new float[3];
    private boolean isSwing = false;
    private SoundPool soundPool;
    private int kaId;
    private int koId;
    private int whistleId;

    protected void playSound(int id) {
        this.soundPool.play(id, 1.0F, 1.0F, 0, 0, 1.0f);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paddle);
        this.mHandler = new Handler();
        this.debugView = (TextView) findViewById(R.id.debugView);

        this.mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensorList = this.mSensorManager
                .getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (sensorList.size() > 0) {
            this.mAccelerometer = sensorList.get(0);
        }

        this.observer = new GameController();
        observer.newGame(this);
        WallListener wallListener = new WallListener();
        observer.joinGame(wallListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_paddle, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        /* 加速度センサーの登録 */
        if (this.mSensorManager != null) {
            mSensorManager.registerListener(this, mAccelerometer,
                    SensorManager.SENSOR_DELAY_UI);
        }
        /* サウンドの登録 */
        this.soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        this.kaId = this.soundPool.load(this, R.raw.ka, 1);
        this.koId = this.soundPool.load(this, R.raw.ko, 1);
        this.whistleId = this.soundPool.load(this, R.raw.whistle, 1);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.soundPool.release();
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        /* nop */
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (DEBUG) {
            if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) {
                return;
            }
            if (DEBUG) {
                for (int i = 0; i < this.acceleration.length; i++) {
                    this.acceleration[i] = event.values[i];
                }
            }
            if (event.values[2] > this.maxAcceleration) {
                this.isSwing = true;
                if (this.observer.getGameMode() == GameMode.WAIT) {
                    this.observer.swing(this, 500);
                }
            } else {
                this.isSwing = false;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!DEBUG) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                this.isSwing = true;
                if (this.observer.getGameMode() == GameMode.WAIT) {
                    this.observer.swing(this, 500);
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                this.isSwing = false;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void setObserver(PaddleObserver observer) {
        /* nop */
    }

    @Override
    public void onHit(GameControllerEvent event) {
        playSound(this.kaId);
    }

    @Override
    public void onFirstBound(GameControllerEvent event) {
        playSound(this.koId);
    }

    @Override
    public void onSecondBound(GameControllerEvent event) {
        playSound(this.koId);
    }

    @Override
    public void onHittable(GameControllerEvent event) {
        if (!event.isHitter()) {
            return;
        }
        if (DEBUG) {
            this.mHandler.post(new Runnable() {

                @Override
                public void run() {
                    PaddleActivity self = PaddleActivity.this;
                    self.debugView.setText(String.format(
                            "(x, y, z)=(%f, %f, %f)", self.acceleration[0],
                            self.acceleration[1], self.acceleration[2]));
                }
            });
        }
        if (this.isSwing) {
            this.observer.swing(this, 500);
        }
    }

    @Override
    public void onGoOutOfBounds(GameControllerEvent event) {
        playSound(this.whistleId);
    }
}
