package com.company.mapper;

import com.company.dto.EmployeeDto;
import com.company.model.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeDto2EmployeeMapper implements Mapper<EmployeeDto, Employee> {

    @Override
    public Employee from(EmployeeDto from) {
        Employee employee = new Employee();
        employee.setAge(from.getAge());
        employee.setCompanyName(from.getCompanyName());
        employee.setName(from.getName());
        employee.setSalary(from.getSalary());
        return employee;
    }
}
