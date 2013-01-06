package com.itvirtuoso.pingpong.network;

import java.util.ArrayList;

import junit.framework.TestCase;

public class AbstractServerTest extends TestCase {
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
        public void onJoin() {
            /* nop */
        }

        @Override
        public void onSwinging() {
            /* nop */
        }

        @Override
        public void onSwinged() {
            /* nop */
        }

        @Override
        protected void sendMessage(int[] message) {
            /* nop */
        }
    }

    public void testイベントがクライアントに送信される() throws Exception {
        class TestServer extends BaseServer {
            private ArrayList<Integer> wholeMessage = new ArrayList<Integer>();

            protected void sendMessage(int[] message) {
                for (int data : message) {
                    this.wholeMessage.add(data);
                }
            }
        }
        TestServer server = new TestServer();
        server.hit();
        server.firstBound();
        server.secondBound();
        server.hittable();
        server.goOutOfBounds();
        server.serviceable();

        ArrayList<Integer> wholeMessage = server.wholeMessage;
        int wholeIndex = 0;

        int[] hitMessage = { AbstractServer.SEND_HIT, AbstractServer.SEND_END };
        for (int data : hitMessage) {
            assertEquals("hitメッセージが正しく送れていない", data,
                    wholeMessage.get(wholeIndex++).intValue());
        }
        int[] firstBoundMessage = { AbstractServer.SEND_FIRST_BOUND,
                AbstractServer.SEND_END };
        for (int data : firstBoundMessage) {
            assertEquals("firstBoundメッセージが正しく送れていない", data,
                    wholeMessage.get(wholeIndex++).intValue());
        }
        int[] secondBoundMessage = { AbstractServer.SEND_SECOND_BOUND,
                AbstractServer.SEND_END };
        for (int data : secondBoundMessage) {
            assertEquals("secondBoundメッセージが正しく送れていない", data,
                    wholeMessage.get(wholeIndex++).intValue());
        }
        int[] hittableMessage = { AbstractServer.SEND_HITTABLE,
                AbstractServer.SEND_END };
        for (int data : hittableMessage) {
            assertEquals("hittableメッセージが正しく送れていない", data,
                    wholeMessage.get(wholeIndex++).intValue());
        }
        int[] goOutOfBoundsMessage = { AbstractServer.SEND_GO_OUT_OF_BOUNDS,
                AbstractServer.SEND_END };
        for (int data : goOutOfBoundsMessage) {
            assertEquals("goOutOfBoundsメッセージが正しく送れていない", data, wholeMessage
                    .get(wholeIndex++).intValue());
        }
        int[] serviceableMessage = { AbstractServer.SEND_SERVICEABLE,
                AbstractServer.SEND_END };
        for (int data : serviceableMessage) {
            assertEquals("serviceableメッセージが正しく送れていない", data,
                    wholeMessage.get(wholeIndex++).intValue());
        }

        assertEquals("余分なメッセージが送信されている", wholeIndex, wholeMessage.size());
    }

    public void testクライアントからのデータをイベントに変換する() throws Exception {
        class TestServer extends BaseServer {
            private static final String ON_JOIN = "onJoin";
            private static final String ON_SWINGING = "onSwinging";
            private static final String ON_SWINGED = "onSwinged";
            private ArrayList<String> calledMethods = new ArrayList<String>();

            @Override
            public void onJoin() {
                this.calledMethods.add(ON_JOIN);
            }
            
            @Override
            public void onSwinging() {
                this.calledMethods.add(ON_SWINGING);
            }

            @Override
            public void onSwinged() {
                this.calledMethods.add(ON_SWINGED);
            }
        }
        TestServer server = new TestServer();
        int[] joinMessage = { AbstractServer.RECEIVE_JOIN,
                AbstractServer.RECEIVE_END };
        for (int data : joinMessage) {
            server.receiveData(data);
        }
        int[] swingingMessage = { AbstractServer.RECEIVE_SWINGING,
                AbstractServer.RECEIVE_END };
        for (int data : swingingMessage) {
            server.receiveData(data);
        }
        int[] swingedMessage = { AbstractServer.RECEIVE_SWINGED,
                AbstractServer.RECEIVE_END };
        for (int data : swingedMessage) {
            server.receiveData(data);
        }
        
        int index = 0;
        assertEquals("onJoinが呼び出されなかった", TestServer.ON_JOIN, server.calledMethods.get(index++));
        assertEquals("onSwingingが呼び出されなかった", TestServer.ON_SWINGING, server.calledMethods.get(index++));
        assertEquals("onSwingedが呼び出されなかった", TestServer.ON_SWINGED, server.calledMethods.get(index++));
        assertEquals("メソッドの呼び出し回数が間違っている", index, server.calledMethods.size());
    }

    public void test接続を解除する() throws Exception {
        fail("テストを作成する");
    }
}
