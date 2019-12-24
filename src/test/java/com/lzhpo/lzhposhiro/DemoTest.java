package com.lzhpo.lzhposhiro;

import com.lzhpo.core.domain.PrizeStaticVo;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;

@SpringBootTest
public class DemoTest {


    @Test
    public  void a1(){
        // 10 9 8 720

    }


    @Test
    public  void a(){

         //
        Class clz= PrizeStaticVo.class;
        Field[] fiields= clz.getDeclaredFields();
        int i=1;
        for (Field field : fiields){
            if (field.getName().contains("0")){
                String field1 = field.getName();
                String  field2=field1.substring(0,1).toUpperCase()+field1.substring(1);
                StringBuffer buffer=new StringBuffer();
             //   System.out.println(buffer.toString());
                buffer=new StringBuffer();
                buffer.append("  <td   class=\"btn-nums-select\">"+i+"</td>");
                System.out.println(buffer.toString());
                i++;
                if (i==11){
                    i=1;
                }
            }
        }



    }


}
