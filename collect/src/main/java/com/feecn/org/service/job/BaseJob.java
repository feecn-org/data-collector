package com.feecn.org.service.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by pczhangyu on 2018/7/4.
 */
public interface BaseJob extends Job{
    public void execute(JobExecutionContext context) throws JobExecutionException;
}
