package com.itvirtuoso.pingpong.controller;

public interface PaddleListener {
    void onHit();
    void onBound();
    void onGoOutOfBounds();
}
