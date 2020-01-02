package com.lzhpo.lzhposhiro;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.lzhpo.core.domain.PrizeStaticVo;
import com.lzhpo.core.domain.dragon.DragonPhoenixStaticVo;
import com.lzhpo.core.domain.dragon.DragonPhoenixVo;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class DemoTest {




    @Test
    public  void a(){
       String str="";
        List<String> myList=Splitter.on(",")
                .omitEmptyStrings()
                .splitToList(str);
        myList.forEach(System.out::println);
        System.out.println(myList.size());

    }

}
