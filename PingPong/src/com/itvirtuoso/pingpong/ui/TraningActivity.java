package com.itvirtuoso.pingpong.ui;

import android.os.Bundle;

/* TODO: WallActivityに名前を変えること */
public class TraningActivity extends PaddleActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PaddleObserver observer = getGameController();
        WallListener wallListener = new WallListener(observer);
        getGameController().joinGame(wallListener);
    }
}
