package com.itvirtuoso.pingpong.controller;

import com.itvirtuoso.pingpong.model.Paddle;

public interface GameController {
    public abstract boolean isPlaying();

    public abstract Paddle newGame(PaddleListener listener);

    public abstract Paddle joinGame(PaddleListener listener);

    public abstract void serve(Paddle paddle, long interval);

    public abstract void receive(Paddle paddle, long interval);

}