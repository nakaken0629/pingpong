package com.itvirtuoso.pingpong.ui;

import android.app.Activity;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;

import com.itvirtuoso.pingpong.R;
import com.itvirtuoso.pingpong.controller.AutoGameController;
import com.itvirtuoso.pingpong.controller.GameController;
import com.itvirtuoso.pingpong.controller.PaddleEvent;
import com.itvirtuoso.pingpong.controller.PaddleListener;
import com.itvirtuoso.pingpong.model.Paddle;

public class PaddleActivity extends Activity implements PaddleListener {
    private Paddle paddle;

    protected void beep(int tone) {
        /* nop */
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paddle);

        GameController controller = new AutoGameController();
        this.paddle = controller.newGame(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_paddle, menu);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            this.paddle.swing();
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onHit(PaddleEvent event) {
        Log.d(this.getClass().getName(), "onHit");
        beep(ToneGenerator.TONE_PROP_ACK);
    }

    @Override
    public void onFirstBound(PaddleEvent event) {
        Log.d(this.getClass().getName(), "onFirstBound");
        beep(ToneGenerator.TONE_PROP_BEEP);
    }

    @Override
    public void onSecondBound(PaddleEvent event) {
        Log.d(this.getClass().getName(), "onSecondBound");
        beep(ToneGenerator.TONE_PROP_BEEP);
    }

    @Override
    public void onHittable(PaddleEvent event) {
        Log.d(this.getClass().getName(), "onHittable");
        /* nop */
    }

    @Override
    public void onGoOutOfBounds(PaddleEvent event) {
        Log.d(this.getClass().getName(), "onGoOutOfBounds");
        beep(ToneGenerator.TONE_PROP_NACK);
    }
}
