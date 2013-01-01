package com.itvirtuoso.pingpong.controller;

import java.util.ArrayList;

import com.itvirtuoso.pingpong.model.Paddle;

public class GameController {
    class ServeRunnable implements Runnable {
        private long interval;
        private boolean isServe;

        public ServeRunnable(long interval, boolean isServe) {
            this.interval = interval;
            this.isServe = isServe;
        }

        @Override
        public void run() {
            GameController self = GameController.this;
            self.hitterIndex = (self.hitterIndex + 1) % self.paddles.size();
            synchronized (self.paddlesLock) {
                for (Paddle paddle2 : self.paddles) {
                    boolean isHitter = paddle2.equals(self.paddles
                            .get(hitterIndex));
                    PaddleEvent event = new PaddleEvent(isHitter);
                    paddle2.onHit(event);
                }
                try {
                    Thread.sleep(this.interval);
                } catch (InterruptedException e) {
                    return;
                }
                if (this.isServe) {
                    for (Paddle paddle2 : self.paddles) {
                        boolean isHitter = paddle2.equals(self.paddles
                                .get(hitterIndex));
                        PaddleEvent event = new PaddleEvent(isHitter);
                        paddle2.onFirstBound(event);
                    }
                }
                try {
                    Thread.sleep(this.interval);
                } catch (InterruptedException e) {
                    return;
                }
                for (Paddle paddle2 : self.paddles) {
                    boolean isHitter = paddle2.equals(self.paddles
                            .get(hitterIndex));
                    PaddleEvent event = new PaddleEvent(isHitter);
                    if (this.isServe) {
                        paddle2.onSecondBound(event);
                    } else {
                        paddle2.onFirstBound(event);
                    }
                }
                try {
                    Thread.sleep(this.interval);
                } catch (InterruptedException e) {
                    return;
                }
                for (Paddle paddle2 : self.paddles) {
                    boolean isHitter = paddle2.equals(self.paddles
                            .get(hitterIndex));
                    PaddleEvent event = new PaddleEvent(isHitter);
                    paddle2.onHittable(event);
                }
                try {
                    Thread.sleep(this.interval);
                } catch (InterruptedException e) {
                    return;
                }
                for (Paddle paddle2 : self.paddles) {
                    boolean isHitter = paddle2.equals(self.paddles
                            .get(hitterIndex));
                    PaddleEvent event = new PaddleEvent(isHitter);
                    paddle2.onGoOutOfBounds(event);
                }
            }
        }
    }

    private Object paddlesLock = new Object();
    private ArrayList<Paddle> paddles = new ArrayList<Paddle>();
    private Thread ball;
    private int hitterIndex = 0;

    public boolean isPlaying() {
        return this.ball != null;
    }
    
    public Paddle newGame(PaddleListener listener) {
        Paddle paddle = new Paddle(this, listener);
        synchronized (this.paddlesLock) {
            paddles.add(paddle);
        }
        return paddle;
    }

    public Paddle joinGame(PaddleListener listener) {
        return newGame(listener);
    }

    public void serve(Paddle paddle, long interval) {
        this.ball = new Thread(new ServeRunnable(interval, true));
        this.ball.start();
    }

    public void receive(Paddle paddle, long interval) {
        this.ball.interrupt();
        this.ball = new Thread(new ServeRunnable(interval, false));
        this.ball.start();
    }
}
