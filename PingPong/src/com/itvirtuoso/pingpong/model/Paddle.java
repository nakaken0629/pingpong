package com.itvirtuoso.pingpong.model;

import java.util.EnumSet;

import com.itvirtuoso.pingpong.Motion;
import com.itvirtuoso.pingpong.controller.GameController;
import com.itvirtuoso.pingpong.controller.PaddleEvent;
import com.itvirtuoso.pingpong.controller.PaddleListener;

public class Paddle implements PaddleListener {
    private GameController controller;
    private PaddleListener listener;
    private EnumSet<Motion> motion;

    public Paddle(GameController controller, PaddleListener listener) {
        this.controller = controller;
        this.listener = listener;
        this.motion = EnumSet.of(Motion.STOP);
    }

    public void swing() {
        if(!this.controller.isPlaying()) {
            this.controller.serve(this, 100);
        } else {
            this.controller.receive(this, 100);
        }
    }
    
    @Override
    public void onHit(PaddleEvent event) {
        this.listener.onHit(event);
    }

    @Override
    public void onFirstBound(PaddleEvent event) {
        this.listener.onFirstBound(event);
    }
    
    @Override
    public void onSecondBound(PaddleEvent event) {
        this.listener.onSecondBound(event);
    }
    
    @Override
    public void onHittable(PaddleEvent event) {
        this.listener.onHittable(event);
    }
    
    @Override
    public void onGoOutOfBounds(PaddleEvent event) {
        this.listener.onGoOutOfBounds(event);
    }
}
