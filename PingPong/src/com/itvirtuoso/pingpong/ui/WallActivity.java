package com.itvirtuoso.pingpong.ui;

import android.os.Bundle;

public class WallActivity extends PaddleActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PaddleObserver observer = getGameController();
        WallListener wallListener = new WallListener(observer);
        getGameController().joinGame(wallListener);
    }
}
