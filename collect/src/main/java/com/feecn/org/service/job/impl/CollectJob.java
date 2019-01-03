package com.feecn.org.service.job.impl;

import com.alibaba.fastjson.JSONObject;
import com.feecn.org.builder.JedisBuilder;
import com.feecn.org.service.job.BaseJob;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 收集任务调度
 * @author pczhangyu
 * @date 2017/12/25
 */
public class CollectJob implements BaseJob {

    private static Logger logger = LoggerFactory.getLogger(CollectJob.class);

    /**
     */
    public void collectJob(JSONObject paramObject) {
        try {
            String pushKey = paramObject.getString("pushKey");
            Document document = Jsoup.connect(paramObject.getString("url")).get();
            Long lpush = JedisBuilder.lpush(pushKey, document.toString());
            logger.info(String.format("document is %s",document.toString()));
            logger.info(String.format("un consumed %s",lpush.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info(paramObject.toJSONString());
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JSONObject paramObject = ((JSONObject) context.getMergedJobDataMap().get("paramObject"));
        logger.info(String.format("object param is %s",paramObject.toJSONString()));
        this.collectJob(paramObject);
    }
}
