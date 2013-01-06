package com.itvirtuoso.pingpong.controller;

import java.util.Calendar;

import junit.framework.TestCase;

import com.itvirtuoso.pingpong.ui.PaddleObserver;

public class GameControllerTest extends TestCase {
    class BaseListener implements GameControllerListener {

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
        
        @Override
        public void onServiceable(GameControllerEvent event) {
            /* nop */
        }
    }

    public void testサービスをする() throws Exception {
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
                this.observer.swing(this, 10);
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
                throw new Exception("三秒待ってもサービスが終わらなかった");
            }
            Thread.sleep(10);
        }
        assertTrue("onHitが呼び出されなかった", listener.isCallOnHit);
        assertTrue("onFirstBoundが呼び出されなかった", listener.isCallOnFirstBound);
        assertTrue("onSecondBoundが呼び出されなかった", listener.isCallOnSecondBound);
        assertTrue("onHittableが呼び出されなかった", listener.isCallOnHittable);
        assertTrue("onGoOutOfBoundsが呼び出されなかった", listener.isCallOnGoOutOfBounds);
    }

    public void testサービスした時のイベントの呼び出し回数は一回であること() throws Exception {
        class TestListener extends BaseListener {
            private PaddleObserver observer;
            private int onHitCount = 0;
            private int onFirstBoundCound = 0;
            private int onSecondBoundCount = 0;
            private int onHittableCount = 0;
            private int onGoOutOfBoundsCount = 0;

            public void onCreate() {
                this.observer = GameControllerFactory.create();
                observer.newGame(this);
            }

            public void onSwing() {
                this.observer.swing(this, 10);
            }

            @Override
            public void onHit(GameControllerEvent event) {
                this.onHitCount++;
            }

            @Override
            public void onFirstBound(GameControllerEvent event) {
                this.onFirstBoundCound++;
            }

            @Override
            public void onSecondBound(GameControllerEvent event) {
                this.onSecondBoundCount++;
            }

            @Override
            public void onHittable(GameControllerEvent event) {
                this.onHittableCount++;
            }

            @Override
            public void onGoOutOfBounds(GameControllerEvent event) {
                this.onGoOutOfBoundsCount++;
            }
        }
        TestListener listener = new TestListener();
        listener.onCreate();
        listener.onSwing();
        long beginTime = Calendar.getInstance().getTimeInMillis();
        while (listener.onGoOutOfBoundsCount == 0) {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - beginTime > 3000) {
                throw new Exception("三秒待ってもサービスが終わらなかった");
            }
            Thread.sleep(10);
        }
        assertEquals("onHitが呼び出されなかった", 1, listener.onHitCount);
        assertEquals("onFirstBoundが呼び出されなかった", 1, listener.onFirstBoundCound);
        assertEquals("onSecondBoundが呼び出されなかった", 1, listener.onSecondBoundCount);
        assertEquals("onHittableが呼び出されなかった", 1, listener.onHittableCount);
        assertEquals("onGoOutOfBoundsが呼び出されなかった", 1, listener.onGoOutOfBoundsCount);
    }

    public void testレシーブをする() throws Exception {
        class ServiceListener extends BaseListener {
            class ReceiveListener extends BaseListener {
                private PaddleObserver observer;
                private boolean isCallOnHit = false;
                private boolean isCallOnFirstBound = false;
                private boolean isCallOnSecondBound = false;
                private boolean isCallOnHittable = false;
                private boolean isCallOnGoOutOfBounds = false;

                public ReceiveListener(PaddleObserver observer) {
                    this.observer = observer;
                }

                public void onSwing() {
                    this.observer.swing(this, 10);
                }

                @Override
                public void onHit(GameControllerEvent event) {
                    if (event.isHitter()) {
                        this.isCallOnHit = true;
                    }
                }

                @Override
                public void onFirstBound(GameControllerEvent event) {
                    if (event.isHitter()) {
                        this.isCallOnFirstBound = true;
                    }
                }

                @Override
                public void onSecondBound(GameControllerEvent event) {
                    if (event.isHitter()) {
                        this.isCallOnSecondBound = true;
                    }
                }

                @Override
                public void onHittable(GameControllerEvent event) {
                    if (event.isHitter()) {
                        this.isCallOnHittable = true;
                        this.onSwing();
                    }
                }

                @Override
                public void onGoOutOfBounds(GameControllerEvent event) {
                    if (event.isHitter()) {
                        this.isCallOnGoOutOfBounds = true;
                    }
                }
            }

            private PaddleObserver observer;
            private ReceiveListener receiveListener;
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
                this.receiveListener = new ReceiveListener(this.observer);
                this.observer.joinGame(this.receiveListener);
            }

            public void onSwing() {
                this.observer.swing(this, 10);
            }

            @Override
            public void onHit(GameControllerEvent event) {
                if (event.isHitter()) {
                    this.isCallOnHit = true;
                }
            }

            @Override
            public void onFirstBound(GameControllerEvent event) {
                if (event.isHitter()) {
                    this.isCallOnFirstBound = true;
                }
            }

            @Override
            public void onSecondBound(GameControllerEvent event) {
                if (event.isHitter()) {
                    this.isCallOnSecondBound = true;
                }
            }

            @Override
            public void onHittable(GameControllerEvent event) {
                if (event.isHitter()) {
                    this.isCallOnHittable = true;
                }
            }

            @Override
            public void onGoOutOfBounds(GameControllerEvent event) {
                if (event.isHitter()) {
                    this.isCallOnGoOutOfBounds = true;
                }
            }
        }

        ServiceListener serviceListener = new ServiceListener();
        serviceListener.onCreate();
        serviceListener.onJoin();
        ServiceListener.ReceiveListener receiveListener = serviceListener.receiveListener;
        serviceListener.onSwing();
        long beginTime = Calendar.getInstance().getTimeInMillis();
        while (!serviceListener.isCallOnGoOutOfBounds) {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - beginTime > 3000) {
                throw new Exception("三秒待ってもサービスが終わらなかった");
            }
            Thread.sleep(10);
        }
        /* サービス */
        assertTrue("サービス側のonHitが呼び出されなかった", serviceListener.isCallOnHit);
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
        assertTrue("サービス側のonFirstBoundが呼び出されなかった",
                serviceListener.isCallOnFirstBound);
        assertFalse("サービス側のonSecondBoundが呼び出された",
                serviceListener.isCallOnSecondBound);
        assertTrue("サービス側のonHittableが呼び出されなかった",
                serviceListener.isCallOnHittable);
        assertTrue("サービス側のonGoOutOfBOundsが呼び出されなかった",
                serviceListener.isCallOnGoOutOfBounds);
    }

    public void test自分がサービスを打てる状態でない時のswingは無視される() throws Exception {
        class ServiceListener extends BaseListener {
            class ReceiveListener extends BaseListener {
                private PaddleObserver observer;
                private boolean isCallOnHit = false;

                public ReceiveListener(PaddleObserver observer) {
                    this.observer = observer;
                }

                public void onSwing() {
                    this.observer.swing(this, 10);
                }

                @Override
                public void onHit(GameControllerEvent event) {
                    this.isCallOnHit = true;
                }
            }

            private ReceiveListener receiveListener;
            private PaddleObserver observer;

            public void onCreate() {
                this.observer = GameControllerFactory.create();
                observer.newGame(this);
            }

            public void onJoin() {
                this.receiveListener = new ReceiveListener(this.observer);
                this.observer.joinGame(this.receiveListener);
            }
        }

        ServiceListener serviceListener = new ServiceListener();
        serviceListener.onCreate();
        serviceListener.onJoin();
        ServiceListener.ReceiveListener receiveListener = serviceListener.receiveListener;
        receiveListener.onSwing();
        Thread.sleep(200);
        assertFalse("ReceiveListenerのonHitが呼び出された", receiveListener.isCallOnHit);
    }

    enum Timing {
        HIT, FIRST_BOUND, SECOND_BOUND, HITTABLE, GO_OUT_OF_BOUNDS,
    }

    public void testプレイ中に自分のhittable状態でない時のswingは無視される() throws Exception {
        class ServiceListener extends BaseListener {
            class ReceiveListener extends BaseListener {
                private PaddleObserver observer;

                public ReceiveListener(PaddleObserver observer) {
                    this.observer = observer;
                }

                @Override
                public void onHittable(GameControllerEvent event) {
                    if (event.isHitter()) {
                        this.observer.swing(this, 10);
                    }
                }
            }

            private Timing hitTiming;
            private PaddleObserver observer;
            private ReceiveListener receiveListener;
            private boolean isCallOnGoOutOfBounds = false;
            private int callCountOnHit = 0;

            public ServiceListener(Timing hitTiming) {
                this.hitTiming = hitTiming;
            }

            public void onCreate() {
                this.observer = GameControllerFactory.create();
                observer.newGame(this);
            }

            public void onJoin() {
                this.receiveListener = new ReceiveListener(this.observer);
                this.observer.joinGame(this.receiveListener);
            }

            public void onSwing() {
                this.observer.swing(this, 10);
            }

            @Override
            public void onHit(GameControllerEvent event) {
                if (event.isHitter()) {
                    this.callCountOnHit++;
                }
                if (event.isHitter() && this.hitTiming == Timing.HIT) {
                    this.observer.swing(this, 10);
                }
            }

            @Override
            public void onFirstBound(GameControllerEvent event) {
                if (event.isHitter() && this.hitTiming == Timing.FIRST_BOUND) {
                    this.observer.swing(this, 10);
                }
            }

            @Override
            public void onSecondBound(GameControllerEvent event) {
                if (event.isHitter() && this.hitTiming == Timing.SECOND_BOUND) {
                    this.observer.swing(this, 10);
                }
            }

            @Override
            public void onHittable(GameControllerEvent event) {
                if (event.isHitter() && this.hitTiming == Timing.HITTABLE) {
                    this.observer.swing(this, 10);
                }
            }

            @Override
            public void onGoOutOfBounds(GameControllerEvent event) {
                if (event.isHitter()
                        && this.hitTiming == Timing.GO_OUT_OF_BOUNDS) {
                    this.observer.swing(this, 10);
                }
                this.isCallOnGoOutOfBounds = true;
            }
        }
        for (Timing hitTiming : Timing.values()) {
            if (hitTiming == Timing.HITTABLE) {
                continue;
            }
            long beginTime = Calendar.getInstance().getTimeInMillis();
            ServiceListener serviceListener = new ServiceListener(hitTiming);
            serviceListener.onCreate();
            serviceListener.onJoin();
            serviceListener.onSwing();
            while (!serviceListener.isCallOnGoOutOfBounds) {
                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime - beginTime > 3000) {
                    throw new Exception("三秒待ってもゲームが終わらなかった");
                }
                Thread.sleep(10);
            }
            assertEquals(hitTiming + "でswingして、サービスのonHitが二回呼ばれた", 1,
                    serviceListener.callCountOnHit);
        }
    }
    
    public void testボールが外に出てから再びサービスできる状態になる() throws Exception {
        fail("サービスできる状態になる実装をすること");
    }
}
