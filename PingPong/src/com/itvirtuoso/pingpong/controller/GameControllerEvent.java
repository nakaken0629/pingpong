package com.itvirtuoso.pingpong.controller;

public class GameControllerEvent {
    private boolean isHitter;

    public GameControllerEvent(boolean isHitter) {
        this.isHitter = isHitter;
    }

    public boolean isHitter() {
        return this.isHitter;
    }
}
