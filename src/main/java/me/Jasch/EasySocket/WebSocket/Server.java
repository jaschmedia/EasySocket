package me.Jasch.EasySocket.WebSocket;

import me.Jasch.EasySocket.EasySocket;
import me.Jasch.EasySocket.Event.EventHandler;
import me.Jasch.EasySocket.Exceptions.*;
import me.Jasch.EasySocket.Message.MType;
import me.Jasch.EasySocket.Message.Message;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.net.InetSocketAddress;
import java.util.HashMap;

/**
 * Implements the functionality of the WebSocket server.
 * @author jasch
 * @version 0.1.0
 * @since 0.1.0
 */
public class Server extends WebSocketServer {

    private static final Logger log = LoggerFactory.getLogger(Server.class); // logger instance
    public static HashMap<String, Connection> conns = new HashMap<>(); // connection, identified by their connection ID.
    private String protocolName;
    private EventHandler handler;

    public Server(InetSocketAddress adr, String protocolName, EventHandler handler) {
        super(adr);
        this.protocolName = protocolName;
        this.handler = handler;
        if (log.isDebugEnabled()) { log.debug("WebSocket server initialised."); }
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        if (log.isDebugEnabled()) {
            log.debug("Connection opened. Remote: {}", conn.getRemoteSocketAddress().getAddress().getHostAddress());
        }
        String cID = WSUtils.generateConnectionId();
        Connection cn = new Connection(cID, conn);
        conns.put(cID, cn);
        Message msg = new Message(MType.CIS, cID);
        conn.send(msg.toString());
        cn.setState(ConnectionState.CIDSENT);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        if (log.isDebugEnabled()) {
            log.debug("Connection closed. Code: {}, Remote: {}", code,
                    conn.getRemoteSocketAddress().getAddress().getHostAddress());
        }

    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        Message msg;
        try {
            msg = new Message(message);
        } catch (InvalidMTypeException | NoConnectionIDException e) {
            log.warn("Malformed message. Remote: {}, Message: {}",
                    conn.getRemoteSocketAddress().getAddress().getHostAddress(), message);
            // TODO: Deal with malformed message.
            return;
        }

        //<editor-fold desc="Log incoming messages.">
        switch (msg.mt) {

            case ERR:
                log.warn("ERR received. Remote: {}, Message: {}",
                        conn.getRemoteSocketAddress().getAddress().getHostAddress(), message);
                break;
            case CIS: case PRT: case EVT:
                if (log.isDebugEnabled()) {
                    log.debug("{} received. Remote: {}, Message: {}", msg.mt.discriminator,
                            conn.getRemoteSocketAddress().getAddress().getHostAddress(), message);
                }
                break;
            default:
                if (log.isInfoEnabled()) {
                    log.info("{} received. Remote: {}, Message: {}", msg.mt.discriminator,
                            conn.getRemoteSocketAddress().getAddress().getHostAddress(), message);
                }
                break;
        }
        //</editor-fold>

        // handle the message.
        switch (msg.mt) {
            case ACK:
                break;
            case NAC:
                // Not implemented.
                break;
            case ATH:
                // Not implemented.
                break;
            case ERR:
                // TODO: improved error handling.
                // Closes the WebSocket connection and removes it from the list.
                Message sendMsg = new Message(MType.ERR, msg.cID, "GenericError");
                conn.send(sendMsg.toString());
                conn.close();
                if (conns.containsKey(msg.cID)) {
                    conns.remove(msg.cID);
                }
                break;
            case CIS:
                try {
                    if (WSUtils.checkCISInformation(msg)) {
                        conns.get(msg.cID).setState(ConnectionState.CIDACK);
                        WSUtils.sendProtocolInformation(conn, msg.cID, this.protocolName);
                    } else {
                        // TODO: more graceful handling of a failed CIS handshake.
                        WSUtils.terminateConnection(conn);
                    }
                } catch ( UnknownConnectionIDException e) {
                    WSUtils.terminateConnection(conn); // Terminate the connection and then quit.
                    break;
                }
                break;
            case PRT:
                try {
                    if (WSUtils.checkPRTInformation(msg, this.protocolName)) {
                        conns.get(msg.cID).setState(ConnectionState.PRTACK);
                    } else {
                        // TODO: more graceful handling of a failed PRT handshake.
                        WSUtils.terminateConnection(conn, msg.cID);
                        if (conns.containsKey(msg.cID)) {
                            conns.remove(msg.cID);
                        }
                    }
                } catch (NoConnectionIDException | UnknownConnectionIDException e) {
                    WSUtils.terminateConnection(conn); // Terminate the connection and then quit.
                    break;
                }
                break;
            case RTL:
                // Not implemented.
                break;
            case PNG:
                // Not implemented.
                break;
            case POG:
                // Not implemented.
                break;
            case EVT:
                if (this.handler.dispatchEvent(msg)) {
                    // TODO: improve logging.
                    log.debug("Dispatched event.");
                } else {
                    log.warn("Failed to dispatch event.");
                }
                break;
        }

    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        log.error("Apparently an error occurred: {}", ex);
    }

    /**
     * Sends a message event to the client specified by the connection ID.
     * @param msg The message.
     */
    public void sendMessage(Message msg) {
        // TODO: implement.
    }
}
