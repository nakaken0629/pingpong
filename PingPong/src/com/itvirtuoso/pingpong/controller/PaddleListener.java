package com.itvirtuoso.pingpong.controller;

public interface PaddleListener {
    public abstract void onHit(PaddleEvent event);

    public abstract void onFirstBound(PaddleEvent event);

    public abstract void onSecondBound(PaddleEvent event);

    public abstract void onHittable(PaddleEvent event);

    public abstract void onGoOutOfBounds(PaddleEvent event);
}
