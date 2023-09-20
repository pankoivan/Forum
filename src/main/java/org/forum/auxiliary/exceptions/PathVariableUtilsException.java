package org.forum.auxiliary.exceptions;

import org.forum.auxiliary.exceptions.common.AuxiliaryInstrumentsException;

public class PathVariableUtilsException extends AuxiliaryInstrumentsException {

    public PathVariableUtilsException() {
        super();
    }

    public PathVariableUtilsException(String msg) {
        super(msg);
    }

    public PathVariableUtilsException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

}
