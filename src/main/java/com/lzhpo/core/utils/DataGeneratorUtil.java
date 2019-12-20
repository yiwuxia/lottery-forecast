package com.lzhpo.core.utils;
import java.text.ParseException;
import	java.util.Random;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lzhpo.core.domain.PrizeData;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.util.StringUtils;

import javax.swing.plaf.TableUI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author <a href="lijing1@wxchina.com@wxchina.com">Lijin</a>
 * @Description TODO
 * @Date 2019/12/19 14:55
 * @Version 1.0
 **/
public class DataGeneratorUtil {


    static  final String [] data={"01","02","03","04","05","06","07","08","09","10"};
    private static  final FastDateFormat format=FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");


    /**
     * 随机产生 01 ---10 的10个数
     */

     public static String generate(){
         List<Integer> set = Lists.newArrayList();
         List<String> result=null;
         while (true){
             if (set.size() ==10){
                 break;
             }
            int  num=new Random().nextInt(10);
             if (!set.contains(num)){
                 set.add(num);
             }
         }
         result = set.stream().map(s->data[s]).collect(Collectors.toList());
         return StringUtils.collectionToCommaDelimitedString(result);
     }

    public static void main(String[] args) {
        System.out.println(generate());
    }

    public static PrizeData converStrToPrizeData(String data) {
         String [] arr=data.split(";");
        PrizeData prizeData=new PrizeData();
        prizeData.setTermNo(arr[0]);
        String prizeNums=arr[1];
        String []  nums=StringUtils.commaDelimitedListToStringArray(prizeNums);
        prizeData.setPrizeNums(nums);
        try {
            prizeData.setOpenTIme(format.parse(arr[2]));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return prizeData;
    }
}
