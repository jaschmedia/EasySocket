package me.Jasch.EasySocket.WebSocket;

/**
 * Enum to keep track of the state of a connection.
 * @author jasch
 * @version 0.1.0
 * @since 0.1.0
 */
public enum ConnectionState {
    NOTINIT, // connection not inited
    CIDSENT, // connection ID sent
    CIDACK, // connection ID acknowledged
    PRTSENT, // protocol information sent
    PRTACK, // protocol information acknowledged
    AUTHINIT, // authentication process initialised
    AUTHED, // authenticated
    GOOD, // connection fine and good to go
}
