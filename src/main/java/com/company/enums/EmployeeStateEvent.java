package com.company.enums;

/**
 * Events that trigger a {@link EmployeeState EmployeeState} change
 */
public enum EmployeeStateEvent {

    IN_CHECK,
    SECURITY_CHECK_FINISHED,
    WORK_PERMIT_CHECK_FINISHED,
    ACTIVE;

    /**
     * Get employee state event from employee state
     *
     * @param state                         an employee state
     * @throws IllegalArgumentException     if this enum type has no constant with the specified name
     * @return {@link EmployeeStateEvent EmployeeStateEvent}
     */
    public static EmployeeStateEvent from(EmployeeState state) {
        return EmployeeStateEvent.valueOf(state.name());
    }
}
