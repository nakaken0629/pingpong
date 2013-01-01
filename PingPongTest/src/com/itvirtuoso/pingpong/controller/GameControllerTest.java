package com.itvirtuoso.pingpong.controller;

import java.util.Calendar;

import junit.framework.TestCase;

import com.itvirtuoso.pingpong.model.Paddle;

public class GameControllerTest extends TestCase {
    class BaseListener implements PaddleListener {
        private Paddle paddle;

        public Paddle getPaddle() {
            return this.paddle;
        }

        protected GameController newGame() {
            GameController controller = new GameController();
            this.paddle = controller.newGame(this);
            return controller;
        }

        protected void swing() {
            paddle.swing();
        }

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
    
    public void testゲームを開始する() throws Exception {
        class FirstListener extends BaseListener {
            /* nop */
        }
        FirstListener listener1 = new FirstListener();
        listener1.newGame();
        assertNotNull("Paddleオブジェクトを取得できない", listener1.getPaddle());
    }

    public void testサーブする() throws Exception {
        class FirstListener extends BaseListener {
            private boolean isHit = false;
            private boolean isFirstBound = false;
            private boolean isSecondBound = false;
            private boolean isGoOutOfBounds = false;

            @Override
            public void onHit(PaddleEvent event) {
                this.isHit = true;
            }

            @Override
            public void onFirstBound(PaddleEvent event) {
                if (!this.isHit) {
                    return;
                }
                this.isFirstBound = true;
            }

            @Override
            public void onSecondBound(PaddleEvent event) {
                if (!this.isFirstBound) {
                    return;
                }
                this.isSecondBound = true;
            }

            @Override
            public void onGoOutOfBounds(PaddleEvent event) {
                if (!this.isSecondBound) {
                    return;
                }
                this.isGoOutOfBounds = true;
            }
        }
        FirstListener listener1 = new FirstListener();
        listener1.newGame();
        listener1.swing();
        long beginTime = Calendar.getInstance().getTimeInMillis();
        while (!listener1.isGoOutOfBounds) {
            Thread.sleep(10);
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - beginTime > 3000) {
                throw new Exception("3秒待ってもonGoOutOfBoudsが呼び出されない");
            }
        }
        assertTrue("onHitが呼び出されていない", listener1.isHit);
        assertTrue("onFirstBoundが呼び出されていない", listener1.isFirstBound);
        assertTrue("onSecondBoundが呼び出されていない", listener1.isSecondBound);
        assertTrue("onGoOutOfBoundsが呼び出されていない", listener1.isGoOutOfBounds);
    }

