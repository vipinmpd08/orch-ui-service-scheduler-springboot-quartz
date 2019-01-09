package com.emirates.ihc.scheduler.jobs;

import com.emirates.ihc.framework.exceptions.InvalidInputException;
import com.emirates.ihc.scheduler.domain.JobInstanceStatus;
import com.emirates.ihc.scheduler.domain.JobInstanceTO;
import com.emirates.ihc.scheduler.service.IJobInstanceService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.logging.Logger;


@DisallowConcurrentExecution
public abstract class OchInterruptableNonConcurrentJob implements InterruptableJob {

    private Logger log = Logger.getLogger("IHCInterruptableNonConcurrentJob.class");

    @Autowired
    private IJobInstanceService instanceService;

    private boolean isInterrupted = false;

    /**
     * This method will be called by Quart run-time
     * @throws UnableToInterruptJobException
     */
    @Override
    public final void interrupt() throws UnableToInterruptJobException {
        isInterrupted = true;
        interrupted();
    }

    public abstract void interrupted()  throws UnableToInterruptJobException;

    @Override
    public final void execute(JobExecutionContext cntx) throws JobExecutionException {
        try {
            logJobInstanceStartsDetail(cntx);
            run(cntx);
            logJobInstanceEndDetail(cntx, null);
        } catch(JobExecutionException ex) {
            throw ex;
        }
    }

    public abstract void run(JobExecutionContext cntx) throws JobExecutionException;

    private void logJobInstanceStartsDetail(JobExecutionContext cntx) throws JobExecutionException {
        try {
            JobInstanceTO instanceTO = new JobInstanceTO();
            instanceTO.setQuartzInstanceId(cntx.getFireInstanceId());
            instanceTO.setStatus(JobInstanceStatus.RUNNING);
            instanceTO.setStartDate(new Date());
            instanceService.saveJobInstanceDetail(instanceTO);
            cntx.put("InstanceTO", instanceTO);
            log.info("Started Quartz Id ====> " + cntx.getFireInstanceId()  + "- IHS id ==>"+ instanceTO.getJobInstanceId());
        } catch(InvalidInputException ex) {
            throw new JobExecutionException("Not able to save Job instance Start detail", ex);
        }
    }

    private void logJobInstanceEndDetail(JobExecutionContext cntx, Exception e) throws JobExecutionException {
        try {
            JobInstanceTO instanceTO = (JobInstanceTO) cntx.get("InstanceTO");
            if (e == null) {
                instanceTO.setStatus(isInterrupted?JobInstanceStatus.INTERRUPTED: JobInstanceStatus.COMPLETED);
                log.info("Completed Quartz Id ====> " + cntx.getFireInstanceId()  + "- IHS id ==>"+ instanceTO.getJobInstanceId());
            } else {
                instanceTO.setStatus(JobInstanceStatus.FAILED);
                log.info("Failed Quartz Id ====> " + cntx.getFireInstanceId()  + "- IHS id ==>"+ instanceTO.getJobInstanceId() + "- Error ==>"+ e.getMessage());
            }
            instanceTO.setEndDate(new Date());
            instanceService.saveJobInstanceDetail(instanceTO);
        } catch(InvalidInputException exe) {
            throw new JobExecutionException("Not able to save Job instance Start detail", exe);
        }
    }

}
