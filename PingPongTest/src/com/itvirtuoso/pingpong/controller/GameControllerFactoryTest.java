package com.itvirtuoso.pingpong.controller;

import com.itvirtuoso.pingpong.ui.PaddleObserver;

import junit.framework.TestCase;

public class GameControllerFactoryTest extends TestCase {
    public void testGameControllerオブジェクトを作成する() throws Exception {
        PaddleObserver controller = GameControllerFactory.create();
        assertNotNull("コントローラオブジェクトを取得できなかった", controller);
    }
}
