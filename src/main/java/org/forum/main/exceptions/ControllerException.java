package org.forum.main.exceptions;

import org.forum.main.exceptions.common.MainInstrumentsException;

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
