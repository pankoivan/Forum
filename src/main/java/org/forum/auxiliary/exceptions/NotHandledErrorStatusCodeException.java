package org.forum.auxiliary.exceptions;

import org.forum.auxiliary.exceptions.common.ForumCheckedException;

public class NotHandledErrorStatusCodeException extends ForumCheckedException {

    public NotHandledErrorStatusCodeException() {
        super();
    }

    public NotHandledErrorStatusCodeException(String msg) {
        super(msg);
    }

    public NotHandledErrorStatusCodeException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

}
