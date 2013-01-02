package com.itvirtuoso.pingpong.model;

import com.itvirtuoso.pingpong.controller.GameController;
import com.itvirtuoso.pingpong.controller.PaddleEvent;
import com.itvirtuoso.pingpong.controller.PaddleListener;

public class Paddle implements PaddleListener {
    private GameController controller;
    private PaddleListener listener;
    private boolean isHittable = false;
    
    public Paddle(GameController controller, PaddleListener listener) {
        this.controller = controller;
        this.listener = listener;
    }
    
    public void setup() {
        this.isHittable = true;
    }
    
    public void swing() {
        if(!this.isHittable) {
            return;
        }
        this.isHittable = false;
        if(!this.controller.isPlaying()) {
            this.controller.serve(this, 500);
        } else {
            this.controller.receive(this, 500);
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
        if(event.isHitter()) {
            this.isHittable = true;
        }
        this.listener.onHittable(event);
    }
    
    @Override
    public void onGoOutOfBounds(PaddleEvent event) {
        this.listener.onGoOutOfBounds(event);
        this.isHittable = true;
    }
}
