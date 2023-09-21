package org.forum.main.controllers.mvc.interceptors;

import org.forum.auxiliary.exceptions.common.MainInstrumentsException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionInterceptor {

    @ExceptionHandler
    public String expectedServerError(MainInstrumentsException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "errors/expected-server-error";
    }

    @ExceptionHandler
    public String unexpectedServerError(Exception e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "errors/unexpected-server-error";
    }

}
