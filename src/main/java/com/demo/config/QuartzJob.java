package com.demo.config;

import com.demo.jpa.Market;
import com.demo.jpa.MarketRepository;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-01-25        koiw1       최초 생성
 */
@Component
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class QuartzJob implements Job {

    @Autowired
    private MarketRepository marketRepository ;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Quartz Job Executed");

        JobDataMap dataMap = context.getJobDetail().getJobDataMap() ;
        log.info("dataMap date : {}", dataMap.get("date"));
        log.info("dataMap executeCount : {}", dataMap.get("executeCount"));

//        JobDataMap를 통해 Job의 실행 횟수를 받아서 횟수 + 1을 한다.
        int cnt = (int) dataMap.get("executeCount") ;
        dataMap.put("executeCount", ++cnt);

//        Market 테이블에 pooney_현재시간 데이터를 insert 한다.
        Market market = new Market() ;
        market.setName(String.format("pooney_%s", dataMap.get("date"))) ;
        market.setPrice(3000) ;
        marketRepository.save(market) ;

    }
}











