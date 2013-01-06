package com.itvirtuoso.pingpong.ui;

import android.util.Log;

import com.itvirtuoso.pingpong.controller.GameControllerEvent;
import com.itvirtuoso.pingpong.controller.GameControllerListener;
import com.itvirtuoso.pingpong.network.Server;

public class SocketListener implements GameControllerListener {
    private static final String TAG = SocketListener.class.getSimpleName();

    private PaddleObserver observer;
    private Server server;

    public SocketListener(PaddleObserver observer, Server server) {
        Log.d(TAG, "SocketListener");
        Log.d(TAG,
                Boolean.toString(observer == null));
        this.observer = observer;
        this.server = server;
    }

    @Override
    public void onHit(GameControllerEvent event) {
        Log.d(TAG, "onHit");
        this.server.hit();
    }

    @Override
    public void onFirstBound(GameControllerEvent event) {
        Log.d(TAG, "onFirstBound");
        this.server.firstBound();
    }

    @Override
    public void onSecondBound(GameControllerEvent event) {
        Log.d(TAG, "onSecondBound");
        this.server.secondBound();
    }

    @Override
    public void onHittable(GameControllerEvent event) {
        Log.d(TAG, "onHittable");
        Log.d(PaddleActivity.class.getSimpleName(),
                Boolean.toString(event.isHitter()));
        this.server.hittable();
        if (!event.isHitter()) {
            return;
        }
        Log.d(TAG, "swing");
        this.observer.swing(this, 100);
    }

    @Override
    public void onGoOutOfBounds(GameControllerEvent event) {
        Log.d(TAG, "onGoOutOfBounds");
        this.server.goOutOfBounds();
    }

    @Override
    public void onServiceable(GameControllerEvent event) {
        Log.d(TAG, "onServiceable");
        this.server.serviceable();
    }

}
