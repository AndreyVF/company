package com.company.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.*;
import java.math.BigDecimal;

public class EmployeeDto {

    @Schema(description = "Employee name", example = "Cristiano Ronaldo")
    @NotBlank
    private String name;

    @Schema(description = "Employee age", minimum = "18", maximum = "150", example = "55")
    @NotNull
    @Min(value = 18)
    @Max(value = 150)
    private Integer age;

    @Schema(description = "Employee salary", minimum = "0", example = "55000")
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal salary;

    @Schema(description = "Employee company name", example = "Manchester United FC")
    @NotBlank
    private String companyName;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }
}
