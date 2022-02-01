package com.company.model;

import com.company.enums.EmployeeState;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

@Entity
public class Employee {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private BigDecimal salary;

    @Column(nullable = false)
    private String companyName;

    @ElementCollection
    private Set<EmployeeState> states = new LinkedHashSet<>();

    public Employee() {
        states.add(EmployeeState.ADDED);
    }

    public UUID getId() {
        return id;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Set<EmployeeState> getStates() {
        return states;
    }

    public void setStates(Set<EmployeeState> states) {
        this.states = states;
    }
}
