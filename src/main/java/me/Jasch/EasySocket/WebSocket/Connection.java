package me.Jasch.EasySocket.WebSocket;

import org.java_websocket.WebSocket;

/**
 * A container for a specific WebSocket connection.
 * @author jasch
 * @version 0.1.0
 * @since 0.1.0
 */
public class Connection {

    private String cID = null; // Stores the connection ID.
    private WebSocket ws = null; // WebSocket connection object.
    private ConnectionState state = ConnectionState.NOTINIT; // Connection state

    public Connection(String cID, WebSocket ws) {
        this.cID = cID;
        this.ws = ws;
    }

    /**
     * Set the connection state.
     * @param state Specific state.
     */
    public void setState(ConnectionState state) {
        this.state = state;
    }

    /**
     * Returns the connection state.
     * @return Connection state.
     */
    public ConnectionState getState() {
        return this.state;
    }
}
