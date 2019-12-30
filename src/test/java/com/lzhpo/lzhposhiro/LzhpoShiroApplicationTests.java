package com.lzhpo.lzhposhiro;

import com.google.common.collect.Lists;
import com.lzhpo.core.config.RedisUtil;
import com.lzhpo.core.service.PrizeDataService;
import com.lzhpo.core.work.MainWork;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LzhpoShiroApplicationTests {


    @Autowired
    private  MainWork work;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private PrizeDataService prizeDataService;


    @Test
    public void generateData() {

        List<Integer> list=Lists.newArrayList(1,2,3);
        List<Integer> noData=Lists.newArrayList();
        String a= StringUtils.collectionToCommaDelimitedString(list);
        String b= StringUtils.collectionToCommaDelimitedString(noData);
        System.out.println();

    }

}
