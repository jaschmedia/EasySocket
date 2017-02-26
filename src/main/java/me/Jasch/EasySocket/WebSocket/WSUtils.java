package me.Jasch.EasySocket.WebSocket;

import me.Jasch.EasySocket.Exceptions.InvalidMTypeException;
import me.Jasch.EasySocket.Exceptions.NoConnectionIDException;
import me.Jasch.EasySocket.MType.MType;
import me.Jasch.EasySocket.MType.MTypeUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.java_websocket.WebSocket;

import java.util.HashMap;

/**
 * @author jasch
 * @version 0.1.0
 * @since 0.1.0
 */
public class WSUtils {

    /**
     * Generates a unique, 8 character alphanumeric ID for a connection.
     * @return Unique ID
     */
    public static String generateConnectionId() {
        // just a wrapper that assigns the correct HashMap
        return generateConnectionId(Server.conns);
    }

    /**
     * Generates a unique, 8 character alphanumeric ID for a connection.
     * @param chm connection hashmap the ID will be generated for.
     * @return Unique ID
     */
    public static String generateConnectionId(HashMap<String, Connection> chm) {
        String cID;
        do {
            cID = RandomStringUtils.randomAlphanumeric(8);
        } while (chm.containsKey(cID));
        return cID;
    }

    public Boolean checkCISACK(String msg, WebSocket ws) {
        Boolean state = false;

        // Test if it's an ACK message
        MType mt = null;
        try {
            mt = MTypeUtils.getMessageType(msg);
        } catch (InvalidMTypeException e) {
            return null;
        }

        if (mt != MType.ACK) {
            return null;
        }

        // get connection ID
        String cID = null;
        try {
            cID = MTypeUtils.getConnectionId(msg);
        } catch (NoConnectionIDException e) {
            return null;
        }

        if (!Server.conns.containsKey(cID)) {
            return null;
        }

        return false; // TODO: FINISH THIS TOMORROW...
    }
}
