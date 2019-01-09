package com.emirates.ihc.scheduler.service.impl;

import com.emirates.ihc.scheduler.domain.SchedulerStatus;
import com.emirates.ihc.scheduler.domain.SchedulerTO;
import com.emirates.ihc.scheduler.exception.OrchSchedulerException;
import com.emirates.ihc.scheduler.exception.SchedulerNotRunningException;
import com.emirates.ihc.scheduler.service.ISchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.SchedulingException;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;


@Service
public class SchedulerService implements ISchedulerService {

    @Autowired
    SchedulerFactoryBean schFactory;

    @Override
    public SchedulerTO stopScheduler() throws SchedulerNotRunningException, OrchSchedulerException {
        if (schFactory.isRunning()) {
            try {
                schFactory.stop();
            } catch(SchedulingException ex) {
                throw new OrchSchedulerException("Not able to stop the scheduler instance", ex);
            }
        } else {
            throw new SchedulerNotRunningException("Scheduler instance is not running");
        }
        return new SchedulerTO(SchedulerStatus.STOPPED);
    }

    @Override
    public SchedulerTO startScheduler() throws OrchSchedulerException {
        if (!schFactory.isRunning()) {
            try {
                schFactory.start();
            } catch(SchedulingException ex) {
                throw new OrchSchedulerException("Not able to start the scheduler instance", ex);
            }
        } else {
            throw new OrchSchedulerException("Scheduler is alreay running", new Exception("Scheduler already running"));
        }
        return new SchedulerTO(SchedulerStatus.STARTED);
    }

    @Override
    public SchedulerTO getStatus() {
        if (schFactory.isRunning()) {
            return new SchedulerTO(SchedulerStatus.RUNNING);
        } else {
            return new SchedulerTO(SchedulerStatus.NOT_RUNNING);
        }
    }
}
