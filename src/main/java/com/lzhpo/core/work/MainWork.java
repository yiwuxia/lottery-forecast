package com.lzhpo.core.work;

import com.lzhpo.core.domain.PrizeData;
import com.lzhpo.core.utils.DataGeneratorUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="lijing1@wxchina.com@wxchina.com">Lijin</a>
 * @Description TODO
 * @Date 2019/12/19 14:44
 * @Version 1.0
 **/
@Component
public class MainWork{


    @Value("${remote.simulation:0}")
    private Integer simulation;

    private AtomicInteger  termNum=new AtomicInteger(1);


    ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(2);

    BlockingQueue<PrizeData> dataQueue=new ArrayBlockingQueue<>(1000);

    @PostConstruct
    public void init() {
        /**
         * 上一次任务执行完毕后才执行下一次
         */
        scheduledThreadPool.scheduleWithFixedDelay(
                new FetchRemoteData(), 0, 5, TimeUnit.MINUTES);
    }


    /**
     * 取数据线程
     */
    private class FetchRemoteData implements  Runnable{

        @Override
        public void run() {
            // itemNo;01,02,....10
            String data="";
            if (simulation==1){
                 data=generateSimulationData();
            }else {

            }
            if (StringUtils.isNotBlank(data)){
                dataQueue.offer(DataGeneratorUtil.converStrToPrizeData(data));
            }

        }

        private String generateSimulationData() {

            StringBuffer buffer=new StringBuffer();
            buffer.append(termNum.incrementAndGet());
            buffer.append(";");
            buffer.append(DataGeneratorUtil.generate());
            return buffer.toString();

        }
    }


}
