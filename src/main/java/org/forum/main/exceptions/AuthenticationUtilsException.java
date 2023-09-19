package org.forum.main.exceptions;

import org.forum.main.exceptions.common.AuxiliaryInstrumentsException;

public class AuthenticationUtilsException extends AuxiliaryInstrumentsException {

    public AuthenticationUtilsException() {
        super();
    }

    public AuthenticationUtilsException(String msg) {
        super(msg);
    }

    public AuthenticationUtilsException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

}
