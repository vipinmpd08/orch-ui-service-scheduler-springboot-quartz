package com.emirates.ihc.scheduler.service;

import com.emirates.ihc.scheduler.domain.JobInstanceTO;
import com.emirates.ihc.scheduler.domain.JobTO;
import com.emirates.ihc.framework.exceptions.InvalidInputException;
import com.emirates.ihc.scheduler.exception.JobNotRunningException;
import com.emirates.ihc.scheduler.exception.JobSchedulingException;
import org.quartz.UnableToInterruptJobException;

public interface IJobService {

    //Boolean stopRunningJobInstance(String jobName);

    JobInstanceTO scheduleJob(JobTO job) throws InvalidInputException, JobSchedulingException;

    JobInstanceTO interruptRunningJob(String jobName) throws JobNotRunningException, UnableToInterruptJobException;
}
