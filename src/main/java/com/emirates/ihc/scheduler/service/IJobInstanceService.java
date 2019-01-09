package com.emirates.ihc.scheduler.service;

import com.emirates.ihc.framework.exceptions.InvalidInputException;
import com.emirates.ihc.scheduler.domain.JobInstanceTO;
import com.emirates.ihc.scheduler.exception.JobSchedulingException;

public interface IJobInstanceService {

    JobInstanceTO saveJobInstanceDetail(JobInstanceTO to) throws InvalidInputException;
}
