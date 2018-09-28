package com.feecn.org.service.job;

import com.feecn.org.service.job.entity.JobAndTrigger;
import com.github.pagehelper.PageInfo;

/**
 *
 * @author pczhangyu
 * @date 2018/7/4
 */
public interface JobService {

    /**
     * 从JobTrigger中获取所有job
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<JobAndTrigger> queryJobs(int pageNum,int pageSize);
}
