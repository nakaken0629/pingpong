package com.itvirtuoso.pingpong.controller;

import com.itvirtuoso.pingpong.model.Paddle;

public interface PaddleObserver {
    public abstract Paddle newGame();
}
