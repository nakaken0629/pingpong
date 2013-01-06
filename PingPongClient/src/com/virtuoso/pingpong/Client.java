package com.virtuoso.pingpong;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {
    private static final String HOST = "192.168.11.5";
    private static final int PORT = 10000;

    public static void main(String[] args) throws Exception {

        Socket socket = null;
        try {
            socket = new Socket();
            InetSocketAddress address = new InetSocketAddress(HOST, PORT);
            socket.connect(address);
            send(socket);
            new Thread(new Receive(socket)).start();
        } catch (IOException e) {
            /* nop */
        }
    }

    private static void closeSocket(Socket socket) {
        try {
            socket.close();
        } catch (IOException e) {
            /* nop */
        }
    }

    private static void send(Socket socket) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(0);
        outputStream.write(255);
    }

    static class Receive implements Runnable {
        private Socket socket;

        public Receive(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                runSafety();
            } catch (IOException e) {
                /* nop */
            }
            closeSocket(socket);
        }

        private void runSafety() throws IOException {
            InputStream inputStream = socket.getInputStream();
            int data = -1;
            do {
                data = inputStream.read();
                System.out.println(data);
            } while (data >= 0);
        }
    }
}
