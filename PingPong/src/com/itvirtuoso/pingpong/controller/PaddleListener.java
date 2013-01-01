package com.itvirtuoso.pingpong.controller;

public interface PaddleListener {
    void onHit(PaddleEvent event);
    void onFirstBound(PaddleEvent event);
    void onSecondBound(PaddleEvent event);
    void onHittable(PaddleEvent event);
    void onGoOutOfBounds(PaddleEvent event);
}
