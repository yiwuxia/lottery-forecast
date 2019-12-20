package com.lzhpo.core.work;

import com.lzhpo.core.domain.PrizeData;
import com.lzhpo.core.service.PrizeDataService;
import com.lzhpo.core.utils.DataGeneratorUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="lijing1@wxchina.com@wxchina.com">Lijin</a>
 * @Description TODO
 * @Date 2019/12/19 14:44
 * @Version 1.0
 **/
@Component
public class MainWork {


    @Value("${remote.simulation:0}")
    private Integer simulation;

    @Autowired
    private PrizeDataService prizeDataService;

    private AtomicInteger termNum = new AtomicInteger(1);

    private static final FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");


    ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
    //使用的无界队列注意溢出
    ExecutorService handlerThreadPool = new ThreadPoolExecutor(5, 5,
            5, TimeUnit.MINUTES, new ArrayBlockingQueue<>(1000));


    BlockingQueue<PrizeData> dataQueue = new ArrayBlockingQueue<>(1000);

    @PostConstruct
    public void init() {
        /**
         * 上一次任务执行完毕后才执行下一次
         */
        scheduledThreadPool.scheduleWithFixedDelay(
                new FetchRemoteData(), 0, 5, TimeUnit.MINUTES);
        handlerThreadPool.execute(new HandlerRemoteData());
    }


    /**
     * 取数据线程
     */
    private class FetchRemoteData implements Runnable {

        @Override
        public void run() {
            // itemNo;01,02,....10
            String data = "";
            if (simulation == 1) {
                data = generateSimulationData();
                if (StringUtils.isNotBlank(data)) {
                    dataQueue.offer(DataGeneratorUtil.converStrToPrizeData(data));
                }
            } else {

            }

        }


    }

    public String generateSimulationData() {

        StringBuffer buffer = new StringBuffer();
        buffer.append(UUID.randomUUID().toString());
        buffer.append(";");
        buffer.append(DataGeneratorUtil.generate());
        buffer.append(";").append(format.format(new Date()));
        return buffer.toString();
    }

    /**
     * 处理数据线程
     */
    private class HandlerRemoteData implements Runnable {

        @Override
        public void run() {

            while (true) {
                try {
                    PrizeData prizeData = dataQueue.take();
                    prizeDataService.handlerOriginDataTrend(prizeData);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }

    }


}
