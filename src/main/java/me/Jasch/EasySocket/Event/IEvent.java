package me.Jasch.EasySocket.Event;

import me.Jasch.EasySocket.Message.Message;

/**
 * @author jasch
 */
public interface IEvent {
    /**
     * Handle a message that triggered this event.
     * @param msg The message.
     */
    void handleEvent(Message msg);

    /**
     * Turns the payload for this event into a string.
     * @param payload The payload.
     * @return The string.
     */
    String stringifyPayload(Object payload);
}
