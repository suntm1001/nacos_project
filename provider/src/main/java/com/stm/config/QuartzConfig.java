package com.stm.config;

import com.stm.controller.QuartzTestJob;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Autowired
    private Scheduler asched;
    /**
     * 创建定时任务
     * @return
     */
    public JobDetail quartzTestDetail(){
        JobDetail jobDetail = JobBuilder.newJob(QuartzTestJob.class)
                //添加认证信息
                .withIdentity("quartzTestDetail","QUARTZ_TEST")
                .storeDurably()
                .build();
        
        return jobDetail;
    }

    /**
     * 添加触发器
     * @return
     */
    public Trigger quartzTestJobTrigger(){
        //执行频率
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("*/5 * * * * ?");
        
        Trigger trigger = TriggerBuilder.newTrigger().forJob(quartzTestDetail())
                //添加认证信息
                .withIdentity("quartzTestJobTrigger", "QUARTZ_TEST_JOB_TRIGGER")
                //添加执行规则
                .withSchedule(cronScheduleBuilder)
                .build();
        return trigger;
    }
    
    @Bean
    public void scs (){
        JobDetail jobDetail = quartzTestDetail();
        Trigger trigger =quartzTestJobTrigger();
        try {
            asched.scheduleJob(jobDetail,trigger);
            boolean flag = asched.isShutdown();
            if(!flag){
                asched.start();
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
    
}
