package org.forum.auxiliary.exceptions;

import org.forum.auxiliary.exceptions.common.MainInstrumentsException;

public class ControllerException extends MainInstrumentsException {

    public ControllerException() {
        super();
    }

    public ControllerException(String msg) {
        super(msg);
    }

    public ControllerException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

}
