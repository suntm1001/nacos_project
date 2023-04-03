package com.stm.controller;

import com.alibaba.fastjson.JSONObject;
import com.stm.annotation.UserLoginToken;
import com.stm.common.Result;
import com.stm.pojo.User;
import java.util.ArrayList;
import java.util.List;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @Autowired
    private Scheduler asched;
    
    @RequestMapping("/hello")
    @UserLoginToken
    public Result<Object> hello(){
        JSONObject jsonObject = new JSONObject();
        String str ="git@github.com:suntm1001/nacos_project.git";
        String s = "ss";
        String test ="";
        String hebing="da";
        String s221="";
        jsonObject.put("hh", "22");
        return Result.success(jsonObject);
    }

    /**
     * 停止定时任务
     * @return
     */
    @RequestMapping("/stopJob")
    public String stopJob(){
        JobKey jobKey = JobKey.jobKey("quartzTestDetail","QUARTZ_TEST");
        try {
            asched.pauseJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return "sucess";
    }

    /**
     * 恢复定时任务
     * @return
     */
    @RequestMapping("/resumeJob")
    public String resumeJob(){
        JobKey jobKey = JobKey.jobKey("quartzTestDetail","QUARTZ_TEST");
        try {
            asched.resumeJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return "sucess";
    }

    /**
     * 添加定时任务
     * @return
     */
    @RequestMapping("/addJob")
    public String addJob() throws ClassNotFoundException {
        String classname = "com.stm.job.QuartzTestTwoJob";
        //添加定时任务
        addJobImpl(classname);
        return "sucess";
    }

    /**
     * 添加定时任务
     * @param classname
     */
    public void addJobImpl(String classname){
        try {
            JobDetail jobDetail = JobBuilder.newJob(getClass(classname))
                    //添加认证信息
                    .withIdentity("com.stm.job.QuartzTestTwoJob","QuartzTestTwoJob")
                    .storeDurably()
                    .build();
            //执行频率
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("*/5 * * * * ?");

            Trigger trigger = TriggerBuilder.newTrigger().forJob(jobDetail)
                    //添加认证信息
                    .withIdentity("com.stm.job.QuartzTestTwoJob", "QuartzTestTwoJob")
                    //添加执行规则
                    .withSchedule(cronScheduleBuilder)
                    .build();
            asched.scheduleJob(jobDetail,trigger);
            boolean flag = asched.isShutdown();
            if(!flag){
                asched.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除定时任务
     * @return
     */
    @RequestMapping("/delJob")
    public String delJob() {
        try {
            asched.pauseTrigger(TriggerKey.triggerKey("com.stm.job.QuartzTestTwoJob","QuartzTestTwoJob"));
            asched.unscheduleJob(TriggerKey.triggerKey("com.stm.job.QuartzTestTwoJob","QuartzTestTwoJob"));
            asched.deleteJob(new JobKey("com.stm.job.QuartzTestTwoJob","QuartzTestTwoJob"));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    return "sucess";
    }
    /**
     * 将类名反射为类
     * @param classname
     * @return
     * @throws Exception
     */
    public static Class<? extends QuartzJobBean> getClass(String classname) throws Exception{
        //将前端传的定时任务类名反射为类
        Class<?> class1 = Class.forName(classname);
        return (Class<? extends QuartzJobBean>) class1;
    }


    /**
     * aop数据字典转化测试
     * @return
     */
    @RequestMapping("/dictTest")
    //@UserLoginToken
    public List<User> dictTest(){
        List<User> list = new ArrayList<>();
        User user = new User();
        user.setId("1");
        user.setUsername("sh");
        user.setPassword("12");
        user.setSex("1");
        list.add(user);
        return list;
    }
}
