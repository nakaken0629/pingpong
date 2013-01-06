package com.itvirtuoso.pingpong.controller;

public interface GameControllerListener {
    public abstract void onHit(GameControllerEvent event);

    public abstract void onFirstBound(GameControllerEvent event);
    
    public abstract void onSecondBound(GameControllerEvent event);
    
    public abstract void onHittable(GameControllerEvent event);
    
    public abstract void onGoOutOfBounds(GameControllerEvent event);
    
    public abstract void onServiceable(GameControllerEvent event);
}
