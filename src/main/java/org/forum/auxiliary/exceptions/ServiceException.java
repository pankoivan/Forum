package org.forum.auxiliary.exceptions;

import org.forum.auxiliary.exceptions.common.MainInstrumentsException;

public class ServiceException extends MainInstrumentsException {

    public ServiceException() {
        super();
    }

    public ServiceException(String msg) {
        super(msg);
    }

    public ServiceException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

}
