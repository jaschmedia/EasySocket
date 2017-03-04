package me.Jasch.EasySocket;

import me.Jasch.EasySocket.WebSocket.Server;

import java.net.InetSocketAddress;

/**
 * Empty so far
 * @author jasch
 * @version 0.1.0
 * @since 0.1.0
 */
public class EasySocket {
    Server wsS; // the used WebSocket server.
    /**
     * The identifier of the protocol used.
     */
    String protocolName;
    InetSocketAddress wsAddress; // the address for the WebSocket server.

    public static void main(String[] args) {
        EasySocket es = new EasySocket();
        es.startServer();

    }

    public EasySocket() {

    }

    public void startServer() {
        this.wsAddress = new InetSocketAddress("localhost", 8000);
        this.wsS = new Server(this.wsAddress, "testabc");
        this.wsS.start();
    }


}
