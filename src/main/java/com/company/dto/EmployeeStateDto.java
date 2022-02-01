package com.company.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;

public class EmployeeStateDto {

    @Schema(description = "New employee state", allowableValues = {"IN_CHECK", "SECURITY_CHECK_FINISHED", "WORK_PERMIT_CHECK_FINISHED", "ACTIVE"}, example = "IN_CHECK")
    @NotBlank
    private String newState;

    private EmployeeStateDto() {
    }

    public EmployeeStateDto(String newState) {
        this.newState = newState;
    }

    public String getNewState() {
        return newState;
    }

}
