package com.itvirtuoso.pingpong.controller;

public final class GameControllerFactory {
    private GameControllerFactory() {
        /* nop */
    }
    
    public static PaddleObserver create() {
        return new GameController();
    }
}
