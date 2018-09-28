package com.feecn.org.service.job.impl;

import com.alibaba.fastjson.JSONObject;
import com.feecn.org.service.job.BaseJob;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * spark / hbase 任务调度
 * @author pczhangyu
 * @date 2017/12/25
 */

public class SparkJob implements BaseJob {

    private static Logger logger = LoggerFactory.getLogger(SparkJob.class);

//    @Autowired
//    private CompareService compareService;

    /**
     */
    public void sparkJob(JSONObject paramObject) {
        logger.info(paramObject.toJSONString());
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        logger.info(((JSONObject) jobDataMap.get("paramObject")).toJSONString());
        this.sparkJob(((JSONObject) jobDataMap.get("paramObject")));
    }
}
