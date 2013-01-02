package com.itvirtuoso.pingpong.controller;

import junit.framework.TestCase;

import com.itvirtuoso.pingpong.model.Paddle;

public class GameControllerTest extends TestCase {
    public void testゲームを新規に作成する() throws Exception {
        PaddleObserver controller = GameControllerFactory.create();
        Paddle paddle = controller.newGame();
        assertNotNull("ゲーム開始時ラケットオブジェクトを入手できなかった", paddle);
    }
}
