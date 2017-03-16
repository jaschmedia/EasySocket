package me.Jasch.EasySocket.Event;

import me.Jasch.EasySocket.Exceptions.UnknownConnectionIDException;
import me.Jasch.EasySocket.Message.MType;
import me.Jasch.EasySocket.Message.Message;
import me.Jasch.EasySocket.WebSocket.WSUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * Handler for user created events.
 * @author jasch
 * @version 0.1.1
 * @since 0.1.0
 */
public class EventHandler {

    private static final Logger log = LoggerFactory.getLogger(EventHandler.class); // logger instance

    private HashMap<String, IEvent> events = new HashMap<>();

    public EventHandler() {

    }

    /**
     * Register a new event.
     * @param id Unique alphanumeric identifier for the event.
     * @param event The event.
     * @return True if event was successfully registered, false if not.
     */
    @NotNull
    public boolean registerEvent(String id, IEvent event) {
        boolean registered = false;
        if (validEventID(id)) {
            if (!events.containsKey(id)) {
                events.put(id, event);
                registered = true;
            }
        }
        if (log.isDebugEnabled()) { log.debug("Registered event. ID: {}", id); }
        return registered;
    }

    /**
     * Dispatches a new event.
     * @param msg The received message.
     * @return True if successfully dispatched the event, false if not.
     * @throws UnknownConnectionIDException Thrown if the connection ID is unknown.
     */
    @NotNull
    public boolean dispatchEvent(Message msg) throws UnknownConnectionIDException {
        boolean dispatched = false;

        if (!WSUtils.validCID(msg.cID)) {
            throw new UnknownConnectionIDException();
        }

        String id = msg.payload.split(":", 2)[0];

        if (events.containsKey(id)) {
            events.get(id).handleEvent(msg);
            dispatched = true;

            if (log.isDebugEnabled()) { log.debug("Event dispatched. ID: {}, CID: {}", id, msg.cID); }
        } else {
            log.info("Unknown event recieved. ID: {}, msg: {}", id, msg.toString());
        }

        return dispatched;
    }

    /**
     * Creates a message object for your event.
     * @param id The id of your event.
     * @param cID The connection ID of your target client.
     * @param payload The payload to send.
     * @return The message object or null if unable to create it.
     * @throws UnknownConnectionIDException Thrown if the connection ID is unknown.
     */
    @Nullable
    public Message createEventMessage(String id, String cID, Object payload) throws UnknownConnectionIDException {
        if (events.containsKey(id)) {
            IEvent event = events.get(id);
            if (log.isDebugEnabled()) { log.debug("Event created. ID: {}, CID: {}", id, cID); }

            return new Message(MType.EVT, cID, id + ":" + event.stringifyPayload(payload));
        } else {
            log.info("Tried to create unknown event. ID: {}, CID: {}", id, cID);
            return null;
        }
    }

    /**
     * Checks if ID is alphanumeric string.
     * @param id Given ID
     * @return True if alphanumeric.
     */
    @NotNull
    private boolean validEventID(String id) {
        char[] chars = id.toCharArray();
        boolean valid = true;
        for (char a : chars) {
            valid = Character.isLetterOrDigit(a) && valid;
        }
        return valid;
    }
}
