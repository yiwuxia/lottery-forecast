package com.lzhpo.core.utils;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        Set<String> result = new HashSet<>();
        for (Integer first : firsts) {
            for (Integer second : seconds) {
                for (Integer third : thirds) {
                    if (first.equals(second) || first.equals(third) || second.equals(third)) {
                        continue;
                    }
                    List<Integer> temp = Lists.newArrayList(first, second, third);
                    if (checkIfFillCondition(temp, nums, occurTimes)) {
                        result.add(first + "-" + second + "-" + third);
                    }
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
        Set<String> result = new HashSet<>();
        for(Integer occur:occurTimes){
                for (Integer first : firsts) {
                    for (Integer second : seconds) {
                        for (Integer third : thirds) {
                            if (first.equals(second) || first.equals(third) || second.equals(third)) {
                                continue;
                            }
                            if (occur==0){
                                if (firstPredict.contains(first) ||  secondPredict.contains(second) ||  thirdPredict.contains(third)){
                                    continue;
                                }else {
                                    result.add(first + "-" + second + "-" + third);
                                    continue;
                                }
                            }
                            if (occur==1){
                                if (firstPredict.contains(first) &&   !secondPredict.contains(second) &&  !thirdPredict.contains(third)){
                                    result.add(first + "-" + second + "-" + third);
                                    continue;
                                }
                                if (!firstPredict.contains(first) &&   secondPredict.contains(second) &&  !thirdPredict.contains(third)){
                                    result.add(first + "-" + second + "-" + third);

                                    continue;
                                }
                                if (!firstPredict.contains(first) &&   !secondPredict.contains(second) &&  thirdPredict.contains(third)){
                                    result.add(first + "-" + second + "-" + third);
                                    continue;
                                }
                            }
                            if (occur==2){
                                if (firstPredict.contains(first) &&   secondPredict.contains(second) &&  !thirdPredict.contains(third)){
                                    result.add(first + "-" + second + "-" + third);
                                    continue;
                                }
                                if (firstPredict.contains(first) &&   !secondPredict.contains(second) &&  thirdPredict.contains(third)){
                                    result.add(first + "-" + second + "-" + third);
                                    continue;
                                }
                                if (!firstPredict.contains(first) &&   secondPredict.contains(second) &&  thirdPredict.contains(third)){
                                    result.add(first + "-" + second + "-" + third);
                                    continue;
                                }
                            }
                            if (occur==3){
                                if (firstPredict.contains(first) &&   secondPredict.contains(second) &&  thirdPredict.contains(third)){
                                    result.add(first + "-" + second + "-" + third);
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
        List<Integer> a = Lists.newArrayList(2,3, 4, 5);
        List<Integer> b = Lists.newArrayList(6,7,8,9);
        List<Integer> c = Lists.newArrayList(1,  3);
        List<Integer> d = Lists.newArrayList(2);
        calcDingweiMa(a, b,c,d);
    }

}
