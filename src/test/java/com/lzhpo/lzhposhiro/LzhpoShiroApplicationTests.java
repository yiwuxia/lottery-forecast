package com.lzhpo.lzhposhiro;
import	java.lang.reflect.Method;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.lzhpo.core.config.RedisUtil;
import com.lzhpo.core.domain.PrizeData;
import com.lzhpo.core.domain.PrizeDetailVo;
import com.lzhpo.core.domain.PrizeInfoEntity;
import com.lzhpo.core.service.PrizeDataService;
import com.lzhpo.core.utils.DataGeneratorUtil;
import com.lzhpo.core.work.MainWork;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
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

        String str="a,b,c,d";
        String [] arr= StringUtils.split(str,",");
        System.out.println(Arrays.toString(arr));

    }

}
