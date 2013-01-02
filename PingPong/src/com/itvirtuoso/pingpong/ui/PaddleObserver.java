package com.itvirtuoso.pingpong.ui;

import com.itvirtuoso.pingpong.controller.GameControllerListener;
import com.itvirtuoso.pingpong.model.Paddle;

public interface PaddleObserver {
    public abstract Paddle newGame(GameControllerListener listener);
    
    public abstract void swing();
}
