package com.itvirtuoso.pingpong.controller;

import java.util.ArrayList;
import java.util.HashMap;

import com.itvirtuoso.pingpong.ui.PaddleObserver;

public class GameController implements PaddleObserver, BallObserver {
    abstract class Updater implements Runnable {
        private GameControllerListener listener;
        private GameControllerEvent event;

        Updater(GameControllerListener listener, GameControllerEvent event) {
            this.listener = listener;
            this.event = event;
        }

        GameControllerListener getListener() {
            return listener;
        }

        GameControllerEvent getEvent() {
            return event;
        }
    }

    abstract class UpdaterFactory {
        abstract Updater create(GameControllerListener listener,
                GameControllerEvent event);
    }

    private ArrayList<GameControllerListener> listeners = new ArrayList<GameControllerListener>();
    private HashMap<GameControllerListener, Object> locks = new HashMap<GameControllerListener, Object>();
    private int listenerIndex;
    private Thread ballThread;
    private GameMode gameMode;

    /* TODO: コンストラクタを使えないようにする */
//    GameController() {
//        /* nop */
//    }
    
    public final GameMode getGameMode() {
        return this.gameMode;
    }

    private void addListener(GameControllerListener listener) {
        this.listeners.add(listener);
        this.locks.put(listener, new Object());
    }

    @Override
    public final void newGame(GameControllerListener listener) {
        addListener(listener);
        this.listenerIndex = 0;
        this.gameMode = GameMode.WAIT;
    }

    @Override
    public final void joinGame(GameControllerListener listener) {
        addListener(listener);
    }

    private void callListenerAsync(UpdaterFactory updaterFactory,
            int listenerIndex) {
        for (int index = 0; index < this.listeners.size(); index++) {
            GameControllerListener listener = this.listeners.get(index);
            boolean isHitter = (index == listenerIndex);
            GameControllerEvent event = new GameControllerEvent(isHitter);
            Updater updater = updaterFactory.create(listener, event);
            Thread updaterThread = new Thread(updater);
            updaterThread.start();
        }
    }

    @Override
    public final void swing(GameControllerListener hitterListener, long interval) {
        synchronized (this.locks.get(hitterListener)) {
            swingSafety(hitterListener, interval);
        }
    }

    private void swingSafety(GameControllerListener hitterListener,
            long interval) {
        if (!this.listeners.get(this.listenerIndex).equals(hitterListener)) {
            /* 他人の打順であればswingを無視する */
            return;
        }

        if (this.gameMode == GameMode.WAIT) {
            /* サービスする */
            this.gameMode = GameMode.HIT;
            hitBySwing(hitterListener);
            Ball ball = new Ball(this, interval);
            this.ballThread = new Thread(ball);
            this.ballThread.start();
        } else if(this.gameMode == GameMode.HITTABLE) {
            /* レシーブする */
            this.ballThread.interrupt();
            hitBySwing(hitterListener);
        } else {
            /* サービスもレシーブもできなければ何もしない */
        }
    }

    private void hitBySwing(GameControllerListener hitterListener) {
        this.gameMode = GameMode.HIT;
        UpdaterFactory factory = new UpdaterFactory() {
            @Override
            Updater create(GameControllerListener listener,
                    GameControllerEvent event) {
                return new Updater(listener, event) {
                    @Override
                    public void run() {
                        this.getListener().onHit(this.getEvent());
                    }
                };
            }
        };
        callListenerAsync(factory, this.listenerIndex);

        /* 打順を次に進める */
        this.listenerIndex = (this.listenerIndex + 1) % this.listeners.size();
    }

    @Override
    public final void onFirstBound() {
        this.gameMode = GameMode.FIRST_BOUND;
        UpdaterFactory factory = new UpdaterFactory() {
            @Override
            Updater create(GameControllerListener listener,
                    GameControllerEvent event) {
                return new Updater(listener, event) {
                    @Override
                    public void run() {
                        this.getListener().onFirstBound(this.getEvent());
                    }
                };
            }
        };
        callListenerAsync(factory, this.listenerIndex);
    }

    @Override
    public final void onSecondBound() {
        this.gameMode = GameMode.SECOND_BOUND;
        UpdaterFactory factory = new UpdaterFactory() {
            @Override
            Updater create(GameControllerListener listener,
                    GameControllerEvent event) {
                return new Updater(listener, event) {
                    @Override
                    public void run() {
                        this.getListener().onSecondBound(this.getEvent());
                    }
                };
            }
        };
        callListenerAsync(factory, this.listenerIndex);
    }

    @Override
    public final void onHittable() {
        this.gameMode = GameMode.HITTABLE;
        UpdaterFactory factory = new UpdaterFactory() {
            @Override
            Updater create(GameControllerListener listener,
                    GameControllerEvent event) {
                return new Updater(listener, event) {
                    @Override
                    public void run() {
                        this.getListener().onHittable(this.getEvent());
                    }
                };
            }
        };
        callListenerAsync(factory, this.listenerIndex);
    }

    @Override
    public final void onGoOutOfBounds() {
        this.gameMode = GameMode.GO_OUT_OF_BOUNDS;
        UpdaterFactory factory = new UpdaterFactory() {
            @Override
            Updater create(GameControllerListener listener,
                    GameControllerEvent event) {
                return new Updater(listener, event) {
                    @Override
                    public void run() {
                        this.getListener().onGoOutOfBounds(this.getEvent());
                    }
                };
            }
        };
        callListenerAsync(factory, this.listenerIndex);
    }
    
    @Override
    public final void onServiceable() {
        this.gameMode = GameMode.WAIT;
        UpdaterFactory factory = new UpdaterFactory() {
            @Override
            Updater create(GameControllerListener listener,
                    GameControllerEvent event) {
                return new Updater(listener, event) {
                    @Override
                    public void run() {
                        this.getListener().onServiceable(this.getEvent());
                    }
                };
            }
        };
        callListenerAsync(factory, this.listenerIndex);
    }
}
