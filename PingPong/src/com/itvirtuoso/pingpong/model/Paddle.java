package com.itvirtuoso.pingpong.model;

import com.itvirtuoso.pingpong.controller.GameController;
import com.itvirtuoso.pingpong.controller.PaddleListener;

public class Paddle {
    private GameController controller;
    private PaddleListener listener;

    public Paddle(GameController controller, PaddleListener listener) {
        this.controller = controller;
        this.listener = listener;
    }

    public void swing() {
        controller.swing(this);
    }

    public void onHit() {
        this.listener.onHit();
    }

    public void onBound() {
        this.listener.onBound();
    }

    public void onGoOutOfBounds() {
        this.listener.onGoOutOfBounds();
    }
}
