package com.itvirtuoso.pingpong.network;

import java.util.ArrayList;
import java.util.Calendar;

import junit.framework.TestCase;

import com.itvirtuoso.pingpong.controller.GameControllerEvent;
import com.itvirtuoso.pingpong.controller.GameControllerFactory;
import com.itvirtuoso.pingpong.controller.GameControllerListener;
import com.itvirtuoso.pingpong.ui.PaddleObserver;

public class AbstractServerTest extends TestCase {
    class BaseObserver implements PaddleObserver {
        @Override
        public void newGame(GameControllerListener listener) {
            /* nop */
        }

        @Override
        public void joinGame(GameControllerListener listener) {
            /* nop */
        }

        @Override
        public void swing(GameControllerListener listener, long interval) {
            /* nop */
        }
    }

    class BaseServer extends AbstractServer {
        @Override
        public void run() {
            /* nop */
        }

        @Override
        public void accept() {
            /* nop */
        }

        @Override
        public void join() {
            /* nop */
        }

        @Override
        protected void sendMessage(int[] message) {
            /* nop */
        }
    }

    class BaseListener implements GameControllerListener {

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
            /* nop */
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

    public void testクライアントからのリクエストでゲームに参加する() throws Exception {
        class TestServer extends BaseServer {
            private boolean isCallReceiveJoinMessage = false;

            protected void receiveJoinMessage(int[] message) {
                this.isCallReceiveJoinMessage = true;
            }

            public void onReceiveJoinMessage() {
                receiveData(AbstractServer.RECEIVE_JOIN);
                receiveData(AbstractServer.RECEIVE_END);
            }
        }
        TestServer server = new TestServer();
        server.onReceiveJoinMessage();
        assertTrue("リスナーを作るメソッドがコールバックされなかった", server.isCallReceiveJoinMessage);
    }

    public void testイベントがクライアントに送信される() throws Exception {
        class TestServer extends BaseServer {
            private ArrayList<Integer> wholeMessage = new ArrayList<Integer>();

            public void onReceiveJoinMessage() {
                receiveData(AbstractServer.RECEIVE_JOIN);
                receiveData(AbstractServer.RECEIVE_END);
            }

            protected void sendMessage(int[] message) {
                for (int data : message) {
                    this.wholeMessage.add(data);
                }
            }
        }
        class TestServerListener extends BaseListener {
            class TestClientListener extends BaseListener {
                private Server server;
                private boolean isCallOnServiceable = false;

                public TestClientListener(PaddleObserver observer, Server server) {
                    this.server = server;
                }

                @Override
                public void onHit(GameControllerEvent event) {
                    this.server.hit();
                }

                @Override
                public void onFirstBound(GameControllerEvent event) {
                    this.server.firstBound();
                }

                @Override
                public void onSecondBound(GameControllerEvent event) {
                    this.server.secondBound();
                }

                @Override
                public void onHittable(GameControllerEvent event) {
                    this.server.hittable();
                }

                @Override
                public void onGoOutOfBounds(GameControllerEvent event) {
                    this.server.goOutOfBounds();
                }

                @Override
                public void onServiceable(GameControllerEvent event) {
                    this.server.serviceable();
                    if (event.isHitter()) {
                        this.isCallOnServiceable = true;
                    }
                }
            }

            private PaddleObserver observer;
            private TestServer testServer;
            private TestClientListener clientListener;

            public void onCreate() {
                this.observer = GameControllerFactory.create();
                this.observer.newGame(this);
                this.testServer = new TestServer() {
                    public void join() {
                        TestServerListener.this.join(this);
                    }
                };
                Thread thread = new Thread(this.testServer);
                thread.start();
            }

            private void join(Server server) {
                this.clientListener = new TestClientListener(this.observer,
                        server);
                observer.joinGame(clientListener);
            }

            public void onSwing() {
                this.observer.swing(this, 10);
            }
        }

        TestServerListener serverListener = new TestServerListener();
        serverListener.onCreate();
        TestServer testServer = serverListener.testServer;
        testServer.onReceiveJoinMessage();
        serverListener.onSwing();

        long beginTime = Calendar.getInstance().getTimeInMillis();
        while (!serverListener.clientListener.isCallOnServiceable) {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - beginTime > 3000) {
                throw new Exception("三秒待っても処理が終わらなかった");
            }
            Thread.sleep(10);
        }

        ArrayList<Integer> wholeMessage = testServer.wholeMessage;
        int wholeIndex = 0;

        int[] hitMessage = { AbstractServer.SEND_HIT, AbstractServer.SEND_END };
        for (int index = 0; index < hitMessage.length; index++) {
            assertEquals("hitメッセージが正しく送れていない", hitMessage[index], wholeMessage
                    .get(wholeIndex++).intValue());
        }
        int[] firstBoundMessage = { AbstractServer.SEND_FIRST_BOUND,
                AbstractServer.SEND_END };
        for (int index = 0; index < firstBoundMessage.length; index++) {
            assertEquals("firstBoundメッセージが正しく送れていない", firstBoundMessage[index],
                    wholeMessage.get(wholeIndex++).intValue());
        }
        int[] secondBoundMessage = { AbstractServer.SEND_SECOND_BOUND,
                AbstractServer.SEND_END };
        for (int index = 0; index < secondBoundMessage.length; index++) {
            assertEquals("secondBoundメッセージが正しく送れていない",
                    secondBoundMessage[index], wholeMessage.get(wholeIndex++)
                            .intValue());
        }
        int[] hittableMessage = { AbstractServer.SEND_HITTABLE,
                AbstractServer.SEND_END };
        for (int index = 0; index < hittableMessage.length; index++) {
            assertEquals("hittableメッセージが正しく送れていない", hittableMessage[index],
                    wholeMessage.get(wholeIndex++).intValue());
        }
        int[] goOutOfBoundsMessage = { AbstractServer.SEND_GO_OUT_OF_BOUNDS,
                AbstractServer.SEND_END };
        for (int index = 0; index < goOutOfBoundsMessage.length; index++) {
            assertEquals("goOutOfBoundsメッセージが正しく送れていない",
                    goOutOfBoundsMessage[index], wholeMessage.get(wholeIndex++)
                            .intValue());
        }
        int[] serviceableMessage = { AbstractServer.SEND_SERVICEABLE,
                AbstractServer.SEND_END };
        for (int index = 0; index < serviceableMessage.length; index++) {
            assertEquals("serviceableメッセージが正しく送れていない",
                    serviceableMessage[index], wholeMessage.get(wholeIndex++)
                            .intValue());
        }

        assertEquals("余分なメッセージが送信されている", wholeIndex, wholeMessage.size());
    }

    public void test接続を解除する() throws Exception {
        class TestServer extends BaseServer {
            public void onReceiveJoinMessage() {
                receiveData(AbstractServer.RECEIVE_JOIN);
                receiveData(AbstractServer.RECEIVE_END);
            }
        }
        TestServer server = new TestServer();
        server.onReceiveJoinMessage();

        fail("接続を解除するテストを作成する");
    }
}
