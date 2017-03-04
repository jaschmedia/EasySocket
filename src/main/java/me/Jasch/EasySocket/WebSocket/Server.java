package me.Jasch.EasySocket.WebSocket;

import me.Jasch.EasySocket.Exceptions.*;
import me.Jasch.EasySocket.MType.MType;
import me.Jasch.EasySocket.MType.MTypeUtils;
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

    public Server(InetSocketAddress adr, String protocolName) {
        super(adr);
        this.protocolName = protocolName;
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
        conn.send(MTypeUtils.connectionIdSet(cID));
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
        MType mt;
        try {
            mt = MTypeUtils.getMessageType(message);
        } catch (InvalidMTypeException e) {
            log.warn("Invalid MType. Remote: {}, Message: {}",
                    conn.getRemoteSocketAddress().getAddress().getHostAddress(), message);
            // TODO: improve the way errors are handled.
            WSUtils.terminateConnection(conn);
            return;
        }

        // handle the message.
        switch (mt) {
            case ACK:
                // deal with connection ID ACKs
                if (log.isDebugEnabled()) {
                    log.debug("ACK received. Remote: {}, Message: {}",
                            conn.getRemoteSocketAddress().getAddress().getHostAddress(), message);
                }
                try {
                    if (WSUtils.checkCISACK(message)) {
                        String cID = MTypeUtils.getConnectionId(message);
                        if (conns.containsKey(cID)) {
                            conns.get(cID).setState(ConnectionState.CIDACK);
                            WSUtils.sendProtocolInformation(conn, cID, this.protocolName);
                            break; // We are done here.
                        } else {
                            throw new UnknownConnectionIDException();
                        }
                    }
                } catch ( NoConnectionIDException | UnknownConnectionIDException e) {
                    WSUtils.terminateConnection(conn); // Terminate the connection and then quit.
                    break;
                }


                break;
            case NAC:
                // Not implemented.
                if (log.isInfoEnabled()) {
                    log.info("NAC received (NOT IMPLEMENTED). Remote: {}, Message: {}",
                            conn.getRemoteSocketAddress().getAddress().getHostAddress(), message);
                }
                break;
            case ATH:
                // Not implemented.
                if (log.isInfoEnabled()) {
                    log.info("ATH received (NOT IMPLEMENTED). Remote: {}, Message: {}",
                            conn.getRemoteSocketAddress().getAddress().getHostAddress(), message);
                }
                break;
            case ERR:
                log.warn("ERR received. Remote: {}, Message: {}",
                        conn.getRemoteSocketAddress().getAddress().getHostAddress(), message);
                // Closes the WebSocket connection and removes it from the list.
                // TODO: Improved error handling.
                try {
                    String cID = MTypeUtils.getConnectionId(message);
                    conn.close();
                    if (conns.containsKey(cID)) {
                        conns.remove(cID);
                    }
                } catch (NoConnectionIDException e) {
                    conn.close();
                }
                break;
            case CIS:
                log.debug("CIS recieved: ", message);
                // This should never be sent by a client.
                try {
                    String cID = MTypeUtils.getConnectionId(message);
                    WSUtils.terminateConnection(conn, cID);
                    if (conns.containsKey(cID)) {
                        conns.remove(cID);
                    }
                } catch (NoConnectionIDException e) {
                    WSUtils.terminateConnection(conn);
                }
                break;
            case PRT:
                if (log.isDebugEnabled()) {
                    log.debug("PRT received. Remote: {}, Message: {}",
                            conn.getRemoteSocketAddress().getAddress().getHostAddress(), message);
                }
                try {
                    if (WSUtils.checkPRTInformation(message, this.protocolName)) {
                        String cID = MTypeUtils.getConnectionId(message);
                        if (conns.containsKey(cID)) {
                            conns.get(cID).setState(ConnectionState.PRTACK);
                            // TODO: what happens now?!?!
                            break; // we are done here.
                        } else {
                            throw new UnknownConnectionIDException();
                        }
                    }
                } catch (NoConnectionIDException | UnknownConnectionIDException e) {
                    WSUtils.terminateConnection(conn); // Terminate the connection and then quit.
                    break;
                }
                break;
            case RTL:
                // Not implemented.
                if (log.isInfoEnabled()) {
                    log.info("RTL received (NOT IMPLEMENTED). Remote: {}, Message: {}",
                            conn.getRemoteSocketAddress().getAddress().getHostAddress(), message);
                }
                break;
            case PNG:
                // Not implemented.
                if (log.isInfoEnabled()) {
                    log.info("PNG received (NOT IMPLEMENTED). Remote: {}, Message: {}",
                            conn.getRemoteSocketAddress().getAddress().getHostAddress(), message);
                }
                break;
            case POG:
                // Not implemented.
                if (log.isInfoEnabled()) {
                    log.info("POG received (NOT IMPLEMENTED). Remote: {}, Message: {}",
                            conn.getRemoteSocketAddress().getAddress().getHostAddress(), message);
                }
                break;
            case EVT:
                // TODO: Implement event system.
                break;
        }

    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        log.error("Apparently an error occurred: {}", ex);
    }
}
