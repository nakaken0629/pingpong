package com.itvirtuoso.pingpong.ui;

import android.os.Bundle;
import android.util.Log;

import com.itvirtuoso.pingpong.network.Server;
import com.itvirtuoso.pingpong.network.SocketServer;

public class SocketActivity extends PaddleActivity {
    private static final String TAG = SocketActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SocketServer socketServer = new SocketServerImpl();
        Thread socketThread = new Thread(socketServer);
        socketThread.start();
    }

    class SocketServerImpl extends SocketServer {

        @Override
        public void onJoin() {
            Log.d(TAG, "join");
            SocketActivity.this.join(this);
        }
        
        @Override
        public void onSwinging() {
            /* TODO: do implement */
        }
        
        @Override
        public void onSwinged() {
            /* TODO: do implement */
        }
    }

    private void join(Server server) {
        SocketListener listener = new SocketListener(getGameController(),
                server);
        getGameController().joinGame(listener);
    }
}
