package com.itvirtuoso.pingpong.controller;

class Ball implements Runnable {
    private BallObserver observer;
    private long interval;
    
    Ball(BallObserver observer, long interval) {
        this.observer = observer;
        this.interval = interval;
    }
    
    @Override
    public void run() {
        try {
            Thread.sleep(this.interval);
            this.observer.onFirstBound();
            Thread.sleep(this.interval);
            this.observer.onSecondBound();
            Thread.sleep(this.interval);
            this.observer.onHittable();
            Thread.sleep(this.interval);
            this.observer.onGoOutOfBounds();
        } catch (InterruptedException e) {
            /* ボールを打ち返したとき中断する */
            return;
        }
    }

}
