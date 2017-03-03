package me.Jasch.EasySocket.WebSocket;

import me.Jasch.EasySocket.Exceptions.*;
import me.Jasch.EasySocket.MType.MType;
import me.Jasch.EasySocket.MType.MTypeUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.java_websocket.WebSocket;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * Utility functions for the WebSocket server.
 * @author jasch
 * @version 0.1.0
 * @since 0.1.0
 */
public class WSUtils {

    /**
     * Generates a unique, 8 character alphanumeric ID for a connection.
     * @return Unique ID
     */
    @NotNull
    public static String generateConnectionId() {
        // just a wrapper that assigns the correct HashMap
        return generateConnectionId(Server.conns);
    }

    /**
     * Generates a unique, 8 character alphanumeric ID for a connection.
     * @param chm connection hashmap the ID will be generated for.
     * @return Unique ID
     */
    @NotNull
    public static String generateConnectionId(HashMap<String, Connection> chm) {
        String cID;
        do {
            cID = RandomStringUtils.randomAlphanumeric(8);
        } while (chm.containsKey(cID));
        return cID;
    }

    /**
     * Checks if a sent ACK message for a CIS message is valid.
     * @param msg The received message.
     * @param ws The affected WebSocket
     * @return true if valid CISACK, false if not.
     * @throws NoConnectionIDException Thrown if message does not contain a connection ID.
     * @throws UnknownConnectionIDException Thrown if the received connection ID is unknown.
     */
    @NotNull
    public static Boolean checkCISACK(String msg, WebSocket ws) throws
            NoConnectionIDException, UnknownConnectionIDException {

        // Boolean container
        Boolean isCisAck = false;

        // get connection ID
        String cID;
        cID = MTypeUtils.getConnectionId(msg);


        // Return null if supplied cID is not known.
        if (!Server.conns.containsKey(cID)) {
            throw new UnknownConnectionIDException();
        }

        Connection c = Server.conns.get(cID);

        // If the given cID is not awaiting an ACK for a CIS terminate
        if (c.getState() == ConnectionState.CIDSENT) {
            isCisAck = true;
        }

        return isCisAck;
    }

    /**
     * Terminates a WebSocket connection.
     * @param ws The affected WebSocket.
     */
    public static void terminateConnection(WebSocket ws) {
        String msg = MTypeUtils.error("GENERIC");
        ws.send(msg);
        ws.close();
    }

    /**
     * Terminates a WebSocket connection and removes it from the list of active connections.
     * @param ws The affected WebSocket.
     * @param cID The affected connection ID.
     */
    public static void terminateConnection(WebSocket ws, String cID) {
        String msg = MTypeUtils.error("GENERIC", cID);
        ws.send(msg);
        ws.close();
        if (Server.conns.containsKey(cID)) {
            Server.conns.remove(cID);
        }
    }
}
