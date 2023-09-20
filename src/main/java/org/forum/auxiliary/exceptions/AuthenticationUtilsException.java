package org.forum.auxiliary.exceptions;

import org.forum.auxiliary.exceptions.common.AuxiliaryInstrumentsException;

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
