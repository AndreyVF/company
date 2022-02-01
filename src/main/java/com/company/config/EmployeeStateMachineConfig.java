package com.company.config;

import com.company.enums.EmployeeState;
import com.company.enums.EmployeeStateEvent;
import com.company.listener.EmployeeStateMachineListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.logging.Logger;

import static com.company.enums.EmployeeState.*;

@EnableStateMachineFactory
@Configuration
public class EmployeeStateMachineConfig extends StateMachineConfigurerAdapter<EmployeeState, EmployeeStateEvent> {

    private static final Logger logger = Logger.getLogger(EmployeeStateMachineConfig.class.getName());

    private static final String EVENT_TRIGGERED_MESSAGE = "Event %s triggered";

    @Override
    public void configure(StateMachineConfigurationConfigurer<EmployeeState, EmployeeStateEvent> config)
        throws Exception {
        config
            .withConfiguration()
            .autoStartup(true)
            .listener(new EmployeeStateMachineListener());
    }

    @Override
    public void configure(StateMachineStateConfigurer<EmployeeState, EmployeeStateEvent> states) throws Exception {
        states
            .withStates()
                .initial(ADDED)
                .fork(IN_CHECK)
                .join(IN_CHECK_DONE)
                .state(APPROVED)
                .end(ACTIVE)
            .and()
            .withStates()
                .parent(IN_CHECK)
                .initial(SECURITY_CHECK_STARTED)
                .end(SECURITY_CHECK_FINISHED)
            .and()
            .withStates()
                .parent(IN_CHECK)
                .initial(WORK_PERMIT_CHECK_STARTED)
                .end(WORK_PERMIT_CHECK_FINISHED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<EmployeeState, EmployeeStateEvent> transitions) throws Exception {
        transitions
            //ADDED -> IN_CHECK
            .withExternal()
                .source(ADDED)
                .target(IN_CHECK)
                .event(EmployeeStateEvent.IN_CHECK)
                .action(sc -> logger.info(String.format(EVENT_TRIGGERED_MESSAGE, sc.getEvent())))

            // SECURITY_CHECK_STARTED -> SECURITY_CHECK_FINISHED
            .and()
            .withExternal()
                .source(SECURITY_CHECK_STARTED)
                .target(SECURITY_CHECK_FINISHED)
                .event(EmployeeStateEvent.SECURITY_CHECK_FINISHED)
                .action(sc -> logger.info(String.format(EVENT_TRIGGERED_MESSAGE, sc.getEvent())))

            // WORK_PERMIT_CHECK_STARTED -> WORK_PERMIT_CHECK_FINISHED
            .and()
            .withExternal()
                .source(WORK_PERMIT_CHECK_STARTED)
                .target(WORK_PERMIT_CHECK_FINISHED)
                .event(EmployeeStateEvent.WORK_PERMIT_CHECK_FINISHED)
                .action(sc -> logger.info(String.format(EVENT_TRIGGERED_MESSAGE, sc.getEvent())))

            // SECURITY_CHECK_FINISHED, WORK_PERMIT_CHECK_FINISHED -> APPROVED
            .and()
            .withFork()
                .source(IN_CHECK)
                .target(SECURITY_CHECK_STARTED)
                .target(WORK_PERMIT_CHECK_STARTED)
            .and()
            .withJoin()
                .source(SECURITY_CHECK_FINISHED)
                .source(WORK_PERMIT_CHECK_FINISHED)
                .target(IN_CHECK_DONE)
            .and()
            .withExternal()
                .source(IN_CHECK_DONE)
                .target(APPROVED)

            // APPROVED -> ACTIVE
            .and()
            .withExternal()
                .source(APPROVED)
                .target(ACTIVE)
                .event(EmployeeStateEvent.ACTIVE)
                .action(sc -> logger.info(String.format(EVENT_TRIGGERED_MESSAGE, sc.getEvent())));
    }

}
