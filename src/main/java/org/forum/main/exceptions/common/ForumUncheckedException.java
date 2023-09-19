package org.forum.main.exceptions.common;

public class ForumUncheckedException extends RuntimeException {

    public ForumUncheckedException() {
        super();
    }

    public ForumUncheckedException(String msg) {
        super(msg);
    }

    public ForumUncheckedException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

}
