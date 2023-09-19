package org.forum.main.exceptions;

import org.forum.main.exceptions.common.MainInstrumentsException;

public class ServiceLayerException extends MainInstrumentsException {

    public ServiceLayerException() {
        super();
    }

    public ServiceLayerException(String msg) {
        super(msg);
    }

    public ServiceLayerException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

}
