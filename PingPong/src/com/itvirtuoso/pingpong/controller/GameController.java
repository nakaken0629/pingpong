package com.itvirtuoso.pingpong.controller;

import java.util.ArrayList;

import com.itvirtuoso.pingpong.model.Paddle;

public class GameController {
    private Object paddlesLock = new Object();
    private ArrayList<Paddle> paddles = new ArrayList<Paddle>();

    public Paddle newGame(PaddleListener listener) {
        Paddle paddle = new Paddle(this, listener);
        synchronized (this.paddlesLock) {
            paddles.add(paddle);
        }
        return paddle;
    }

    public void swing(Paddle paddle) {
        synchronized (this.paddlesLock) {
            for(Paddle paddle2 : this.paddles) {
                paddle2.onHit();
            }
            for(Paddle paddle2 : this.paddles) {
                paddle2.onBound();
            }
            for(Paddle paddle2 : this.paddles) {
                paddle2.onBound();
            }
            for(Paddle paddle2 : this.paddles) {
                paddle2.onGoOutOfBounds();
            }
        }
    }
}
