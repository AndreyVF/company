package com.company.listener;

import com.company.enums.EmployeeState;
import com.company.enums.EmployeeStateEvent;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.logging.Logger;

/**
 * Custom listener to monitor state transition
 */
public class EmployeeStateMachineListener extends StateMachineListenerAdapter<EmployeeState, EmployeeStateEvent> {

    private static final Logger logger = Logger.getLogger(EmployeeStateMachineListener.class.getName());

    @Override
    public void stateChanged(State from, State to) {
        String fromMessage = from != null ? from.getIds().toString() : null;
        String toMessage = to != null ? to.getIds().toString() : null;
        logger.info(() -> "Transitioned from " + fromMessage + " to " + toMessage);
    }
}
