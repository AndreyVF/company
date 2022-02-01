package com.company.service;

import com.company.dto.EmployeeDetailsDto;
import com.company.dto.EmployeeDto;
import com.company.dto.IdDto;
import com.company.enums.EmployeeStateEvent;
import com.company.exception.ResourceNotFoundException;
import com.company.mapper.Employee2EmployeeDetailsDtoMapper;
import com.company.mapper.EmployeeDto2EmployeeMapper;
import com.company.model.Employee;
import com.company.repository.EmployeeRepository;
import com.company.services.EmployeeServiceImpl;
import com.company.services.EmployeeStateStateMachineService;
import com.company.enums.EmployeeState;
import com.company.utils.EmployeeTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @InjectMocks
    private EmployeeServiceImpl tested;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private Employee2EmployeeDetailsDtoMapper employee2EmployeeDetailsDtoMapper;

    @Mock
    private EmployeeDto2EmployeeMapper employeeDto2EmployeeMapper;

    @Mock
    private EmployeeStateStateMachineService employeeStateStateMachineService;

    private UUID id;
    private Employee employee;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        employee = EmployeeTestUtils.buildEmployee(id);
    }

    @Test
    void testCreateEmployee() {
        EmployeeDto employeeDto = EmployeeTestUtils.buildEmployeeDto();

        when(employeeDto2EmployeeMapper.from(employeeDto)).thenReturn(employee);
        when(employeeRepository.save(employee)).thenReturn(employee);

        IdDto result = tested.create(employeeDto);

        verify(employeeRepository, times(1)).save(employee);
        assertEquals(id, result.getId());
    }

    @Test
    void testReadEmployeeById() {
        EmployeeDetailsDto employeeDetailsDto = EmployeeTestUtils.buildEmployeeDetailsDto(id, Set.of(EmployeeState.ADDED));

        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));
        when(employee2EmployeeDetailsDtoMapper.from(employee)).thenReturn(employeeDetailsDto);

        EmployeeDetailsDto result = tested.read(id);

        assertEquals(employeeDetailsDto.getAge(), result.getAge());
        assertEquals(employeeDetailsDto.getCompanyName(), result.getCompanyName());
        assertEquals(employeeDetailsDto.getName(), result.getName());
        assertEquals(employeeDetailsDto.getSalary(), result.getSalary());
        assertEquals(employeeDetailsDto.getSalary(), result.getSalary());
        assertEquals(employeeDetailsDto.getStates(), result.getStates());
        assertEquals(employeeDetailsDto.getId(), result.getId());
    }

    @Test
    void testReadEmployeeByIdForNotExistingEmployee() {
        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tested.read(id));
    }

    @Test
    void testUpdateState() {
        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));
        when(employeeStateStateMachineService.getEmployeeStatesFromStateMachine(employee, EmployeeStateEvent.from(EmployeeState.IN_CHECK))).thenReturn(List.of(EmployeeState.IN_CHECK, EmployeeState.SECURITY_CHECK_STARTED, EmployeeState.WORK_PERMIT_CHECK_STARTED));

        tested.updateState(id, EmployeeState.IN_CHECK);

        assertEquals(Set.of(EmployeeState.IN_CHECK, EmployeeState.SECURITY_CHECK_STARTED, EmployeeState.WORK_PERMIT_CHECK_STARTED), employee.getStates());
    }

    @Test
    void testUpdateEmployeeByIdForNotExistingEmployee() {
        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tested.updateState(id, EmployeeState.IN_CHECK));
    }

}
