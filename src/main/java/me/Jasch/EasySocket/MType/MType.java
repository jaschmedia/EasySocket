package me.Jasch.EasySocket.MType;

/**
 * Possible types of messages sent over the WebSocket.
 * @author jasch
 * @version 0.1.0
 * @since 0.1.0
 */
public enum MType {
    ACK("ACK"), // acknowledge
    NAC("NAC"), // not acknowledge

    ATH("ATH"), // authenticate

    ERR("ERR"), // Error

    CIS("CIS"), // connection ID set
    PRT("PRT"), // protocol information

    RTL("RTL"), // rate limited - NOT IMPLEMENTED

    PNG("PNG"), // ping - NOT IMPLEMENTED
    POG("POG"), // pong - NOT IMPLEMENTED
    EVT("EVT"),; // event

    private final String discriminator;

    MType(String discriminator) {
        this.discriminator = discriminator;
    }

    public String getDiscrminator() {
        return this.discriminator;
    }
}