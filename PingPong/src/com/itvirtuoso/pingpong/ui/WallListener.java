package com.itvirtuoso.pingpong.ui;

import com.itvirtuoso.pingpong.controller.GameControllerEvent;
import com.itvirtuoso.pingpong.controller.GameControllerListener;

/**
 * 常に同じ力でクロスに打ち返す壁
 * @author kenji
 *
 */
public class WallListener implements GameControllerListener {
    private PaddleObserver observer;
    
    public WallListener(PaddleObserver observer) {
        this.observer = observer;
    }

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
        if(!event.isHitter()) {
            return;
        }
        this.observer.swing(this, 100);
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
