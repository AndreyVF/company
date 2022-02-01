package com.company.dto;

import com.company.enums.EmployeeState;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

public class EmployeeDetailsDto extends EmployeeDto {

    @Schema(description = "Employee id")
    @NotNull
    private UUID id;

    @Schema(description = "Employee states")
    private Set<EmployeeState> states = new LinkedHashSet<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Set<EmployeeState> getStates() {
        return states;
    }

    public void setStates(Set<EmployeeState> states) {
        this.states = states;
    }
}
