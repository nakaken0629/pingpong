package com.itvirtuoso.pingpong.model;

import com.itvirtuoso.pingpong.controller.GameController;
import com.itvirtuoso.pingpong.controller.PaddleEvent;

public class AutoPaddle extends Paddle {

    public AutoPaddle(GameController controller) {
        super(controller, null);
    }
    
    @Override
    public void onHit(PaddleEvent event) {
        /* nop */
    }
    
    @Override
    public void onFirstBound(PaddleEvent event) {
        /* nop */
    }
    
    @Override
    public void onSecondBound(PaddleEvent event) {
        /* nop */
    }

    @Override
    public void onHittable(PaddleEvent event) {
        if (!event.isHitter()) {
            return;
        }
        swing();
    }
    
    @Override
    public void onGoOutOfBounds(PaddleEvent event) {
        /* nop */
    }
}
