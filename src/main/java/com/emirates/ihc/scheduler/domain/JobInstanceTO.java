package com.emirates.ihc.scheduler.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;


public class JobInstanceTO {

    public JobInstanceTO() {
    }

    public JobInstanceTO(JobInstanceStatus status) {
        this.status = status;
    }

    private Long jobInstanceId;

    private String quartzInstanceId;

    private JobInstanceStatus status;

    private Date startDate;

    private Date endDate;

    public Long getJobInstanceId() {
        return jobInstanceId;
    }

    public void setJobInstanceId(Long jobInstanceId) {
        this.jobInstanceId = jobInstanceId;
    }

    public String getQuartzInstanceId() {
        return quartzInstanceId;
    }

    public void setQuartzInstanceId(String quartzInstanceId) {
        this.quartzInstanceId = quartzInstanceId;
    }

    public JobInstanceStatus getStatus() {
        return status;
    }

    public void setStatus(JobInstanceStatus status) {
        this.status = status;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
