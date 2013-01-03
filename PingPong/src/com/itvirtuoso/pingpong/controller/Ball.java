package com.itvirtuoso.pingpong.controller;

class Ball implements Runnable {
    private BallObserver observer;
    private long interval;
    private boolean isService = true;

    Ball(BallObserver observer, long interval) {
        this.observer = observer;
        this.interval = interval;
    }

    @Override
    public void run() {
        while (runInner()) {
            this.isService = false;
        }
    }

    private boolean runInner() {
        try {
            Thread.sleep(this.interval);
            if (this.isService) {
                this.observer.onFirstBound();
            }
            Thread.sleep(this.interval);
            if (this.isService) {
                this.observer.onSecondBound();
            } else {
                this.observer.onFirstBound();
            }
            Thread.sleep(this.interval);
            this.observer.onHittable();
            Thread.sleep(this.interval);
            this.observer.onGoOutOfBounds();
            return false;
        } catch (InterruptedException e) {
            return true;
        }
    }
}
