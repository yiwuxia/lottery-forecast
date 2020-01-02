package com.lzhpo.lzhposhiro;

import com.lzhpo.core.domain.PrizeStaticVo;
import com.lzhpo.core.domain.dragon.DragonPhoenixStaticVo;
import com.lzhpo.core.domain.dragon.DragonPhoenixVo;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.util.Arrays;

@SpringBootTest
public class DemoTest {




    @Test
    public  void a(){

     //   DragonPhoenixVo
       Class clazz=   DragonPhoenixStaticVo.class;
       Field[] arr= clazz.getDeclaredFields();
       for(Field field:arr){
           String fieldName=field.getName();
           if (fieldName.startsWith("dragon")
                   || fieldName.startsWith("phoen")
                    || fieldName.startsWith("area")
           ){
               String UpperFirstLetterName=fieldName.substring(0,1).toUpperCase()
                       +fieldName.substring(1);
               String str=" maxContinu.set"+UpperFirstLetterName+"(Collections.max("+fieldName+"OkList));\n" +
                       "        maxMiss.set"+UpperFirstLetterName+"(Collections.max("+fieldName+"NoList));";
               System.out.println(str);
           }
       }

    }


}
