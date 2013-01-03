package com.itvirtuoso.pingpong.controller;

import com.itvirtuoso.pingpong.ui.PaddleObserver;

public interface GameControllerListener {
    public abstract void setObserver(PaddleObserver observer);
    
    public abstract void onHit(GameControllerEvent event);

    public abstract void onFirstBound(GameControllerEvent event);
    
    public abstract void onSecondBound(GameControllerEvent event);
    
    public abstract void onHittable(GameControllerEvent event);
    
    public abstract void onGoOutOfBounds(GameControllerEvent event);
}
