package quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.text.ParseException;

public class CronScheduler {
    //创建调度器
    public static Scheduler getScheduler() throws SchedulerException {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        return  schedulerFactory.getScheduler();
    }

    public static Trigger getTrigger() throws ParseException {
        //每5秒调度一次
        String cronExpression="*/5 * * * * ?";
//        Trigger trigger=new CronTriggerImpl("cronTrigger",Scheduler.DEFAULT_GROUP,cronExpression); //按时间间隔执行
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1","group3")
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .build();
        return trigger;
    }

    public static void main(String[] args) throws ParseException, SchedulerException {
        //创建任务
        JobDetail jobDetail = JobBuilder.newJob(MyJob.class).withIdentity("job1","group1").build();
        jobDetail.getJobDataMap().put("Test", "This is test value22");
        //获取定时触发器
        Trigger trigger = getTrigger();
        //获取调度器
        Scheduler scheduler = getScheduler();
        //使用调度器 将任务 与 触发器组合 起来并执行
        scheduler.scheduleJob(jobDetail,trigger);
        scheduler.start();

    }
}
