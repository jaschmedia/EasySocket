package me.Jasch.EasySocket.WebSocket;

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

    }

    @Override
    public void onError(WebSocket conn, Exception ex) {

    }
}
