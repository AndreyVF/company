package com.company.services;

import com.company.dto.EmployeeDetailsDto;
import com.company.dto.EmployeeDto;
import com.company.dto.IdDto;
import com.company.enums.EmployeeState;
import com.company.enums.EmployeeStateEvent;
import com.company.exception.ResourceNotFoundException;
import com.company.mapper.Employee2EmployeeDetailsDtoMapper;
import com.company.mapper.EmployeeDto2EmployeeMapper;
import com.company.model.Employee;
import com.company.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.UUID;

import static com.company.exception.Errors.ERROR_EMPLOYEE_NOT_FOUND;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;
    private final EmployeeDto2EmployeeMapper employeeDto2EmployeeMapper;
    private final Employee2EmployeeDetailsDtoMapper employee2EmployeeDetailsDtoMapper;
    private final EmployeeStateStateMachineService employeeStateStateMachineService;

    public EmployeeServiceImpl(EmployeeRepository repository,
                               EmployeeDto2EmployeeMapper employeeDto2EmployeeMapper,
                               Employee2EmployeeDetailsDtoMapper employee2EmployeeDetailsDtoMapper,
                               EmployeeStateStateMachineService employeeStateStateMachineService) {
        this.repository = repository;
        this.employeeDto2EmployeeMapper = employeeDto2EmployeeMapper;
        this.employee2EmployeeDetailsDtoMapper = employee2EmployeeDetailsDtoMapper;
        this.employeeStateStateMachineService = employeeStateStateMachineService;
    }

    /**
     * Read employee by id
     *
     * @param id        an employee id
     * @throws ResourceNotFoundException if the employee was not found
     * @return {@link EmployeeDetailsDto EmployeeDetailsDto}
     */
    @Override
    @Transactional(readOnly = true)
    public EmployeeDetailsDto read(UUID id) {
        Employee employee = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ERROR_EMPLOYEE_NOT_FOUND));
        return employee2EmployeeDetailsDtoMapper.from(employee);
    }

    /**
     * Create employee
     *
     * @param employeeDto        an employee employeeDto
     * @return {@link IdDto IdDto}
     */
    @Override
    @Transactional
    public IdDto create(EmployeeDto employeeDto) {
        Employee employee = employeeDto2EmployeeMapper.from(employeeDto);
        Employee savedEmployee = repository.save(employee);
        return new IdDto(savedEmployee.getId());
    }

    /**
     * Update employee state
     *
     * @param id                            an employee id
     * @param newState                      an employee new state
     * @throws ResourceNotFoundException    if the employee was not found
     *  @throws IllegalArgumentException     if this enum type has no constant with the specified name
     */
    @Override
    @Transactional
    public void updateState(UUID id, EmployeeState newState) {
        Employee employee = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ERROR_EMPLOYEE_NOT_FOUND));
        Collection<EmployeeState> employeeStates = employeeStateStateMachineService.getEmployeeStatesFromStateMachine(employee, EmployeeStateEvent.from(newState));
        employee.setStates(new LinkedHashSet<>(employeeStates));
    }

}
