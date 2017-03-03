package me.Jasch.EasySocket.Exceptions;

import java.net.UnknownHostException;

/**
 * Thrown if a received connection ID is not known to the server.
 * @author jasch
 * @version 0.1.0
 * @since 0.1.0
 */
public class UnknownConnectionIDException extends Exception {
    public UnknownConnectionIDException() {
        super();
    }
}
