/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.connections;

/**
 *
 * @author armen
 */
public class MongoInitializationException extends RuntimeException {

    private static final long serialVersionUID = -1099192821808240472L;

    /**
     * Constructor accepting a custom message.
     *
     * @param message String describing the exception
     */
    public MongoInitializationException(String message) {
        super(message);
    }

    /**
     * Constructor excepting a custom message and the original exception.
     *
     * @param message String describing the exception
     * @param cause   Original exception
     */
    public MongoInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}