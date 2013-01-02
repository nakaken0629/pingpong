package com.itvirtuoso.pingpong.controller;

public interface GameControllerListener {
    public abstract void onHit();

    public abstract void onFirstBound();
    
    public abstract void onSecondBound();
    
    public abstract void onHittable();
    
    public abstract void onGoOutOfBounds();
}
