package com.lzhpo.lzhposhiro;
import	java.lang.reflect.Field;

import com.lzhpo.core.domain.PrizeInfoEntity;
import com.lzhpo.core.domain.concord.BorderDataVo;
import com.lzhpo.core.domain.concord.SumDataStaticsVo;
import com.lzhpo.core.domain.concord.SumDataVo;
import com.lzhpo.core.service.DragonDataService;
import com.lzhpo.core.service.PrizeDataService;
import com.lzhpo.core.service.SumValueDataService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LzhpoShiroApplicationTests {



    @Autowired
    private DragonDataService dataService;


    @Test
    public void generateData() {
        List<BorderDataVo> list=  dataService.getBorderDisIndexList();
        list.forEach(System.out::println);
    }
}
