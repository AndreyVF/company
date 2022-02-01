package com.company.utils;

import com.company.dto.EmployeeDetailsDto;
import com.company.dto.EmployeeDto;
import com.company.enums.EmployeeState;
import com.company.model.Employee;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

public class EmployeeTestUtils {

    private static final String NAME = "name";
    private static final String COMPANY_NAME = "companyName";
    private static final int SALARY = 10000;
    private static final int AGE = 18;

    public static Employee buildEmployee(UUID id) {
        Employee employee = new Employee();
        ReflectionTestUtils.setField(employee, "id", id);
        employee.setSalary(BigDecimal.valueOf(SALARY));
        employee.setName(NAME);
        employee.setCompanyName(COMPANY_NAME);
        employee.setAge(AGE);
        return employee;
    }

    public static EmployeeDto buildEmployeeDto() {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setName(NAME);
        employeeDto.setSalary(BigDecimal.valueOf(SALARY));
        employeeDto.setCompanyName(COMPANY_NAME);
        employeeDto.setAge(AGE);
        return employeeDto;
    }

    public static EmployeeDetailsDto buildEmployeeDetailsDto(UUID id, Set<EmployeeState> employeeStates) {
        EmployeeDetailsDto employeeDetailsDto = new EmployeeDetailsDto();
        employeeDetailsDto.setName(NAME);
        employeeDetailsDto.setSalary(BigDecimal.valueOf(SALARY));
        employeeDetailsDto.setCompanyName(COMPANY_NAME);
        employeeDetailsDto.setAge(AGE);
        employeeDetailsDto.setId(id);
        employeeDetailsDto.setStates(employeeStates);
        return employeeDetailsDto;
    }

}
