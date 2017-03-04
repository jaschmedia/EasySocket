package me.Jasch.EasySocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import me.Jasch.EasySocket.WebSocket.Server;

import java.net.InetSocketAddress;

/**
 * Empty so far
 * @author jasch
 * @version 0.1.0
 * @since 0.1.0
 */
public class EasySocket {

    private static final Logger log = LoggerFactory.getLogger(EasySocket.class); // logger instance

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
        String adr = "localhost";
        int port = 8000;
        protocolName = "testabc";
        this.wsAddress = new InetSocketAddress(adr, port);
        this.wsS = new Server(this.wsAddress, protocolName);
        this.wsS.start();
        log.info("EasySocket started. Address: {}, Protocol: {}", (adr + ":" + port), protocolName);
    }


}
