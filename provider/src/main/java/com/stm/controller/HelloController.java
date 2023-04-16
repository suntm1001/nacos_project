package com.stm.controller;

import com.alibaba.fastjson.JSONObject;
import com.stm.annotation.AnnotationTest;
import com.stm.annotation.PassToken;
import com.stm.annotation.UserLoginToken;
import com.stm.common.Result;
import com.stm.pojo.User;
import com.stm.support.MyTask;
import com.stm.support.MyThreadPool;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
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
    @AnnotationTest
    public List<User> dictTest(String name){
        List<User> list = new ArrayList<>();
        User user = new User();
        user.setId("1");
        user.setUsername("sh");
        user.setPassword("12");
        user.setSex("1");
        User user1 = new User();
        user1.setId("1");
        user1.setUsername("sh");
        user1.setPassword("12");
        user1.setSex("1");
        list.add(user1);
        return list;
    }

    /**
     * aop数据字典转化测试
     * @return
     */
    @RequestMapping("/threadPool")
    @PassToken
    public Result threadPool() throws ExecutionException, InterruptedException {
        JSONObject jsonObject = new JSONObject();
        
        long start = System.currentTimeMillis();
        ThreadPoolExecutor executor = MyThreadPool.createThreadPool();
        List<Future<String>> list = new LinkedList<>();
        List<Integer> tempList = new ArrayList<>();
        //创建测试用例
        for(int i=0; i<100; i++) {
            tempList.add(100*(i+1));
        }
        for(int i=0; i<tempList.size(); i++) {
            MyTask worker = new MyTask(tempList.get(i));	//执行任务
            //下面两行代码是将执行的返回结果进行汇总
            Future<String> submit = executor.submit(worker);
            list.add(submit);
        }
        List<String> result = new LinkedList<>();
        int sucess = 0;
        int fail=0;
        for(Future<String> f : list) {
            //将汇总好的结果进行轮询，判断任务是否执行完成，确保每个任务执行完成后将结果添加到结果集中
            while(true) {
                if(f.isDone() && !f.isCancelled()) {
                    String object = f.get();
                    if("成功".equals(object)){
                        sucess++;
                    }else{
                        fail++;
                    }
                    result.add(object);
                    break;
                }
            }
        }
        long end = System.currentTimeMillis();
        log.info("work time:"+(end - start)+"ms");
        //终止线程
        executor.shutdown();
        while(!executor.isTerminated()) {}
        log.info("Finished all threads");

        jsonObject.put("result","成功条数："+sucess+",失败条数："+fail);
        return Result.success(jsonObject);
    }

    @RequestMapping("/dataTest")
    @UserLoginToken
    public Result<Object> dataTest(@RequestBody User userqu){
        JSONObject jsonObject = new JSONObject();
        List<User> list = new ArrayList<>();
        User user = new User();
        user.setId("1001");
        user.setSex("男");
        user.setUsername("xiaoshan");

        list.add(user);
        user.setUsername("222");
        list.add(user);
        jsonObject.put("list", list);
        return Result.success(jsonObject);
    }
}
