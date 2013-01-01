package com.itvirtuoso.pingpong.controller;

public class PaddleEvent {
    private boolean isHitter;
    
    public PaddleEvent(boolean isHitter) {
        this.isHitter = isHitter;
    }
    
    public boolean isHitter() {
        return this.isHitter;
    }
}
