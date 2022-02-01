package com.company.services;

import com.company.enums.EmployeeState;
import com.company.enums.EmployeeStateEvent;
import com.company.model.Employee;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.access.StateMachineAccess;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
public class EmployeeStateStateMachineService {

    private static final String EMPLOYEE_ID = "EMPLOYEE_ID";

    private final StateMachineFactory<EmployeeState, EmployeeStateEvent> stateMachineFactory;

    public EmployeeStateStateMachineService(StateMachineFactory<EmployeeState, EmployeeStateEvent> stateMachineFactory) {
        this.stateMachineFactory = stateMachineFactory;
    }

    /**
     * Fetch employee state for state machine
     *
     * @param employee                      an employee
     * @param event                         an employee state event
     * @return list of employee states
     */
    public Collection<EmployeeState> getEmployeeStatesFromStateMachine(Employee employee, EmployeeStateEvent event) {
        StateMachine<EmployeeState, EmployeeStateEvent> stateMachine = buildStateMachine(employee);
        Mono<Message<EmployeeStateEvent>> message = Mono.just(MessageBuilder.withPayload(event).setHeader(EMPLOYEE_ID, employee.getId()).build());
        stateMachine.sendEvent(message).subscribe();
        return stateMachine.getState().getIds();
    }

    /**
     * Build state machine for specific employee
     *
     * @param employee                      an employee
     * @return state machine {@link StateMachine StateMachine}
     */
    private StateMachine<EmployeeState, EmployeeStateEvent> buildStateMachine(Employee employee) {
        StateMachine<EmployeeState, EmployeeStateEvent> stateMachine = this.stateMachineFactory.getStateMachine(employee.getId().toString());
        stateMachine.stopReactively().subscribe();
        List<StateMachineAccess<EmployeeState, EmployeeStateEvent>> stateMachineAccesses = stateMachine.getStateMachineAccessor().withAllRegions();
        for (StateMachineAccess<EmployeeState, EmployeeStateEvent> stateMachineAccess : stateMachineAccesses) {
            DefaultStateMachineContext<EmployeeState, EmployeeStateEvent> defaultStateMachineContext;
            if (!employee.getStates().contains(EmployeeState.IN_CHECK)) {
                defaultStateMachineContext = new DefaultStateMachineContext<>(employee.getStates().iterator().next(), null, null, null, null);
            } else {
                List<StateMachineContext<EmployeeState, EmployeeStateEvent>> children = createChildrenStateMachineContexts(employee.getStates(), EmployeeState.IN_CHECK);
                defaultStateMachineContext = new DefaultStateMachineContext<EmployeeState, EmployeeStateEvent>(children, EmployeeState.IN_CHECK, null, null, null);
            }
            stateMachineAccess.resetStateMachineReactively(defaultStateMachineContext).subscribe();
        }
        stateMachine.startReactively().subscribe();
        return stateMachine;
    }

    /**
     * Create children state machine context for fork state.
     * @see <a href="https://docs.spring.io/spring-statemachine/docs/current/reference/#fork-state">https://docs.spring.io/spring-statemachine/docs/current/reference/#fork-state</a>
     *
     * @param employeeStates                      employee states
     * @param rootState                           employee root state
     * @return the list of state machine context
     */
    private List<StateMachineContext<EmployeeState, EmployeeStateEvent>> createChildrenStateMachineContexts(Set<EmployeeState> employeeStates, EmployeeState rootState) {
        if (!employeeStates.contains(rootState)) {
            return List.of();
        }
        List<StateMachineContext<EmployeeState, EmployeeStateEvent>> result = new ArrayList<>();
        for (EmployeeState employeeState : employeeStates) {
            if (!employeeState.equals(rootState)) {
                result.add(new DefaultStateMachineContext<>(employeeState, null, null, null));
            }
        }
        return result;
    }

}
