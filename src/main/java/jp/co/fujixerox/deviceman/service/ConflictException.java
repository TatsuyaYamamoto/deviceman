package jp.co.fujixerox.deviceman.service;

import jp.co.fujixerox.deviceman.ApplicationException;

/**
 * It's thrown when an one entity could not create because of unique constraint error.
 */
public class ConflictException extends ApplicationException {

    public ConflictException(String message) {
        super(message);
    }

    public ConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
