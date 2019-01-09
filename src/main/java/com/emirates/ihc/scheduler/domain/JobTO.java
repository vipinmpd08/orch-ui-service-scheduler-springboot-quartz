package com.emirates.ihc.scheduler.domain;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class JobTO {

    @NotNull
    private String jobClassName;

    @NotNull
    private String jobName;

    private TriggerTO trigger;

    public String getJobClassName() {
        return jobClassName;
    }

    public void setJobClassName(String jobClassName) {
        this.jobClassName = jobClassName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public TriggerTO getTrigger() {
        return trigger;
    }

    public void setTrigger(TriggerTO trigger) {
        this.trigger = trigger;
    }
}
