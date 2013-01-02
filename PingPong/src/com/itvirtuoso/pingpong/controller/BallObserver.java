package com.itvirtuoso.pingpong.controller;

interface BallObserver {
    abstract void onFirstBound();
    
    abstract void onSecondBound();
    
    abstract void onHittable();
    
    abstract void onGoOutOfBounds();
}
