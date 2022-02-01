package com.company.mapper;

import com.company.dto.EmployeeDetailsDto;
import com.company.model.Employee;
import org.springframework.stereotype.Component;

@Component
public class Employee2EmployeeDetailsDtoMapper implements Mapper<Employee, EmployeeDetailsDto> {

    @Override
    public EmployeeDetailsDto from(Employee from) {
        EmployeeDetailsDto employeeDetailsDto = new EmployeeDetailsDto();
        employeeDetailsDto.setAge(from.getAge());
        employeeDetailsDto.setCompanyName(from.getCompanyName());
        employeeDetailsDto.setSalary(from.getSalary());
        employeeDetailsDto.setName(from.getName());
        employeeDetailsDto.setStates(from.getStates());
        employeeDetailsDto.setId(from.getId());
        return employeeDetailsDto;
    }

}
