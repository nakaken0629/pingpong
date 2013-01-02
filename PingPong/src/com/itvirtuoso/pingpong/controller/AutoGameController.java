package com.itvirtuoso.pingpong.controller;

import com.itvirtuoso.pingpong.model.AutoPaddle;
import com.itvirtuoso.pingpong.model.Paddle;

public class AutoGameController extends AbstractGameController {
    @Override
    public Paddle newGame(PaddleListener listener) {
        Paddle paddle1 = super.newGame(listener);
        AutoPaddle paddle2 = new AutoPaddle(this);
        addPaddle(paddle2);
        return paddle1;
    }
}
