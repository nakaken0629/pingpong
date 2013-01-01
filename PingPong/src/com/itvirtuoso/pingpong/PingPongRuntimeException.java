package com.itvirtuoso.pingpong;

public class PingPongRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PingPongRuntimeException() {
        super();
    }

    public PingPongRuntimeException(String detailMessage) {
        super(detailMessage);
    }

    public PingPongRuntimeException(Throwable throwable) {
        super(throwable);
    }

    public PingPongRuntimeException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

}
