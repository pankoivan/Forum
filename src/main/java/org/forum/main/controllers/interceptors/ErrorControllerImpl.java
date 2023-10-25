package org.forum.main.controllers.interceptors;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.forum.auxiliary.exceptions.NotHandledErrorStatusCodeException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
@PreAuthorize("permitAll()")
public class ErrorControllerImpl implements ErrorController {

    @RequestMapping
    public String errors(HttpServletRequest request) throws NotHandledErrorStatusCodeException {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        return status == null
                ? "errors/404"
                : switch (status.toString()) {
                    case "401" -> "errors/401";
                    case "403" -> "errors/403";
                    case "404" -> "errors/404";
                    case "405" -> "errors/405";
                    case "500" -> "unexpected-server-error";
                    default -> throw new NotHandledErrorStatusCodeException(
                            "There were no explicit mappings for error with status code %s".formatted(status)
                    );
        };
    }

}
