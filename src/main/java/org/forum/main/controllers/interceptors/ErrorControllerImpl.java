package org.forum.main.controllers.interceptors;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorControllerImpl implements ErrorController {

    @RequestMapping
    public String errors(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        return switch (status.toString()) {
            case "403" -> "errors/403";
            case "404" -> "errors/404";
            case "405" -> "errors/405";
            default -> "errors/unknown-error";
        };
    }

}
