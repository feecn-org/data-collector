package com.feecn.org.controller;

import com.alibaba.fastjson.JSONObject;
import com.feecn.org.service.job.BaseJob;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 *
 * @author pczhangyu
 * @date 2018/7/3
 */
@RestController
@RequestMapping("/job")
public class JobController {

    private static Logger logger = LoggerFactory.getLogger(JobController.class);

    @Autowired
    @Qualifier("Scheduler")
    private Scheduler scheduler;

    @PostMapping(value="/add")
    public void addJob(@RequestParam(value="jobClassName")String jobClassName,
                       @RequestParam(value="jobGroupName")String jobGroupName,
                       @RequestParam(value="cronExpression")String cronExpression,
                       @RequestBody JSONObject paramObject
                       ) throws Exception{
        scheduler.start();
        logger.info(String.format("object is %s",paramObject.toJSONString()));
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("paramObject",jobDataMap);
        JobDetail jobDetail = JobBuilder.newJob(getClass(jobClassName).getClass()).withIdentity(jobClassName, jobGroupName).setJobData(jobDataMap).build();
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobClassName, jobGroupName).withSchedule(scheduleBuilder).build();
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new Exception("创建定时任务失败");
        }
    }


    @PostMapping(value="/pause")
    public void pauseJob(@RequestParam(value="jobClassName")String jobClassName, @RequestParam(value="jobGroupName")String jobGroupName) throws Exception
    {
        scheduler.pauseJob(JobKey.jobKey(jobClassName, jobGroupName));
    }


    @PostMapping(value="/resume")
    public void resumeJob(@RequestParam(value="jobClassName")String jobClassName, @RequestParam(value="jobGroupName")String jobGroupName) throws Exception
    {
        scheduler.resumeJob(JobKey.jobKey(jobClassName, jobGroupName));
    }

    @PostMapping(value="/reschedule")
    public void rescheduleJob(@RequestParam(value="jobClassName")String jobClassName,
                              @RequestParam(value="jobGroupName")String jobGroupName,
                              @RequestParam(value="cronExpression")String cronExpression) throws Exception
    {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobClassName, jobGroupName);
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            throw new Exception("更新定时任务失败");
        }
    }

    @PostMapping(value="/delete")
    public void deleteJob(@RequestParam(value="jobClassName")String jobClassName, @RequestParam(value="jobGroupName")String jobGroupName) throws Exception
    {
        scheduler.pauseTrigger(TriggerKey.triggerKey(jobClassName, jobGroupName));
        scheduler.unscheduleJob(TriggerKey.triggerKey(jobClassName, jobGroupName));
        scheduler.deleteJob(JobKey.jobKey(jobClassName, jobGroupName));
    }

//    @GetMapping(value="/queryjob")
//    public Map<String, Object> queryjob(@RequestParam(value="pageNum")Integer pageNum, @RequestParam(value="pageSize")Integer pageSize)
//    {
//        PageInfo<JobAndTrigger> jobAndTrigger = iJobAndTriggerService.getJobAndTriggerDetails(pageNum, pageSize);
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("JobAndTrigger", jobAndTrigger);
//        map.put("number", jobAndTrigger.getTotal());
//        return map;
//    }

    public static BaseJob getClass(String classname) throws Exception
    {
        Class<?> class1 = Class.forName(classname);
        return (BaseJob)class1.newInstance();
    }


}
