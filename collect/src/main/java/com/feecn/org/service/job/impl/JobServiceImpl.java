package com.feecn.org.service.job.impl;

import com.feecn.org.service.job.JobService;
import com.feecn.org.service.job.entity.JobAndTrigger;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.quartz.simpl.RAMJobStore;

import java.util.ArrayList;

/**
 * Created by pczhangyu on 2018/7/4.
 */
public class JobServiceImpl implements JobService{


    @Override
    public PageInfo<JobAndTrigger> queryJobs(int pageNum, int pageSize) {
        return null;
    }
}
