package com.server;

import com.corundumstudio.socketio.AckCallback;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.VoidAckCallback;

/**
 * Created by wangyong on 2017/1/7.
 */
public class ScoketServer {

    public static void main(String[] args) throws InterruptedException {

        Configuration configuration = new Configuration();
        configuration.setHostname("127.0.0.1");
        configuration.setPort(4000);

        final SocketIOServer server = new SocketIOServer(configuration);
        server.addEventListener("", String.class, (client, data, ackSender) -> {

            if (ackSender.isAckRequested()) {
                ackSender.sendAckData("client message is send to server", "hello");
            }

            String msg = data;

            client.sendEvent("", new AckCallback<String>(String.class) {
                @Override
                public void onSuccess(String result) {
                    System.out.println("ack from client" + client.getSessionId() + "data:" + result);
                }
            }, msg);


            client.sendEvent("", new VoidAckCallback() {
                @Override
                protected void onSuccess() {
                    System.out.println("void ack from:" + client.getSessionId());
                }
            }, "message with void ack");


        });

        server.start();
        Thread.sleep(Integer.MAX_VALUE);
        server.stop();


    }
}
