package io.shabab477.github.model2cobol.exceptions;

/**
 * @author shabab
 * @since 11/8/18
 */
public class NotPrimitiveException extends RuntimeException {

    public NotPrimitiveException() {
        super("Data Member not a primitive type");
    }
}
