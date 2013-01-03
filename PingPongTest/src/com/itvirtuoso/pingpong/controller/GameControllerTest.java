package com.itvirtuoso.pingpong.controller;

import java.util.Calendar;

import junit.framework.TestCase;

import com.itvirtuoso.pingpong.ui.PaddleObserver;

public class GameControllerTest extends TestCase {
    class BaseListener implements GameControllerListener {
        @Override
        public void setObserver(PaddleObserver observer) {
            /* nop */
        }
        
        @Override
        public void onHit(GameControllerEvent event) {
            /* nop */
        }

        @Override
        public void onFirstBound(GameControllerEvent event) {
            /* nop */
        }

        @Override
        public void onSecondBound(GameControllerEvent event) {
            /* nop */
        }

        @Override
        public void onHittable(GameControllerEvent event) {
            /* nop */
        }

        @Override
        public void onGoOutOfBounds(GameControllerEvent event) {
            /* nop */
        }
    }

    public void testサーブをする() throws Exception {
        System.out.println("サーブをする");
        class TestListener extends BaseListener {
            private PaddleObserver observer;
            private boolean isCallOnHit = false;
            private boolean isCallOnFirstBound = false;
            private boolean isCallOnSecondBound = false;
            private boolean isCallOnHittable = false;
            private boolean isCallOnGoOutOfBounds = false;

            public void onCreate() {
                this.observer = GameControllerFactory.create();
                observer.newGame(this);
            }

            public void onSwing() {
                this.observer.swing(this);
            }

            @Override
            public void onHit(GameControllerEvent event) {
                this.isCallOnHit = true;
            }

            @Override
            public void onFirstBound(GameControllerEvent event) {
                if (this.isCallOnHit) {
                    this.isCallOnFirstBound = true;
                }
            }

            @Override
            public void onSecondBound(GameControllerEvent event) {
                if (this.isCallOnFirstBound) {
                    this.isCallOnSecondBound = true;
                }
            }

            @Override
            public void onHittable(GameControllerEvent event) {
                if (this.isCallOnSecondBound) {
                    this.isCallOnHittable = true;
                }
            }

            @Override
            public void onGoOutOfBounds(GameControllerEvent event) {
                if (this.isCallOnHittable) {
                    this.isCallOnGoOutOfBounds = true;
                }
            }
        }
        TestListener listener = new TestListener();
        listener.onCreate();
        listener.onSwing();
        long beginTime = Calendar.getInstance().getTimeInMillis();
        while (!listener.isCallOnGoOutOfBounds) {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - beginTime > 3000) {
                throw new Exception("三秒待ってもサーブが終わらなかった");
            }
            Thread.sleep(100);
        }
        assertTrue("onHitが呼び出されなかった", listener.isCallOnHit);
        assertTrue("onFirstBoundが呼び出されなかった", listener.isCallOnFirstBound);
        assertTrue("onSecondBoundが呼び出されなかった", listener.isCallOnSecondBound);
        assertTrue("onHittableが呼び出されなかった", listener.isCallOnHittable);
        assertTrue("onGoOutOfBoundsが呼び出されなかった", listener.isCallOnGoOutOfBounds);
    }

    public void testレシーブをする() throws Exception {
        System.out.println("レシーブをする");
        class TestListener extends BaseListener {
            class TestReceiveListener extends BaseListener {
                private PaddleObserver observer;
                private boolean isCallOnHit = false;
                private boolean isCallOnFirstBound = false;
                private boolean isCallOnSecondBound = false;
                private boolean isCallOnHittable = false;
                private boolean isCallOnGoOutOfBounds = false;

                @Override
                public void setObserver(PaddleObserver observer) {
                    this.observer = observer;
                }
                
                public void onSwing() {
                    this.observer.swing(this);
                }

                @Override
                public void onHit(GameControllerEvent event) {
                    if (event.isHitter()) {
                        this.isCallOnHit = true;
                        System.out.println("receive - hit");
                    }
                }

                @Override
                public void onFirstBound(GameControllerEvent event) {
                    if (event.isHitter()) {
                        this.isCallOnFirstBound = true;
                        System.out.println("receive - firstBound");
                    }
                }

                @Override
                public void onSecondBound(GameControllerEvent event) {
                    if (event.isHitter()) {
                        this.isCallOnSecondBound = true;
                        System.out.println("receive - secondBound");
                    }
                }

                @Override
                public void onHittable(GameControllerEvent event) {
                    if (event.isHitter()) {
                        this.isCallOnHittable = true;
                        this.onSwing();
                        System.out.println("receive - hittable");
                    }
                }

                @Override
                public void onGoOutOfBounds(GameControllerEvent event) {
                    if (event.isHitter()) {
                        this.isCallOnGoOutOfBounds = true;
                        System.out.println("receive - goOutOfBounds");
                    }
                }
            }

            private PaddleObserver observer;
            private TestReceiveListener receiveListener;
            private boolean isCallOnHit = false;
            private boolean isCallOnFirstBound = false;
            private boolean isCallOnSecondBound = false;
            private boolean isCallOnHittable = false;
            private boolean isCallOnGoOutOfBounds = false;

            public void onCreate() {
                this.observer = GameControllerFactory.create();
                observer.newGame(this);
            }

            public void onJoin() {
                this.receiveListener = new TestReceiveListener();
                this.observer.joinGame(this.receiveListener);
            }

            public void onSwing() {
                this.observer.swing(this);
            }

            @Override
            public void onHit(GameControllerEvent event) {
                if (event.isHitter()) {
                    this.isCallOnHit = true;
                    System.out.println("service - hit");
                }
            }

            @Override
            public void onFirstBound(GameControllerEvent event) {
                if (event.isHitter()) {
                    this.isCallOnFirstBound = true;
                    System.out.println("service - firstBound");
                }
            }

            @Override
            public void onSecondBound(GameControllerEvent event) {
                if (event.isHitter()) {
                    this.isCallOnSecondBound = true;
                    System.out.println("service - secondBound");
                }
            }

            @Override
            public void onHittable(GameControllerEvent event) {
                if (event.isHitter()) {
                    this.isCallOnHittable = true;
                    System.out.println("service - hittable");
                }
            }

            @Override
            public void onGoOutOfBounds(GameControllerEvent event) {
                if (event.isHitter()) {
                    this.isCallOnGoOutOfBounds = true;
                    System.out.println("service - goOutOfBounds");
                }
            }
        }

        TestListener serviceListener = new TestListener();
        serviceListener.onCreate();
        serviceListener.onJoin();
        TestListener.TestReceiveListener receiveListener = serviceListener.receiveListener;
        serviceListener.onSwing();
        long beginTime = Calendar.getInstance().getTimeInMillis();
        while (!serviceListener.isCallOnGoOutOfBounds) {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - beginTime > 3000) {
                throw new Exception("三秒待ってもサーブが終わらなかった");
            }
            Thread.sleep(100);
        }
        /* サーブ */
        assertTrue("サーブ側のonHitが呼び出されなかった", serviceListener.isCallOnHit);
        assertTrue("レシーブ側のonFirstBoundが呼び出されなかった",
                receiveListener.isCallOnFirstBound);
        assertTrue("レシーブ側のonSecondBoundが呼び出されなかった",
                receiveListener.isCallOnSecondBound);
        assertTrue("レシーブ側のonHittableが呼び出されなかった",
                receiveListener.isCallOnHittable);
        assertFalse("レシーブ側のonGoOutOfBOundsが呼び出された",
                receiveListener.isCallOnGoOutOfBounds);
        /* レシーブ */
        assertTrue("レシーブ側のonHitが呼び出された", receiveListener.isCallOnHit);
        assertTrue("サーブ側のonFirstBoundが呼び出されなかった",
                serviceListener.isCallOnFirstBound);
        assertFalse("サーブ側のonSecondBoundが呼び出された",
                serviceListener.isCallOnSecondBound);
        assertTrue("サーブ側のonHittableが呼び出されなかった", serviceListener.isCallOnHittable);
        assertTrue("サーブ側のonGoOutOfBOundsが呼び出されなかった",
                serviceListener.isCallOnGoOutOfBounds);
    }
    
    public void test自分がサーブを打てる状態でない時のswingは無視される() throws Exception {
        System.out.println("自分がサーブを打てる状態でない時のswingは無視される");
        fail("todo");
    }
    
    public void testプレイ中に自分のhittable状態でない時のswingは無視される() throws Exception {
        System.out.println("プレイ中に自分のhittable状態でない時のswingは無視される");
        fail("todo");
    }
}
