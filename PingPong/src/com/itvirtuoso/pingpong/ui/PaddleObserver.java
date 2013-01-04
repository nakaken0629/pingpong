package com.itvirtuoso.pingpong.ui;

import com.itvirtuoso.pingpong.controller.GameControllerListener;

public interface PaddleObserver {
    public abstract void newGame(GameControllerListener listener);

    public abstract void joinGame(GameControllerListener listener);

    public abstract void swing(GameControllerListener listener, long interval);
}
