package org.forum.main.services.interfaces.common;

import org.forum.auxiliary.exceptions.ServiceException;
import org.springframework.validation.BindingResult;

public interface ValidationService<T> {

    boolean savingValidation(T validatedObject, BindingResult bindingResult);

    String deletingValidation(T validatedObject);

    default String anyError(BindingResult bindingResult) {
        return bindingResult.getAllErrors().stream()
                .findAny()
                .orElseThrow(() -> new ServiceException("There was no errors in BindingResult"))
                .getDefaultMessage();
    }

}
