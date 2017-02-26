package me.Jasch.EasySocket.Exceptions;

/**
 * Thrown for invalid message types.
 * @author jasch
 * @version 0.1.0
 * @since 0.1.0
 */
public class InvalidMTypeException extends Exception {
    public InvalidMTypeException(String err) {
        super(err);
    }
}
