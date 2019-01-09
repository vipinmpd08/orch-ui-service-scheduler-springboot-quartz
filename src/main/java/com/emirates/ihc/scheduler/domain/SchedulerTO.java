package com.emirates.ihc.scheduler.domain;

public class SchedulerTO {
    private SchedulerStatus status;

    public SchedulerTO() {

    }

    public SchedulerTO(SchedulerStatus status) {
        this.status = status;
    }

    public SchedulerStatus getStatus() {
        return status;
    }

    public void setStatus(SchedulerStatus status) {
        this.status = status;
    }
}
