package me.Jasch.EasySocket.Event;

import me.Jasch.EasySocket.Message.MType;
import me.Jasch.EasySocket.Message.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

/**
 * Handler for user created events.
 * @author jasch
 * @version 0.1.0
 * @since 0.1.0
 */
public class EventHandler {

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
        return registered;
    }

    /**
     * Dispatches a new event.
     * @param msg The received message.
     * @return True if successfully dispatched the event, false if not.
     */
    @NotNull
    public boolean dispatchEvent(Message msg) {
        boolean dispatched = false;
        String id = msg.payload.split(":", 2)[0];

        if (events.containsKey(id)) {
            events.get(id).handleEvent(msg);
            dispatched = true;
        }

        return dispatched;
    }

    /**
     * Creates a message object for your event.
     * @param id The id of your event.
     * @param cID The connection ID of your target client.
     * @param payload The payload to send.
     * @return The message object or null if unable to create it.
     */
    @Nullable
    public Message createEventMessage(String id, String cID, Object payload) {
        if (events.containsKey(id)) {
            IEvent event = events.get(id);
            return new Message(MType.EVT, cID, id + ":" + event.stringifyPayload(payload));
        } else {
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
