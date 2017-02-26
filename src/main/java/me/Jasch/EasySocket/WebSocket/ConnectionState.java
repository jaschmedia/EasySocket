package me.Jasch.EasySocket.WebSocket;

/**
 * Created by jasch on 26/02/17.
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
