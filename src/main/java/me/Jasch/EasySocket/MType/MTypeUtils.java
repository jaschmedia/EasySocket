package me.Jasch.EasySocket.MType;

import me.Jasch.EasySocket.Exceptions.InvalidMTypeException;
import me.Jasch.EasySocket.Exceptions.NoConnectionIDException;
import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;

import java.util.HashMap;

/**
 * Helper function for dealing with message types.
 * @author jasch
 * @version 0.1.0
 * @since 0.1.0
 */
public final class MTypeUtils {

    private final static HashMap<String, MType> mTKeys = new HashMap<>(); // mapping String->Type for message types.

    static {

        // add all MType into the mapping.
        for (MType mt: MType.values()) {
            mTKeys.put(mt.getDiscrminator(), mt);
        }
    }

    /**
     * Convert a String message type into an actual {@link MType}.
     * @param ts The message type as a string.
     * @return The message type as {@link MType}.
     * @throws InvalidMTypeException If the given message type is invalid or doesn't exist.
     */
    public static MType getMType(String ts) throws InvalidMTypeException {
        MType mt = mTKeys.get(ts);
        if (mt == null) throw new InvalidMTypeException(String.format("The string: \"%s\" is not a valid type.", ts));
        return mt;
    }

    /**
     * Creates an error message to send via WebSocket.
     * @param reason The error reason.
     * @return The message to be sent.
     */
    public static String error(String reason) {
        return MType.ERR.getDiscrminator() + reason;
    }

    /**
     * Creates an error message to send via WebSocket.
     * @param reason The error reason.
     * @param cID The affect connection ID.
     * @return The message to be sent.
     */
    public static String error(String reason, String cID) {
        return MType.ERR.getDiscrminator() + cID + reason;
    }

    /**
     * Creates the connection ID set message.
     * @param cID The connection ID.
     * @return The message to be sent.
     */
    public static String connectionIdSet(String cID) {
        return MType.CIS + cID;
    }

    /**
     * Returns the message type of the message.
     * @param msg The message.
     * @return The type.
     * @throws InvalidMTypeException
     */
    public static MType getMessageType(String msg) throws InvalidMTypeException {
        String mt = null;
        try {
            mt = msg.substring(0,3);
        } catch (Exception e) {
            throw new InvalidMTypeException("An error happened.");
        }
        return getMType(mt);
    }

    /**
     * Returns the connection ID from the message.
     * @param msg The message in question.
     * @return The connection ID.
     * @throws NoConnectionIDException If there is an error while trying to get the ID.
     */
    public static String getConnectionId(String msg) throws NoConnectionIDException {
        String cID = null;
        try {
            cID = msg.substring(3,11);
        } catch (Exception e) {
            throw new NoConnectionIDException();
        }
        return cID;
    }
}
