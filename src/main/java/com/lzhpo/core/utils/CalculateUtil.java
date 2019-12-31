package com.lzhpo.core.utils;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.C;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author <a href="lijing1@wxchina.com@wxchina.com">Lijin</a>
 * @Description TODO
 * @Date 2019/12/25 13:27
 * @Version 1.0
 **/
public class CalculateUtil {

    private static final Integer[] firsts = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private static final Integer[] seconds = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private static final Integer[] thirds = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};


    /**
     * 计算胆码num中的号码出现的情况。
     *
     * @param nums       预测的号码
     * @param occurTimes 出现的次数
     * @return
     */
    public static Set<String> calcDanMa(List<Integer> nums, List<Integer> occurTimes) {
        if (CollectionUtils.isEmpty(occurTimes)){
            return new HashSet<>();
        }
        Set<String> result = new HashSet<>();
        for (Integer first : firsts) {
            for (Integer second : seconds) {
                for (Integer third : thirds) {
                    if (first.equals(second) || first.equals(third) || second.equals(third)) {
                        continue;
                    }
                    List<Integer> temp = Lists.newArrayList(first, second, third);
                    if (checkIfFillCondition(temp, nums, occurTimes)) {
                        result.add((first==10?first:"0"+first) + "-" + (second==10?second:"0"+second)
                                + "-" + (third==10?third:"0"+third));
                    }
                }

            }
        }
        System.out.println(result.size());
        return result;
    }


    public static Set<String> calcNoCondition() {
        Set<String> result = new HashSet<>();
        for (Integer first : firsts) {
            for (Integer second : seconds) {
                for (Integer third : thirds) {
                    if (first.equals(second) || first.equals(third) || second.equals(third)) {
                        continue;
                    }
                        result.add((first==10?first:"0"+first) + "-" + (second==10?second:"0"+second)
                                + "-" + (third==10?third:"0"+third));
                }

            }
        }
        System.out.println(result.size());
        return result;
    }

    /**
     * 计算定位码出现的情况 123  456  789  0 （1,2）
     *occurTimes 0,（1,2,3）
     * @return
     */
    public static Set<String> calcDingweiMa(List<Integer> firstPredict,
                                             List<Integer> secondPredict,
                                             List<Integer> thirdPredict,
                                             List<Integer> occurTimes

    ) {
        if (CollectionUtils.isEmpty(occurTimes)){
            return new HashSet<>();
        }
        Set<String> result = new HashSet<>();
        for(Integer occur:occurTimes){
                for (Integer first : firsts) {
                    for (Integer second : seconds) {
                        for (Integer third : thirds) {
                            if (first.equals(second) || first.equals(third) || second.equals(third)) {
                                continue;
                            }
                            if (occur==0){
                                if (firstPredict.contains(first) &&  secondPredict.contains(second) &&  thirdPredict.contains(third)){
                                    result.add((first==10?first:"0"+first) + "-" + (second==10?second:"0"+second)
                                            + "-" + (third==10?third:"0"+third));
                                    continue;
                                }
                            }
                            if (occur==1){
                                if (firstPredict.contains(first) &&   !secondPredict.contains(second) &&  !thirdPredict.contains(third)){
                                    result.add((first==10?first:"0"+first) + "-" + (second==10?second:"0"+second)
                                            + "-" + (third==10?third:"0"+third));
                                    continue;
                                }
                                if (!firstPredict.contains(first) &&   secondPredict.contains(second) &&  !thirdPredict.contains(third)){
                                    result.add((first==10?first:"0"+first) + "-" + (second==10?second:"0"+second)
                                            + "-" + (third==10?third:"0"+third));

                                    continue;
                                }
                                if (!firstPredict.contains(first) &&   !secondPredict.contains(second) &&  thirdPredict.contains(third)){
                                    result.add((first==10?first:"0"+first) + "-" + (second==10?second:"0"+second)
                                            + "-" + (third==10?third:"0"+third));
                                    continue;
                                }
                            }
                            if (occur==2){
                                if (firstPredict.contains(first) &&   secondPredict.contains(second) &&  !thirdPredict.contains(third)){
                                    result.add((first==10?first:"0"+first) + "-" + (second==10?second:"0"+second)
                                            + "-" + (third==10?third:"0"+third));
                                    continue;
                                }
                                if (firstPredict.contains(first) &&   !secondPredict.contains(second) &&  thirdPredict.contains(third)){
                                    result.add((first==10?first:"0"+first) + "-" + (second==10?second:"0"+second)
                                            + "-" + (third==10?third:"0"+third));
                                    continue;
                                }
                                if (!firstPredict.contains(first) &&   secondPredict.contains(second) &&  thirdPredict.contains(third)){
                                    result.add((first==10?first:"0"+first) + "-" + (second==10?second:"0"+second)
                                            + "-" + (third==10?third:"0"+third));
                                    continue;
                                }
                            }
                            if (occur==3){
                                if (firstPredict.contains(first) &&   secondPredict.contains(second) &&  thirdPredict.contains(third)){
                                    result.add((first==10?first:"0"+first) + "-" + (second==10?second:"0"+second)
                                            + "-" + (third==10?third:"0"+third));
                                    continue;
                                }
                            }

                        }
                    }
                }
        }
        System.out.println(result.size());
        return result;
    }


    //是否满足出现次数
    private static boolean checkIfFillCondition(List<Integer> temp, List<Integer> nums, List<Integer> occurTimes) {
        temp.retainAll(nums);
        if (occurTimes.contains(temp.size())) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        List<Integer> a = Lists.newArrayList( 5);
        List<Integer> b = Lists.newArrayList(4,8);
        List<Integer> c = Lists.newArrayList();
        List<Integer> d = Lists.newArrayList(1,2,3);
         Set<String> set1=  calcDingweiMa(a, b,c,d);//164
        System.out.println(set1);
       //  Set<String> set2=  calcDanMa(Lists.newArrayList(1,3,4),Lists.newArrayList(1,2));//164

    }
    /**
     * 多个集合取交集
     */
    public static <T, C extends Collection<T>> C findIntersection(C newCollection,
                                                                  List<Collection<T>> collections) {
        boolean first = true;
        for (Collection<T> collection : collections) {
            if (first) {
                newCollection.addAll(collection);
                first = false;
            } else {
                newCollection.retainAll(collection);
            }
        }
        return newCollection;
    }


    public static List<String> findIntersectionNew(List<String> newCollection,
                                       List<List<String>> collections) {
        boolean first = true;
        for (List<String> collection : collections) {
            if (first) {
                newCollection.addAll(collection);
                first = false;
            } else {
                newCollection.retainAll(collection);
            }
        }
        if (CollectionUtils.isNotEmpty(newCollection)){
            newCollection =newCollection.stream().distinct().collect(Collectors.toList());
        }
        return newCollection;
    }

    /**
     * 将字符串分割的整数转为list
     * @param regionsPredict
     * @return
     */
    public static List<Integer> intCommonsStrToList(String regionsPredict) {
        List<Integer> list =new ArrayList<>();
        String [] arr= org.springframework.util.StringUtils.commaDelimitedListToStringArray(regionsPredict);
        for (String s : arr){
            if (StringUtils.isNotBlank(s)){
                list.add(Integer.valueOf(s));
            }
        }
        return list;
    }



}
