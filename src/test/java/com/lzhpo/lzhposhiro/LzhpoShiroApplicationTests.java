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
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LzhpoShiroApplicationTests {


    @Autowired
    private  MainWork work;

    @Autowired
    private RedisUtil redisUtil;


    @Test
    public void generateData() {
        Dog dog=new Dog(1,"hello");
        Dog dog2=new Dog(1,"hello");
        String keys= JSON.toJSONString(dog);
        redisUtil.set("lijin",keys);
        System.out.println(redisUtil.get("lijin"));
    }

}
