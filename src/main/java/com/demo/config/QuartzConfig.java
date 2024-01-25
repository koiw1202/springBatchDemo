package com.demo.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-01-25        koiw1       최초 생성
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class QuartzConfig {

    private final DataSource dataSource ;
    private final ApplicationContext applicationContext ;
    private final PlatformTransactionManager platformTransactionManager ;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean() ;
        AutoWiringSpringBeanJobFactory autoWiringSpringBeanJobFactory = new AutoWiringSpringBeanJobFactory() ;
        autoWiringSpringBeanJobFactory.setApplicationContext(applicationContext);
        schedulerFactoryBean.setJobFactory(autoWiringSpringBeanJobFactory);
        schedulerFactoryBean.setDataSource(dataSource);
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setAutoStartup(true);
        schedulerFactoryBean.setTransactionManager(platformTransactionManager);
        schedulerFactoryBean.setQuartzProperties(quartzProperties());
        return schedulerFactoryBean ;
    }

    private Properties quartzProperties() {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean() ;
        propertiesFactoryBean.setLocation(new ClassPathResource("quartz.properties")) ;
        Properties properties = null ;
        try {
            propertiesFactoryBean.afterPropertiesSet(); ;
            properties = propertiesFactoryBean.getObject() ;

        } catch(Exception e) {
            log.error("quartzProperties parse error : {}", e.getMessage());
        }
        return properties ;
    }

}










