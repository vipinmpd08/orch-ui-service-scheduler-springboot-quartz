package com.emirates.ihc.scheduler.service;

import com.emirates.ihc.scheduler.domain.SchedulerTO;
import com.emirates.ihc.scheduler.exception.OrchSchedulerException;
import com.emirates.ihc.scheduler.exception.SchedulerNotRunningException;

public interface ISchedulerService {

    SchedulerTO stopScheduler() throws SchedulerNotRunningException, OrchSchedulerException;

    SchedulerTO startScheduler() throws OrchSchedulerException;

    SchedulerTO getStatus();
}
