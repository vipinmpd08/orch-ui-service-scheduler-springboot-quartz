package com.emirates.ihc.scheduler.domain;

public class RepeatDetail {
    private boolean forever;
    private boolean oneTime;

    public boolean isForever() {
        return forever;
    }

    public void setForever(boolean forever) {
        this.forever = forever;
    }

    public boolean isOneTime() {
        return oneTime;
    }

    public void setOneTime(boolean oneTime) {
        this.oneTime = oneTime;
    }
}
