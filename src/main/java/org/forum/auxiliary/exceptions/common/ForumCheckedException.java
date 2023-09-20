package org.forum.auxiliary.exceptions.common;

public class ForumCheckedException extends Exception {

    public ForumCheckedException() {
        super();
    }

    public ForumCheckedException(String msg) {
        super(msg);
    }

    public ForumCheckedException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

}
