package com.company.enums;

public enum EmployeeState {

    ADDED(false),
    IN_CHECK(false),
    SECURITY_CHECK_STARTED(false),
    SECURITY_CHECK_FINISHED(false),
    WORK_PERMIT_CHECK_STARTED(false),
    WORK_PERMIT_CHECK_FINISHED(false),
    /**
     * This is an internal employee state ("transition" state) that is used to join SECURITY_CHECK_FINISHED and WORK_PERMIT_CHECK_FINISHED.
     * @see <a href="https://docs.spring.io/spring-statemachine/docs/current/reference/#join-state">https://docs.spring.io/spring-statemachine/docs/current/reference/#join-state</a>
     */
    IN_CHECK_DONE(true),
    APPROVED(false),
    ACTIVE(false);

    private final boolean internal;

    EmployeeState(boolean internal) {
        this.internal = internal;
    }

    public boolean isInternal() {
        return internal;
    }
}
