package me.Jasch.EasySocket.MType;

import me.Jasch.EasySocket.Exceptions.InvalidMTypeException;
import me.Jasch.EasySocket.Exceptions.NoConnectionIDException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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
            mTKeys.put(mt.discriminator, mt);
        }
    }

    /**
     * Convert a String message type into an actual {@link MType}.
     * @param ts The message type as a string.
     * @return The message type as {@link MType}.
     * @throws InvalidMTypeException If the given message type is invalid or doesn't exist.
     */
    @NotNull
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
    @NotNull
    @Contract(pure = true)
    public static String error(String reason) {
        return MType.ERR.discriminator + reason;
    }

    /**
     * Creates an error message to send via WebSocket.
     * @param reason The error reason.
     * @param cID The affect connection ID.
     * @return The message to be sent.
     */
    @NotNull
    @Contract(pure = true)
    public static String error(String reason, String cID) {
        return MType.ERR.discriminator + cID + reason;
    }

    /**
     * Creates the connection ID set message.
     * @param cID The connection ID.
     * @return The message to be sent.
     */
    @NotNull
    @Contract(pure = true)
    public static String connectionIdSet(String cID) {
        return MType.CIS.discriminator + cID;
    }

    /**
     * Creates a protocol message.
     * @param cID Affected Connection ID.
     * @param protocolName Protocol name.
     * @return The message to be sent.
     */
    @NotNull
    @Contract(pure = true)
    public static String protocolMessage(String cID, String protocolName) {
        return MType.PRT.discriminator + cID + protocolName;
    }

    /**
     * Returns the message type of the message.
     * @param msg The message.
     * @return The type.
     * @throws InvalidMTypeException Thrown if the message does not contain a proper message type indicator.
     */
    @NotNull
    public static MType getMessageType(String msg) throws InvalidMTypeException {
        String mt;
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
        String cID;
        try {
            cID = msg.substring(3,11);
        } catch (Exception e) {
            throw new NoConnectionIDException();
        }
        return cID;
    }

    /**
     * Returns the payload of a message.
     * @param msg The message.
     * @return The payload of the message, or an empty string if retrieving the payload failed.
     */
    @NotNull
    public static String getMessagePayload(String msg) {
        String payload;
        try {
            payload = msg.substring(11,msg.length());
        } catch (Exception e) {
            payload = "";
        }
        return payload;
    }
}
