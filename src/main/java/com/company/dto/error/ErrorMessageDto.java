package com.company.dto.error;

import javax.validation.constraints.NotBlank;

public class ErrorMessageDto {

    @NotBlank
    private final String message;

    public ErrorMessageDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
