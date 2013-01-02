package com.itvirtuoso.pingpong.model;

import junit.framework.TestCase;

import com.itvirtuoso.pingpong.controller.GameController;
import com.itvirtuoso.pingpong.controller.PaddleEvent;
import com.itvirtuoso.pingpong.controller.PaddleListener;

public class PaddleTest extends TestCase {
    class BaseGameController implements GameController {
        @Override
        public boolean isPlaying() {
            return false;
        }

        @Override
        public Paddle newGame(PaddleListener listener) {
            return null;
        }

        @Override
        public Paddle joinGame(PaddleListener listener) {
            return null;
        }

        @Override
        public void serve(Paddle paddle, long interval) {
            /* nop */
        }

        @Override
        public void receive(Paddle paddle, long interval) {
            /* nop */
        }
    }

    class BaseListener implements PaddleListener {
        @Override
        public void onHit(PaddleEvent event) {
            /* nop */
        }

        @Override
        public void onFirstBound(PaddleEvent event) {
            /* nop */
        }

        @Override
        public void onSecondBound(PaddleEvent event) {
            /* nop */
        }

        @Override
        public void onHittable(PaddleEvent event) {
            /* nop */
        }

        @Override
        public void onGoOutOfBounds(PaddleEvent event) {
            /* nop */
        }
    }

    public void testサーブを打てる準備を行う() throws Exception {
        class TestGameController extends BaseGameController {
            private boolean isServe = false;

            public boolean isPlaying() {
                return false;
            };

            public void serve(Paddle paddle, long interval) {
                this.isServe = true;
            };
        }
        TestGameController controller = new TestGameController();
        BaseListener listener = new BaseListener();
        Paddle paddle = new Paddle(controller, listener);
        paddle.swing();
        assertFalse("setupが呼ばれていないのにサーブができてしまった", controller.isServe);
        paddle.setup();
        paddle.swing();
        assertTrue("setupが呼ばれたのにサーブができない", controller.isServe);
    }
    
    public void testサーブを打つ() throws Exception {
        class TestGameController extends BaseGameController {
            private boolean isServe = false;
            private boolean isReceive = false;
            
            @Override
            public boolean isPlaying() {
                return false;
            }
            
            @Override
            public void serve(Paddle paddle, long interval) {
                this.isServe = true;
            }
            
            @Override
            public void receive(Paddle paddle, long interval) {
                this.isReceive = true;
            }
        }
        TestGameController controller = new TestGameController();
        BaseListener listener = new BaseListener();
        Paddle paddle = new Paddle(controller, listener);
        paddle.setup();
        assertFalse("ゲーム中になっている", controller.isPlaying());
        paddle.swing();
        assertTrue("controller.serveが呼び出されていない", controller.isServe);
        assertFalse("controller.receiveが呼び出されている", controller.isReceive);
    }

    public void testレシーブを打つ() throws Exception {
        class TestGameController extends BaseGameController {
            private boolean isServe = false;
            private boolean isReceive = false;
            
            @Override
            public boolean isPlaying() {
                return true;
            }
            
            @Override
            public void serve(Paddle paddle, long interval) {
                this.isServe = true;
            }
            
            @Override
            public void receive(Paddle paddle, long interval) {
                this.isReceive = true;
            }
        }
        TestGameController controller = new TestGameController();
        BaseListener listener = new BaseListener();
        Paddle paddle = new Paddle(controller, listener);
        paddle.setup();
        assertTrue("ゲーム中になっていない", controller.isPlaying());
        paddle.swing();
        assertFalse("controller.serveが呼び出されている", controller.isServe);
        assertTrue("controller.receiveが呼び出されていない", controller.isReceive);
    }
}
