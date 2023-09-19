package org.forum.main.exceptions.common;

public class MainInstrumentsException extends ForumUncheckedException {

    public MainInstrumentsException() {
        super();
    }

    public MainInstrumentsException(String msg) {
        super(msg);
    }

    public MainInstrumentsException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

}
