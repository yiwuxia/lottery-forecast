package com.lzhpo.lzhposhiro;

import com.lzhpo.core.domain.PrizeStaticVo;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.util.Arrays;

@SpringBootTest
public class DemoTest {


    @Test
    public  void a1(){
        // 10 9 8 720

    }


    @Test
    public  void a(){

         //
        String str=",,c,d";
        String [] arr= StringUtils.split(str,",");
        //arr=str.split(",");
        arr= org.springframework.util.StringUtils.split(str,",");
        System.out.println(arr.length);
        System.out.println(Arrays.toString(arr));



    }


}
