package org.forum.auxiliary.exceptions;

import org.forum.auxiliary.exceptions.common.AuxiliaryInstrumentsException;

public class UrlPathVariableUtilsException extends AuxiliaryInstrumentsException {

    public UrlPathVariableUtilsException() {
        super();
    }

    public UrlPathVariableUtilsException(String msg) {
        super(msg);
    }

    public UrlPathVariableUtilsException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

}
