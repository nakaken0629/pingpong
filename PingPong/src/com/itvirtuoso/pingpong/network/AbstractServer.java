package com.itvirtuoso.pingpong.network;

public abstract class AbstractServer implements Server {
    private static final int MESSAGE_MAX_LENGTH = 10;
    private static final int MESSAGE_INDEX_KIND = 0;
    protected static final int RECEIVE_JOIN = 0;
    protected static final int RECEIVE_END = 255;
    protected static final int SEND_HIT = 0;
    protected static final int SEND_FIRST_BOUND = 1;
    protected static final int SEND_SECOND_BOUND = 2;
    protected static final int SEND_HITTABLE = 3;
    protected static final int SEND_GO_OUT_OF_BOUNDS = 4;
    protected static final int SEND_SERVICEABLE = 5;
    protected static final int SEND_END = 255;

    private Object inputLock = new Object();
    private int[] inputMessage = new int[MESSAGE_MAX_LENGTH];
    private int currentIndex = 0;

    public void receiveData(int data) {
        synchronized (this.inputLock) {
            receiveDataSafety(data);
        }
    }

    private void receiveDataSafety(int data) {
        this.inputMessage[currentIndex] = data;
        currentIndex++;
        if (data != RECEIVE_END) {
            return;
        }
        /* 一つの意味のあるデータ群となった */
        int[] message = new int[this.currentIndex];
        for (int index = 0; index < this.currentIndex; index++) {
            message[index] = this.inputMessage[index];
        }
        this.inputMessage = new int[MESSAGE_MAX_LENGTH];
        this.currentIndex = 0;
        switch (message[MESSAGE_INDEX_KIND]) {
        case RECEIVE_JOIN:
            receiveJoinMessage(message);
            break;
        default:
            /* nop */
        }
    }

    protected void receiveJoinMessage(int[] message) {
        join();
    }

    protected abstract void sendMessage(int[] message);

    @Override
    public void hit() {
        int[] message = { SEND_HIT, SEND_END };
        sendMessage(message);
    }

    @Override
    public void firstBound() {
        int[] message = { SEND_FIRST_BOUND, SEND_END };
        sendMessage(message);
    }

    @Override
    public void secondBound() {
        int[] message = { SEND_SECOND_BOUND, SEND_END };
        sendMessage(message);
    }

    @Override
    public void hittable() {
        int[] message = { SEND_HITTABLE, SEND_END };
        sendMessage(message);
    }

    @Override
    public void goOutOfBounds() {
        int[] message = { SEND_GO_OUT_OF_BOUNDS, SEND_END };
        sendMessage(message);
    }
    
    @Override
    public void serviceable() {
        int[] message = { SEND_SERVICEABLE, SEND_END };
        sendMessage(message);
    }
}
