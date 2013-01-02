package com.itvirtuoso.pingpong.controller;

import com.itvirtuoso.pingpong.model.ActivityPaddle;
import com.itvirtuoso.pingpong.model.Paddle;
import com.itvirtuoso.pingpong.ui.PaddleObserver;

public class GameController implements PaddleObserver, BallObserver {
    private GameControllerListener listener;

    @Override
    public Paddle newGame(GameControllerListener listener) {
        this.listener = listener;
        return new ActivityPaddle();
    }

    @Override
    public void swing() {
        this.listener.onHit();
        Ball ball = new Ball(this, 100);
        new Thread(ball).start();
    }

    @Override
    public void onFirstBound() {
        this.listener.onFirstBound();
    }

    @Override
    public void onSecondBound() {
        this.listener.onSecondBound();
    }

    @Override
    public void onHittable() {
        this.listener.onHittable();
    }

    @Override
    public void onGoOutOfBounds() {
        this.listener.onGoOutOfBounds();
    }
}
