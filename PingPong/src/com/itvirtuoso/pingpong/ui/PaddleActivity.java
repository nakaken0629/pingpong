package com.itvirtuoso.pingpong.ui;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;

import com.itvirtuoso.pingpong.R;
import com.itvirtuoso.pingpong.controller.GameController;
import com.itvirtuoso.pingpong.controller.GameControllerEvent;
import com.itvirtuoso.pingpong.controller.GameControllerListener;

public class PaddleActivity extends Activity implements GameControllerListener {
    private PaddleObserver observer;
    private boolean isSwing = false;
    private SoundPool soundPool;
    private int kaId;
    private int koId;
    private int whistleId;

    protected void beep(int id) {
        this.soundPool.play(id, 1.0F, 1.0F, 0, 0, 1.0f);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paddle);

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
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            this.isSwing = true;
        } else if(event.getAction() == MotionEvent.ACTION_UP) {
            this.isSwing = false;
        }
        if(this.isSwing) {
            this.observer.swing(this, 500);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void setObserver(PaddleObserver observer) {
        /* nop */
    }

    @Override
    public void onHit(GameControllerEvent event) {
        beep(this.kaId);
    }

    @Override
    public void onFirstBound(GameControllerEvent event) {
        beep(this.koId);
    }

    @Override
    public void onSecondBound(GameControllerEvent event) {
        beep(this.koId);
    }

    @Override
    public void onHittable(GameControllerEvent event) {
        if(!event.isHitter()) {
            return;
        }
        if(this.isSwing) {
            this.observer.swing(this, 500);
        }
    }

    @Override
    public void onGoOutOfBounds(GameControllerEvent event) {
        beep(this.whistleId);
    }
}
