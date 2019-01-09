package com.emirates.ihc.scheduler.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("Trigger Detail")
public class TriggerTO {

    @ApiModelProperty("Trigger Type")
    private TriggerType type;
    private TriggerUnit unit;
    private Integer unitValue;
    private String cronExpression;
    private RepeatDetail repeat;

    public TriggerType getType() {
        return type;
    }

    public void setType(TriggerType type) {
        this.type = type;
    }

    public TriggerUnit getUnit() {
        return unit;
    }

    public void setUnit(TriggerUnit unit) {
        this.unit = unit;
    }

    public Integer getUnitValue() {
        return unitValue;
    }

    public void setUnitValue(Integer unitValue) {
        this.unitValue = unitValue;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public RepeatDetail getRepeat() {
        return repeat;
    }

    public void setRepeat(RepeatDetail repeat) {
        this.repeat = repeat;
    }
}
