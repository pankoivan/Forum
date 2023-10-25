package org.forum.main.controllers.interceptors;

import org.forum.auxiliary.exceptions.common.MainInstrumentsException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@PreAuthorize("permitAll()")
public class ExceptionInterceptor {

    @ExceptionHandler({AccessDeniedException.class, HttpClientErrorException.Unauthorized.class})
    public String error401() {
        return "errors/401";
    }

    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    public String error403() {
        return "errors/403";
    }

    @ExceptionHandler({NoHandlerFoundException.class, HttpClientErrorException.NotFound.class})
    public String error404() {
        return "errors/404";
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class, HttpClientErrorException.MethodNotAllowed.class})
    public String error405() {
        return "errors/405";
    }

    @ExceptionHandler(MainInstrumentsException.class)
    public String expectedServerError(MainInstrumentsException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "errors/expected-server-error";
    }

    @ExceptionHandler(Exception.class)
    public String unexpectedServerError(Exception e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "errors/unexpected-server-error";
    }

}
