package me.Jasch.EasySocket.WebSocket;

import me.Jasch.EasySocket.Exceptions.*;
import me.Jasch.EasySocket.MType.MType;
import me.Jasch.EasySocket.MType.MTypeUtils;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.HashMap;

/**
 * Implements the functionality of the WebSocket server.
 * @author jasch
 * @version 0.1.0
 * @since 0.1.0
 */
public class Server extends WebSocketServer {

    public static HashMap<String, Connection> conns = new HashMap<>(); // connection, identified by their connection ID.

    public Server(InetSocketAddress adr) {
        super(adr);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        String cID = WSUtils.generateConnectionId();
        Connection cn = new Connection(cID, conn);
        conns.put(cID, cn);
        conn.send(MTypeUtils.connectionIdSet(cID));
        cn.setState(ConnectionState.CIDSENT);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {

    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        MType mt;
        try {
            mt = MTypeUtils.getMessageType(message);
        } catch (InvalidMTypeException e) {
            // TODO: improve the way errors are handled.
            WSUtils.terminateConnection(conn);
            return;
        }

        // handle the message.
        switch (mt) {
            case ACK:
                // deal with connection ID ACKs
                try {
                    if (WSUtils.checkCISACK(message, conn)) {
                        String cID = MTypeUtils.getConnectionId(message);
                        if (conns.containsKey(cID)) {
                            conns.get(cID).setState(ConnectionState.CIDACK);
                            // TODO: call protocol communication functions.
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
                break;
            case ATH:
                // Not implemented.
                break;
            case ERR:
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
                // TODO: Implement protocol communication.
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
                // TODO: Implement event system.
                break;
        }

    }

    @Override
    public void onError(WebSocket conn, Exception ex) {

    }
}
