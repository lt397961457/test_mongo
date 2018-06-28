package quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class MainScheduler {
    //创建调度器
    public static Scheduler getScheduler() throws SchedulerException {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        return  schedulerFactory.getScheduler();
    }

    public static void schedulerJob() throws SchedulerException {
        //创建任务
        JobDetail jobDetail = JobBuilder.newJob(MyJob.class).withIdentity("job1","group1").build();
        jobDetail.getJobDataMap().put("Test", "This is test value");

        //创建触发器
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1","group3")
                        .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(3).repeatForever())
                        .build();
        Scheduler scheduler = getScheduler();

        //将任务及其触发器放入调度器
        scheduler.scheduleJob(jobDetail,trigger);
        scheduler.start();

    }

    public static void main(String[] args) throws SchedulerException {
        MainScheduler.schedulerJob();
    }
}
