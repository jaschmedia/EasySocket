package me.Jasch.EasySocket.Message;

import me.Jasch.EasySocket.Exceptions.InvalidMTypeException;
import me.Jasch.EasySocket.Exceptions.NoConnectionIDException;
import me.Jasch.EasySocket.Exceptions.UnknownConnectionIDException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static me.Jasch.EasySocket.WebSocket.WSUtils.*;

/**
 * Message object.
 * @author jasch
 * @version 0.1.0
 * @since 0.1.0
 */
public class Message {

    private static final Logger log = LoggerFactory.getLogger(Message.class); // logger instance for message

    public MType mt; // the message type
    public String cID; // connection ID
    public String payload; // payload of the message

    /**
     * Creates a new message from a message type and a connection ID.
     * @param mt The message type.
     * @param cID The connection ID.
     * @throws UnknownConnectionIDException Thrown if the connection ID is unknown.
     */
    public Message(MType mt, String cID) throws UnknownConnectionIDException {
        this.mt = mt;
        this.cID = cID;
        this.payload = "";

        if (!validCID(cID)) {
            log.warn("Message create: Unknown connection ID {}", cID);
            throw new UnknownConnectionIDException();
        }

        if (log.isDebugEnabled()) { log.debug("Message created (MT,CID). mt: {}, cid: {}", mt.discriminator, cID); }
    }

    /**
     * Creates a new message from a type, a connection ID and a payload.
     * @param mt The message type.
     * @param cID The connection ID.
     * @param payload The payload.
     * @throws UnknownConnectionIDException Thrown if the connection ID is unknown.
     */
    public Message(MType mt, String cID, String payload) throws UnknownConnectionIDException {
        this.mt = mt;
        this.cID = cID;
        this.payload = payload;

        if (!validCID(this.cID)) {
            log.warn("Message create: Unknown connection ID {}", cID);
            throw new UnknownConnectionIDException();
        }

        if (log.isDebugEnabled()) {
            log.debug("Message created (MT,CID,PL). mt: {}, cid: {}, pl: {}", mt.discriminator, cID, payload);
        }
    }

    /**
     * Creates a new message from a formatted message string.
     * @param message The message string.
     * @throws InvalidMTypeException Thrown if the message type could not be extracted.
     * @throws NoConnectionIDException Thrown if the connection ID could not be extracted.
     * @throws UnknownConnectionIDException Thrown if the connection ID is unknown.
     */
    public Message(String message) throws InvalidMTypeException, NoConnectionIDException, UnknownConnectionIDException {
        this.mt = MessageUtils.getMessageType(message); // extract message type
        this.cID = MessageUtils.getConnectionId(message); // extract connection ID
        try {
            this.payload = message.substring(11,message.length());
        } catch (Exception e) {
            log.warn("Message failed to extract payload. str: {}", message);
            this.payload = "";
        }

        if (!validCID(this.cID)) {
            log.warn("Message create: Unknown connection ID {}", cID);
            throw new UnknownConnectionIDException();
        }

        if (log.isDebugEnabled()) { log.debug("Message created (STR). str: {}", message); }
    }

    /**
     * Returns a properly formatted string for the message.
     * @return The formatted message.
     */
    public String toString() {
        return this.mt.discriminator + this.cID + this.payload;
    }
}
