package com.company.services;

import com.company.dto.EmployeeDetailsDto;
import com.company.dto.EmployeeDto;
import com.company.dto.IdDto;
import com.company.enums.EmployeeState;

import java.util.UUID;

public interface EmployeeService {

    EmployeeDetailsDto read(UUID id);

    IdDto create(EmployeeDto employeeDto);

    void updateState(UUID id, EmployeeState newState);

}
