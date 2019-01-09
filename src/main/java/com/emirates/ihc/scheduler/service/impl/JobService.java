package com.emirates.ihc.scheduler.service.impl;

import com.emirates.ihc.scheduler.domain.*;
import com.emirates.ihc.framework.exceptions.InvalidInputException;
import com.emirates.ihc.scheduler.exception.JobNotRunningException;
import com.emirates.ihc.scheduler.exception.JobSchedulingException;
import com.emirates.ihc.scheduler.jobs.OchInterruptableNonConcurrentJob;
import com.emirates.ihc.scheduler.service.IJobInstanceService;
import com.emirates.ihc.scheduler.service.IJobService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class JobService implements IJobService {

    @Autowired
    SchedulerFactoryBean schFactory;

    @Autowired
    IJobInstanceService instanceSrv;


    @Override
    public JobInstanceTO interruptRunningJob(String jobName) throws JobNotRunningException, UnableToInterruptJobException {
        try {
            boolean interrupted = false;
            List<JobExecutionContext> lstJobs = schFactory.getScheduler().getCurrentlyExecutingJobs();
            for(JobExecutionContext c : lstJobs) {
                if (c instanceof OchInterruptableNonConcurrentJob && c.getJobDetail().getKey().getName().equals(jobName)) {
                    ((OchInterruptableNonConcurrentJob) c.getJobInstance()).interrupt();
                    interrupted = true;
                    break;
                }
            }
            if (!interrupted) {
                throw new JobNotRunningException(jobName + " is not running. So it cannot be interrupted");
            }
            return new JobInstanceTO(JobInstanceStatus.INTERRUPTED);
        } catch(SchedulerException  ex) {
            throw new UnableToInterruptJobException(ex);
        }
    }


    @Override
    public JobInstanceTO scheduleJob(JobTO job) throws InvalidInputException, JobSchedulingException {
        try {
            this.validateJobData(job);
            JobDetail jobDtl = createJob(job);
            Trigger trg = createTrigger(job, jobDtl);
            schFactory.getScheduler().addJob(jobDtl, true);
            schFactory.getScheduler().scheduleJob(trg);
            return new JobInstanceTO(JobInstanceStatus.SCHEDULED);
        } catch(SchedulerException ex) {
            throw new JobSchedulingException("Not able to schedule job", ex);
        }
    }

    private void validateJobData(JobTO job) throws InvalidInputException {
        if (job == null || job.getJobName() == null || job.getJobClassName() == null) {
            throw new InvalidInputException("Parameter or JobName or JobClass cannot be null", new Exception());
        }
        if (job.getTrigger() == null || job.getTrigger().getType() == null) {
            throw new InvalidInputException("Trigger/TriggerType detail cannot be null", new Exception());
        }
        if (job.getTrigger().getType() == TriggerType.SIMPLE && (job.getTrigger().getUnit() == null || job.getTrigger().getUnitValue() == null)){
            throw new InvalidInputException("Unit and UnitValue cannot be null for Simple trigger", new Exception());
        }
        if (job.getTrigger().getType() == TriggerType.CRON && job.getTrigger().getCronExpression() == null){
            throw new InvalidInputException("CronExpression cannot be null for Cron trigger", new Exception());
        }
    }

    private JobDetail createJob(JobTO to) throws JobSchedulingException {
        try {
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("jobName", to.getJobName());
            Class c = Class.forName(to.getJobClassName());
            return JobBuilder.newJob(c)
                    .withIdentity(to.getJobName())
                    .setJobData(jobDataMap)
                    .storeDurably()
                    .build();
        } catch(ClassNotFoundException ex) {
            throw new JobSchedulingException("ClassNotFoundException for class "+ to.getJobClassName(), new Exception());
        }
    }

    private Trigger createTrigger(JobTO to, JobDetail jobDtl) {
        SimpleScheduleBuilder schBuilder = null;
        String triggerName = "";
        if (to.getTrigger().getType() == TriggerType.SIMPLE) {
            schBuilder = SimpleScheduleBuilder.simpleSchedule();
            if (to.getTrigger().getUnit() == TriggerUnit.HOURS) {
                schBuilder.withIntervalInHours(to.getTrigger().getUnitValue());
                triggerName = to.getTrigger().getUnitValue() + "_Hours";
            } else if (to.getTrigger().getUnit() == TriggerUnit.MINUTES) {
                schBuilder.withIntervalInMinutes(to.getTrigger().getUnitValue());
                triggerName = to.getTrigger().getUnitValue() + "_Minutes";
            } else if (to.getTrigger().getUnit() == TriggerUnit.SECONDS) {
                schBuilder.withIntervalInSeconds(to.getTrigger().getUnitValue());
                triggerName = to.getTrigger().getUnitValue() + "_Seconds";
            } else if (to.getTrigger().getUnit() == TriggerUnit.MILLISECOND) {
                schBuilder.withIntervalInMilliseconds(to.getTrigger().getUnitValue());
                triggerName = to.getTrigger().getUnitValue() + "_Milliseconds";
            }
            if (to.getTrigger().getRepeat().isForever()) {
                schBuilder.repeatForever();
                triggerName = "repeat_for_every_"+ triggerName + " forever";
            } else if (to.getTrigger().getRepeat().isOneTime()) {
                schBuilder.withRepeatCount(0);
                triggerName = "run_just_one_time";
            }
            triggerName = to.getJobName() + "_Job_SimpleTrigger_"+ triggerName;
        }

        return TriggerBuilder
                .newTrigger()
                .forJob(jobDtl)
                .withIdentity(triggerName)
                .withSchedule(schBuilder)
                .build();
    }
}
