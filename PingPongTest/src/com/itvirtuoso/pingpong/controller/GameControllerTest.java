package com.itvirtuoso.pingpong.controller;

import java.util.Calendar;

import junit.framework.TestCase;

import com.itvirtuoso.pingpong.model.Paddle;
import com.itvirtuoso.pingpong.ui.PaddleObserver;

public class GameControllerTest extends TestCase {
    class BaseListener implements GameControllerListener {
        @Override
        public void onHit() {
            /* nop */
        }

        @Override
        public void onFirstBound() {
            /* nop */
        }

        @Override
        public void onSecondBound() {
            /* nop */
        }

        @Override
        public void onHittable() {
            /* nop */
        }

        @Override
        public void onGoOutOfBounds() {
            /* nop */
        }
    }

    public void testゲームを新規に作成する() throws Exception {
        class TestListener extends BaseListener {
            private PaddleObserver observer;
            private Paddle paddle;

            public void onCreate() {
                this.observer = GameControllerFactory.create();
                this.paddle = observer.newGame(this);
            }
        }
        TestListener listener = new TestListener();
        listener.onCreate();
        assertNotNull("ゲーム開始時にラケットオブジェクトを入手できなかった", listener.paddle);
    }

    public void testサーブをする() throws Exception {
        class TestListener extends BaseListener {
            private PaddleObserver observer;
            private Paddle paddle;
            private boolean isCallOnHit = false;
            private boolean isCallOnFirstBound = false;
            private boolean isCallOnSecondBound = false;
            private boolean isCallOnHittable = false;
            private boolean isCallOnGoOutOfBounds = false;

            public void onCreate() {
                this.observer = GameControllerFactory.create();
                this.paddle = observer.newGame(this);
            }

            public void onSwing() {
                this.observer.swing();
            }

            @Override
            public void onHit() {
                this.isCallOnHit = true;
            }

            @Override
            public void onFirstBound() {
                if (this.isCallOnHit) {
                    this.isCallOnFirstBound = true;
                }
            }

            @Override
            public void onSecondBound() {
                if (this.isCallOnFirstBound) {
                    this.isCallOnSecondBound = true;
                }
            }

            @Override
            public void onHittable() {
                if (this.isCallOnSecondBound) {
                    this.isCallOnHittable = true;
                }
            }

            @Override
            public void onGoOutOfBounds() {
                if (this.isCallOnHittable) {
                    this.isCallOnGoOutOfBounds = true;
                }
            }
        }
        TestListener listener = new TestListener();
        listener.onCreate();
        listener.onSwing();
        long beginTime = Calendar.getInstance().getTimeInMillis();
        while(!listener.isCallOnGoOutOfBounds) {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if(currentTime - beginTime > 3000) {
                throw new Exception("三秒待ってもサーブが終わらなかった");
            }
            Thread.sleep(100);
        }
    }
}
