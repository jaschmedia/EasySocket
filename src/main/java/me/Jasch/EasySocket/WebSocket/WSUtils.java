package me.Jasch.EasySocket.WebSocket;

import me.Jasch.EasySocket.Exceptions.*;
import me.Jasch.EasySocket.Message.MType;
import me.Jasch.EasySocket.Message.Message;
import org.apache.commons.lang.RandomStringUtils;
import org.java_websocket.WebSocket;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * Utility functions for the WebSocket server.
 * @author jasch
 * @version 0.1.0
 * @since 0.1.0
 */
public class WSUtils {

    private static final Logger log = LoggerFactory.getLogger(WSUtils.class); // logger instance

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
        if (log.isDebugEnabled()) {log.debug("Generated connection ID: {}", cID);}
        return cID;
    }

    /**
     * Checks the answer for a CIS message.
     * @param msg The received message.
     * @return True if the connection ID was set correctly, false otherwise.
     * @throws UnknownConnectionIDException Thrown if the received connection ID is unknown.
     */
    @NotNull
    public static Boolean checkCISInformation(Message msg) throws UnknownConnectionIDException {
        // Return null if supplied cID is not known.
        if (!Server.conns.containsKey(msg.cID)) { throw new UnknownConnectionIDException();}

        Connection c = Server.conns.get(msg.cID);

        return (c.getState() == ConnectionState.CIDSENT);
    }

    /**
     * Terminates a WebSocket connection.
     * @param ws The affected WebSocket.
     */
    public static void terminateConnection(WebSocket ws) {
        if (log.isDebugEnabled()) {
            log.debug("Terminating connection. Remote: {}", ws.getRemoteSocketAddress().getAddress().getHostAddress());
        }
        ws.close();
    }

    /**
     * Terminates a WebSocket connection and removes it from the list of active connections.
     * @param ws The affected WebSocket.
     * @param cID The affected connection ID.
     */
    public static void terminateConnection(WebSocket ws, String cID) {
        Message msg = new Message(MType.ERR, cID, "GenericError");
        ws.send(msg.toString());
        ws.close();
        if (Server.conns.containsKey(cID)) {
            Server.conns.remove(cID);
        }
    }

    /**
     * Sends protocol information and saves state to connection.
     * @param ws The affected WebSocket.
     * @param cID The affected Connection ID.
     * @param protocolName The protocol name.
     */
    public static void sendProtocolInformation(WebSocket ws, String cID, String protocolName) {
        Message msg = new Message(MType.PRT, cID, protocolName);
        ws.send(msg.toString());
        if (Server.conns.containsKey(cID)) {
            Server.conns.get(cID).setState(ConnectionState.PRTSENT);
        }
    }

    /**
     * Checks if the returned protocol information is good.
     * @param msg The received message.
     * @param protocolName The expected protocol name.
     * @return True if the received protocol information matches the expected information.
     * @throws UnknownConnectionIDException Thrown if the received connection ID is unknown.
     */
    @NotNull
    public static Boolean checkPRTInformation(Message msg, String protocolName) throws
            NoConnectionIDException, UnknownConnectionIDException {
        // Throw exception if connection ID is unknown.
        if (!Server.conns.containsKey(msg.cID)) { throw new UnknownConnectionIDException(); }

        Connection c = Server.conns.get(msg.cID);

        return c.getState() == ConnectionState.PRTSENT && msg.payload.equals(protocolName);
    }
}
