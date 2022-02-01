package com.company.api;

import com.company.dto.EmployeeDetailsDto;
import com.company.dto.EmployeeDto;
import com.company.dto.EmployeeStateDto;
import com.company.dto.IdDto;
import com.company.enums.EmployeeState;
import com.company.exception.InvalidEmployeeStateException;
import com.company.services.EmployeeServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

import static com.company.exception.Errors.ERROR_EMPLOYEE_STATE_INCORRECT;

@RestController
@RequestMapping("/api/employees")
@Validated
public class EmployeeController {

    private final EmployeeServiceImpl employeeService;

    public EmployeeController(EmployeeServiceImpl employeeService) {
        this.employeeService = employeeService;
    }

    @Operation(summary = "create employee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Employee created successfully"),
        @ApiResponse(responseCode = "400",
            description = "Validation for Employee fields failed",
            content = @Content(schema = @Schema()))
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IdDto> createEmployee(@Valid @RequestBody EmployeeDto employeeDto) {
        return new ResponseEntity<>(employeeService.create(employeeDto), HttpStatus.CREATED);
    }

    @Operation(summary = "read employee by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404",
            description = "Employee not found",
            content = @Content(schema = @Schema()))
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmployeeDetailsDto> read(@PathVariable("id") @NotNull UUID id) {
        return new ResponseEntity<>(employeeService.read(id), HttpStatus.OK);
    }

    @Operation(summary = "update employee state")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204"),
        @ApiResponse(responseCode = "404",
            description = "Employee not found",
            content = @Content(schema = @Schema())),
        @ApiResponse(responseCode = "400",
            description = "Employee state is not correct",
            content = @Content(schema = @Schema()))
    })
    @PutMapping("/{id}/updateState")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateEmployeeState(@PathVariable("id") @NotNull UUID id, @Valid @RequestBody EmployeeStateDto employeeStateDto) {
        if (!EnumUtils.isValidEnum(EmployeeState.class, employeeStateDto.getNewState()) || EmployeeState.valueOf(employeeStateDto.getNewState()).isInternal()) {
            throw new InvalidEmployeeStateException(ERROR_EMPLOYEE_STATE_INCORRECT);
        }
        employeeService.updateState(id, EmployeeState.valueOf(employeeStateDto.getNewState()));
    }

}
