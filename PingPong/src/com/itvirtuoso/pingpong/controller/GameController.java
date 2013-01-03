package com.itvirtuoso.pingpong.controller;

import java.util.ArrayList;

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
    private int listenerIndex;
    private Thread ballThread;

    @Override
    public void newGame(GameControllerListener listener) {
        this.listeners.add(listener);
        this.listenerIndex = 0;
    }

    @Override
    public void joinGame(GameControllerListener listener) {
        listener.setObserver(this);
        this.listeners.add(listener);
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
    public void swing(GameControllerListener hitterListener) {
        if (this.ballThread == null) {
            swingAsService(hitterListener);
        } else {
            swingAsReceive(hitterListener);
        }
    }

    private void swingAsService(GameControllerListener hitterListener) {
        callListenerAsync(new UpdaterFactory() {
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
        }, this.listenerIndex);

        this.listenerIndex = (this.listenerIndex + 1) % this.listeners.size();
        Ball ball = new Ball(this, 200);
        this.ballThread = new Thread(ball);
        this.ballThread.start();
    }
    
    private void swingAsReceive(GameControllerListener hitterListener) {
        this.ballThread.interrupt();
        callListenerAsync(new UpdaterFactory() {
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
        }, this.listenerIndex);
        this.listenerIndex = (this.listenerIndex + 1) % this.listeners.size();
    }

    @Override
    public void onFirstBound() {
        callListenerAsync(new UpdaterFactory() {
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
        }, this.listenerIndex);
    }

    @Override
    public void onSecondBound() {
        callListenerAsync(new UpdaterFactory() {
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
        }, this.listenerIndex);
    }

    @Override
    public void onHittable() {
        callListenerAsync(new UpdaterFactory() {
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
        }, this.listenerIndex);
    }

    @Override
    public void onGoOutOfBounds() {
        callListenerAsync(new UpdaterFactory() {
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
        }, this.listenerIndex);
    }
}
