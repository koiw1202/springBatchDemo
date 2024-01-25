package com.demo.jpa;

import com.demo.config.QuartzJob;
import com.demo.config.QuartzJobListner;
import com.demo.config.QuartzTriggerListner;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-01-25        koiw1       최초 생성
 */
@Slf4j
@Configuration
public class QuartzService {

    private final Scheduler scheduler ;

    public QuartzService(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @PostConstruct
    public void init() {
        try {
            scheduler.clear();

            scheduler.getListenerManager().addJobListener(new QuartzJobListner());

            scheduler.getListenerManager().addTriggerListener(new QuartzTriggerListner());

            Map paramMap = new HashMap<>() ;
            paramMap.put("executeCount", 1) ;
            paramMap.put("date", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))) ;

            addJob(QuartzJob.class, "QuartzJob", "Quartz Job 입니다", paramMap, "0/5 * * * * ?") ;

        } catch(Exception e) {
            log.info(e.getMessage());
        }
    }

    public <T extends Job> void addJob(Class<? extends Job> job, String name, String dsec, Map paramsMap, String cron) throws SchedulerException {
        JobDetail jobDetail = buildJobDetail(job, name, dsec, paramsMap) ;
        Trigger trigger = buildCronTrigger(cron) ;
        if(scheduler.checkExists(jobDetail.getKey()))
            scheduler.deleteJob(jobDetail.getKey()) ;

        scheduler.scheduleJob(jobDetail, trigger) ;

    }

    public <T extends  Job> JobDetail buildJobDetail(Class<? extends  Job> job, String name, String dsec, Map paramsMap) {
        JobDataMap jobDataMap = new JobDataMap() ;
        jobDataMap.putAll(paramsMap);

        return JobBuilder
                .newJob()
                .withIdentity(name)
                .withDescription(dsec)
                .usingJobData(jobDataMap)
                .build() ;
    }

    public Trigger buildCronTrigger(String cronExp) {
        return TriggerBuilder.newTrigger()
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExp))
                .build() ;
    }
}










