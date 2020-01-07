package com.lzhpo.lzhposhiro;

import com.google.common.collect.Lists;
import com.lzhpo.core.domain.concord.SumDataStaticsVo;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;

@SpringBootTest
public class DemoTest {




    @Test
    public  void a(){


        Class clazz= SumDataStaticsVo.class;
        Field[] fields =clazz.getDeclaredFields();
        int i=10;
        for (Field field : fields) {
            String name=field.getName();
            String name2=name.substring(0,1).toUpperCase()
                    +name.substring(1);
            String str="maxContinu.set"+name2+"(Collections.max("+name+"OkList));\n" +
                    "        maxMiss.set"+name2+"(Collections.max("+name+"NoList));";
            System.out.println(str);

        }

    }


}
