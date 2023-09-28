package org.forum.main.controllers.interceptors;

import org.forum.auxiliary.exceptions.common.MainInstrumentsException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionInterceptor {

    @ExceptionHandler(value = MainInstrumentsException.class)
    public String expectedServerError(MainInstrumentsException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "errors/expected-server-error";
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public String error405() {
        return "errors/405";
    }

    @ExceptionHandler(value = Exception.class)
    public String unexpectedServerError(Exception e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "errors/unexpected-server-error";
    }

}
