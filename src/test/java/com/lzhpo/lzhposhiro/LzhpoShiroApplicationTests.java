package com.lzhpo.lzhposhiro;
import	java.lang.reflect.Field;

import com.lzhpo.core.domain.PrizeInfoEntity;
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
    private SumValueDataService  service;


    @Test
    public void generateData() {

        Class clazz= SumDataStaticsVo.class;
        Field[] fields =clazz.getDeclaredFields();
        for (Field field : fields) {
            String name=field.getName();
            String name2=name.substring(0,1).toUpperCase()
                    +name.substring(1);
            String str=" Set<Integer> "+name+"OkList= Sets.newHashSet();\n" +
                    "        Set<Integer> "+name+"NoList= Sets.newHashSet();\n" +
                    "        Integer "+name+"Ok=0;\n" +
                    "        Integer "+name+"No=0;";
            System.out.println(str);

        }




    }

}
