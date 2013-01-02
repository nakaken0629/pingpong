package com.itvirtuoso.pingpong.controller;

import com.itvirtuoso.pingpong.model.Paddle;

public class GameController implements PaddleObserver {
    @Override
    public Paddle newGame() {
        return new Paddle();
    }
}
