package com.itvirtuoso.pingpong.controller;

import java.util.ArrayList;

import android.util.Log;

import com.itvirtuoso.pingpong.model.Paddle;

public abstract class AbstractGameController implements GameController {
    class ServeRunnable implements Runnable {
        private long interval;
        private boolean isServe;

        public ServeRunnable(long interval, boolean isServe) {
            this.interval = interval;
            this.isServe = isServe;
        }

        @Override
        public void run() {
            Log.d("ServeRunnable", "begin run");
            runnInner();
            Log.d("ServeRunnable", "end run");
        }
        private void runnInner() {
            AbstractGameController self = AbstractGameController.this;
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
                self.ball = null;
            }
        }
    }

    private Object paddlesLock = new Object();
    private ArrayList<Paddle> paddles = new ArrayList<Paddle>();
    private Thread ball;
    private int hitterIndex = 0;

    protected void addPaddle(Paddle paddle) {
        synchronized (this.paddlesLock) {
            this.paddles.add(paddle);
        }
    }
    
    protected Runnable createBall(long interval, boolean isServe) {
        return new ServeRunnable(interval, isServe);
    }
    
    @Override
    public boolean isPlaying() {
        return this.ball != null;
    }
    
    @Override
    public Paddle newGame(PaddleListener listener) {
        Paddle paddle = new Paddle(this, listener);
        addPaddle(paddle);
        return paddle;
    }

    @Override
    public Paddle joinGame(PaddleListener listener) {
        return newGame(listener);
    }

    @Override
    public void serve(Paddle paddle, long interval) {
        Runnable serve = createBall(interval, true);
        this.ball = new Thread(serve);
        this.ball.start();
    }

    @Override
    public void receive(Paddle paddle, long interval) {
        this.ball.interrupt();
        Runnable receive = createBall(interval, false);
        this.ball = new Thread(receive);
        this.ball.start();
    }
}
