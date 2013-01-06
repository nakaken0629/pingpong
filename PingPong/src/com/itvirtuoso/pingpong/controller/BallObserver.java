package com.itvirtuoso.pingpong.controller;

interface BallObserver {
    public abstract void onFirstBound();
    
    public abstract void onSecondBound();
    
    public abstract void onHittable();
    
    public abstract void onGoOutOfBounds();
    
    public abstract void onServiceable();
}
