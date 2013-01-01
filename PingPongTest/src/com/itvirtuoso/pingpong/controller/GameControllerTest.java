package com.itvirtuoso.pingpong.controller;

import java.util.Calendar;

import junit.framework.TestCase;

import com.itvirtuoso.pingpong.model.Paddle;

public class GameControllerTest extends TestCase {
    class BaseListener implements PaddleListener {
        @Override
        public void onHit() {
            /* nop */
        }

        @Override
        public void onBound() {
            /* nop */
        }

        @Override
        public void onGoOutOfBounds() {
            /* nop */
        }
    }

    public void testゲームを開始する() throws Exception {
        class TestListener extends BaseListener {
            void newGame() {
                GameController controller = new GameController();
                Paddle paddle = controller.newGame(this);
                assertNotNull("Paddleオブジェクトを取得できない", paddle);
            }
        }
        TestListener listener = new TestListener();
        listener.newGame();
    }

    public void testサーブする() throws Exception {
        class TestListener extends BaseListener {
            private Paddle paddle;
            private boolean isHit = false;
            private int boundCount = 0;
            private boolean isGoOutOfBounds = false;

            void newGame() {
                GameController controller = new GameController();
                this.paddle = controller.newGame(this);
            }

            void swing() {
                paddle.swing();
            }

            @Override
            public void onHit() {
                assertFalse("hitが呼ばれている", this.isHit);
                assertEquals("バウンドしている", 0, this.boundCount);
                assertFalse("ボールがラインを割っている", this.isGoOutOfBounds);
                this.isHit = true;
            }

            @Override
            public void onBound() {
                assertTrue("hitが呼ばれていない", this.isHit);
                assertTrue("バウンド回数がおかしい", this.boundCount < 2);
                assertFalse("ボールがラインを割っている", isGoOutOfBounds);
                this.boundCount++;
            }

            @Override
            public void onGoOutOfBounds() {
                assertTrue("hitが呼ばれていない", this.isHit);
                assertEquals("バウンド回数がおかしい", 2, this.boundCount);
                assertFalse("ボールがラインを割っている", isGoOutOfBounds);
                this.isGoOutOfBounds = true;
            }
        }
        TestListener listener = new TestListener();
        listener.newGame();
        listener.swing();
        long beginTime = Calendar.getInstance().getTimeInMillis();
        while(!listener.isGoOutOfBounds) {
            Thread.sleep(100);
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - beginTime > 3000) {
                throw new Exception("3秒待ってもonGoOutOfBoudsが呼び出されない");
            }
        }
    }
}
