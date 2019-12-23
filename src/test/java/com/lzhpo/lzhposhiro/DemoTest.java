package com.lzhpo.lzhposhiro;

import com.lzhpo.core.domain.PrizeDetailVo;
import com.lzhpo.core.domain.PrizeStaticVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
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
        for (Field field : fiields){
            if (field.getName().contains("0")){
                String field1 = field.getName();
                String  field2=field1.substring(0,1).toUpperCase()+field1.substring(1);
                StringBuffer buffer=new StringBuffer();
             //   System.out.println(buffer.toString());
                buffer=new StringBuffer();
                buffer.append(" if (       p.get"+field2+"().startsWith(\"ok\")) {\n" +
                        "                if (occurTimes.get"+field2+"() == null) {\n" +
                        "                    occurTimes.set"+field2+"(1);\n" +
                        "                } else {\n" +
                        "                    occurTimes.set"+field2+"(occurTimes.get"+field2+"() + 1);\n" +
                        "                }\n" +
                        "                //未出连续中断\n" +
                        "                if ("+field1+"No>=0){\n" +
                        "                    "+field1+"NoList.add("+field1+"No);\n" +
                        "                    "+field1+"No=0;\n" +
                        "                }\n" +
                        "                "+field1+"Ok="+field1+"Ok+1;\n" +
                        "            }else{\n" +
                        "                if ("+field1+"Ok>=0){\n" +
                        "                    "+field1+"OkList.add("+field1+"Ok);\n" +
                        "                    "+field1+"Ok=0;\n" +
                        "                }\n" +
                        "                "+field1+"No="+field1+"No+1;\n" +
                        "            }");
                System.out.println(buffer.toString());
            }
        }



    }


}