    public void testサーブしたときのイベントのタイミング() throws Exception {
        class FirstListener extends BaseListener {
            private long hitTime;
            private long firstBoundTime;
            private long secondBoundTime;
            private long goOutOfBoundsTime;
            private boolean isGoOutOfBounds = false;

            @Override
            public void onHit(PaddleEvent event) {
                this.hitTime = Calendar.getInstance().getTimeInMillis();
            }

            @Override
            public void onFirstBound(PaddleEvent event) {
                this.firstBoundTime = Calendar.getInstance().getTimeInMillis();
            }

            @Override
            public void onSecondBound(PaddleEvent event) {
                this.secondBoundTime = Calendar.getInstance().getTimeInMillis();
            }
            
            @Override
            public void onGoOutOfBounds(PaddleEvent event) {
                this.goOutOfBoundsTime = Calendar.getInstance()
                        .getTimeInMillis();
                this.isGoOutOfBounds = true;
            }
        }
        FirstListener listener1 = new FirstListener();
        listener1.newGame();
        listener1.swing();
        long beginTime = Calendar.getInstance().getTimeInMillis();
        while (!listener1.isGoOutOfBounds) {
            Thread.sleep(10);
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - beginTime > 3000) {
                throw new Exception("3秒待ってもonGoOutOfBoudsが呼び出されない");
            }
        }
        /* 間隔のタイミング（ヒット→初回バウンド→二回目バウンド→ヒットタイミング→フィールド外＝1:1:1:1） */
        double interval1 = listener1.firstBoundTime - listener1.hitTime;
        double interval2 = listener1.secondBoundTime - listener1.firstBoundTime;
        double interval3 = listener1.goOutOfBoundsTime
                - listener1.secondBoundTime;
        assertTrue("１回目と２回目のインターバルが1:1でない（誤差10%）",
                Math.abs(1 - interval1 / interval2) < 1 * 0.1);
        assertTrue("２回目と３回目のインターバルが1:2でない（誤差10%）",
                Math.abs(0.5 - interval2 / interval3) < 0.5 * 0.1);
    }

    public void testレシーブする() throws Exception {
        class FirstListener extends BaseListener {
            private boolean isFirstBound = false;
            private boolean isSecondBound = false;
            private boolean isGoOutOfBounds = false;

            protected void swing() {
                System.out.println("FirstListener swing");
                super.swing();
            }
            
            @Override
            public void onFirstBound(PaddleEvent event) {
                if (!event.isHitter()) {
                    return;
                }
                System.out.println("FirstListener onFirstBound");
                this.isFirstBound = true;
            }
            
            @Override
            public void onSecondBound(PaddleEvent event) {
                if (!event.isHitter()) {
                    return;
                }
                System.out.println("FirstListener onSecondBound");
                this.isSecondBound = true;
            }
            
            public void onHittable(PaddleEvent event) {
                if (!event.isHitter()) {
                    return;
                }
                System.out.println("FirstListener onHittable");
            }

            @Override
            public void onGoOutOfBounds(PaddleEvent event) {
                if (!event.isHitter()) {
                    return;
                }
                System.out.println("FirstListener onGoOutOfBounds");
                this.isGoOutOfBounds = true;
            }

        }
        class SecondListener extends BaseListener {
            private Paddle paddle;
            private boolean isGoOutOfBounds = false;

            void joinGame(GameController controller) {
                this.paddle = controller.joinGame(this);
            }
            
            @Override
            public void onFirstBound(PaddleEvent event) {
                if (!event.isHitter()) {
                    return;
                }
                System.out.println("SecondListener onFirstBound");
            }
            
            @Override
            public void onSecondBound(PaddleEvent event) {
                if (!event.isHitter()) {
                    return;
                }
                System.out.println("SecondListener onSecondBound");
            }

            @Override
            public void onHittable(PaddleEvent event) {
                if (!event.isHitter()) {
                    return;
                }
                System.out.println("SecondListener onHittable");
                this.paddle.swing();
            }
            
            @Override
            public void onGoOutOfBounds(PaddleEvent event) {
                if (!event.isHitter()) {
                    return;
                }
                System.out.println("SecondListener onGoOutOfBounds");
                this.isGoOutOfBounds = true;
            }
        }
        FirstListener listener1 = new FirstListener();
        GameController controller = listener1.newGame();
        SecondListener listener2 = new SecondListener();
        listener2.joinGame(controller);

        listener1.swing();
        long beginTime = Calendar.getInstance().getTimeInMillis();
        while (!listener1.isGoOutOfBounds) {
            Thread.sleep(10);
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - beginTime > 3000) {
                throw new Exception("3秒待ってもonGoOutOfBoudsが呼び出されない");
            }
        }
        assertFalse("レシーブしたのに、SecondListenerのonGoOutOfBoundsが呼び出されている",
                listener2.isGoOutOfBounds);
        assertTrue("レシーブなのに、FirstListenerのonFirstBoundが呼び出されない",
                listener1.isFirstBound);
        assertFalse("レシーブなのに、FirstListenerのonFirstBoundが呼び出された",
                listener1.isSecondBound);
        assertTrue("レシーブされたのに、FirstListenerのonGoOutOfBoundsが呼び出されない",
                listener1.isGoOutOfBounds);
    }
}
