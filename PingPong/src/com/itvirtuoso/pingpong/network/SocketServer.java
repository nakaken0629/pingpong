package com.itvirtuoso.pingpong.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class SocketServer extends AbstractServer {
    private static final int DEFAULT_PORT = 10000;
    private Object outputStreamLock = new Object();
    private OutputStream outputStream;

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(DEFAULT_PORT);
            Socket socket = serverSocket.accept();
            this.outputStream = socket.getOutputStream();
            SocketServer.this.runInner(socket.getInputStream());
        } catch (Exception e) {
            /* TODO: 通信エラーの対応を考える */
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e1) {
                    /* nop */
                }
            }
        }
    }

    private void runInner(InputStream inputStream) throws IOException {
        int data = -1;
        do {
            data = inputStream.read();
            receiveData(data);
        } while (data >= 0);
    }

    @Override
    protected void sendMessage(int[] message) {
        synchronized (this.outputStreamLock) {
            try {
                for(int index = 0; index < message.length; index++) {
                    this.outputStream.write(message[index]);
                }
                this.outputStream.flush();
            } catch (IOException e) {
                /* TODO: 通信エラーの対応を考える */
            }
        }
    }
}
