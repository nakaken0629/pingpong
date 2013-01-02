package com.itvirtuoso.pingpong.controller;

import com.itvirtuoso.pingpong.ui.PaddleObserver;

public final class GameControllerFactory {
    private GameControllerFactory() {
        /* nop */
    }
    
    public static PaddleObserver create() {
        return new GameController();
    }
}
