package com.itvirtuoso.pingpong.network;

public interface Server extends Runnable {
    public abstract void accept();
    
    public abstract void hit();
    
    public abstract void firstBound();
    
    public abstract void secondBound();
    
    public abstract void hittable();
    
    public abstract void goOutOfBounds();
    
    public abstract void serviceable();
}
