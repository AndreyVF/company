package com.company.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class IdDto {

    @Schema(description = "Resource id")
    @NotNull
    private UUID id;

    private IdDto() {
    }

    public IdDto(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

}
