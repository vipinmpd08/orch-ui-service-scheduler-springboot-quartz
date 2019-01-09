package com.emirates.ihc.scheduler.service.impl;

import com.emirates.ihc.framework.exceptions.InvalidInputException;
import com.emirates.ihc.scheduler.domain.JobInstanceTO;
import com.emirates.ihc.scheduler.exception.JobSchedulingException;
import com.emirates.ihc.scheduler.service.IJobInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobInstanceService implements IJobInstanceService {


    public JobInstanceTO saveJobInstanceDetail(JobInstanceTO jobInstanceTO) throws InvalidInputException {
        if (jobInstanceTO == null) {
            throw new InvalidInputException("JobInstanceTO cannot be null", new Exception());
        }
        if (jobInstanceTO.getQuartzInstanceId() == null) {
            throw new InvalidInputException("QuartzInstanceId cannot be null", new Exception());
        }
        if (jobInstanceTO.getStatus() == null) {
            throw new InvalidInputException("Status cannot be null", new Exception());
        }
        /*
        JobInstanceEntity ent = new JobInstanceEntity();
        ent.setJobInstanceId(jobInstanceTO.getJobInstanceId());
        ent.setQuartzInstanceId(jobInstanceTO.getQuartzInstanceId());
        ent.setStatus(jobInstanceTO.getStatus().name());
        ent.setStartDate(jobInstanceTO.getStartDate());
        ent.setEndDate(jobInstanceTO.getEndDate());
        //ent = repo.save(ent);
        jobInstanceTO.setJobInstanceId(ent.getJobInstanceId());
        */

        return jobInstanceTO;
    }

}
