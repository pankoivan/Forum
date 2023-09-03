package org.forum.main.services.interfaces.common;

import org.springframework.validation.BindingResult;

public interface ValidationService<T> {

    boolean savingValidation(T validatedObject, BindingResult bindingResult);

    String deletingValidation(T validatedObject);

    default String extractAnySingleError(BindingResult bindingResult) {
        return bindingResult.getAllErrors().stream()
                .findAny()
                .orElseThrow(() -> new RuntimeException("There was no errors in BindingResult"))
                .getDefaultMessage();
    }

}
