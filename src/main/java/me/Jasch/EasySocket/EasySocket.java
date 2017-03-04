package me.Jasch.EasySocket;

import me.Jasch.EasySocket.Event.EventHandler;
import me.Jasch.EasySocket.Message.Message;
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
public class EasySocket extends Thread {

    private static final Logger log = LoggerFactory.getLogger(EasySocket.class); // logger instance

    Server wsS; // the used WebSocket server.
    /**
     * The identifier of the protocol used.
     */
    String protocolName;
    EventHandler handler; // Event Handler
    InetSocketAddress wsAddress; // the address for the WebSocket server.


    public EasySocket(InetSocketAddress addr, String protocolName) {
        this.protocolName = protocolName;
        this.handler = new EventHandler();
        this.wsS = new Server(addr, protocolName, this.handler);
        log.info("EasySocket created. Address: {}, Protocol: {}", addr.toString(), protocolName);

    }

    public void start() {
        this.wsS.start();
    }

    /**
     * Get the event handler for this instance.
     * @return The event handler.
     */
    public EventHandler getHandler() {
        return this.handler;
    }


    /**
     * Send a message to a client.
     * @param msg The message to send.
     */
    public void sendEvent(Message msg) {
        this.wsS.sendMessage(msg);
    }


    @Override
    public void run() {

    }
}
