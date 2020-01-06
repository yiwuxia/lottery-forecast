package com.lzhpo.core.utils;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;

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
     * 质数合集，不是质数就是合数。 1 代表龙头 0 凤尾  1 表示质数 0 表示合数
     */
    private static  final  Set<Integer> zhishuList= Sets.newHashSet(1, 2, 3, 5, 7);
    // private static  final  Set<Integer> heshuList=Sets.newHashSet(4,6,8,9,10);


    public static Map<String, String> dragonHeadAndTail=new HashMap<>();
    static {
        dragonHeadAndTail.put("1:1","头质");
        dragonHeadAndTail.put("1:0","头合");
        dragonHeadAndTail.put("0:1","尾质");
        dragonHeadAndTail.put("0:0","尾合");
    }


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
                        //组合的三个数不能相同
                        if (first.equals(second) || first.equals(third) || second.equals(third)) {
                            continue;
                        }
                        if(checkFillConditionSelect(occur,first,second,third,firstPredict,secondPredict,thirdPredict)){
                            result.add((first==10?first:"0"+first) + "-" + (second==10?second:"0"+second)
                                    + "-" + (third==10?third:"0"+third));
                            continue;
                        }
                            /*//一个条件都不满足  wuxia
                            if (occur==0){
                                if (!firstPredict.contains(first) &&  !secondPredict.contains(second) &&  !thirdPredict.contains(third)){
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
                            }*/

                    }
                }
            }
        }
        System.out.println(result.size());
        return result;
    }

    /***
     * 检查定位码满足所选的几个条件
     * @param occur
     * @param first
     * @param second
     * @param third
     * @param firstPredict
     * @param secondPredict
     * @param thirdPredict
     * @return
     */
    private static boolean checkFillConditionSelect(Integer occur,
                                                    Integer first, Integer second, Integer third,
                                                    List<Integer> firstPredict,
                                                    List<Integer> secondPredict,
                                                    List<Integer> thirdPredict) {

        int i=0;
        if (firstPredict.contains(first)){
            i++;
        }
        if (secondPredict.contains(second)){
            i++;
        }
        if (thirdPredict.contains(third)){
            i++;
        }
        return occur==i;
    }


    //是否满足出现次数
    private static boolean checkIfFillCondition(List<Integer> temp, List<Integer> nums, List<Integer> occurTimes) {
        temp.retainAll(nums);
        if (occurTimes.contains(temp.size())) {
            return true;
        }
        return false;
    }

    /**
     * 计算龙头凤尾的条件
     * @param headAndTail  龙头凤尾质合
     * @param headArea
     * @param tailArea
     * @param area0
     * @param area1
     * @param area2
     * @param occurs
     * @return
     */
    public static Set<String>  calcDragon(
            String headAndTail,
            String headArea,
            String tailArea,
            String area0,
            String area1,
            String area2,
            String occurs
    ) {

        Set<String> result = new HashSet<>();
        Splitter splitter=Splitter.on(";");
        //龙头凤尾的质，合。及出现次数
        List<String> headAndTailList= splitter.splitToList(headAndTail);
        List<Integer> headAreaList= intCommonsStrToList(headArea);
        List<Integer> tailAreaList= intCommonsStrToList(tailArea);
        List<Integer> area0List= intCommonsStrToList(area0);
        List<Integer> area1List= intCommonsStrToList(area1);
        List<Integer> area2List= intCommonsStrToList(area2);
        //occursList 为空时前台阻止提交
        List<Integer> occursList= intCommonsStrToList(occurs);
        for(Integer occur:occursList) {
            for (Integer first : firsts) {
                for (Integer second : seconds) {
                    for (Integer third : thirds) {
                        //组合的三个数不能相同
                        if (first.equals(second) || first.equals(third) || second.equals(third)) {
                            continue;
                        }
                        List<Integer> eles=Lists.newArrayList(first,second,third);
                        //occur表示需要满足的条件个数
                        int fillCount=0;
                        //龙头凤尾质合满足
                        if (fillHeadAndTail(headAndTailList,eles)){
                            fillCount++;
                        }
                        //龙头满足012路条件
                        if (fillHeadArea012(headAreaList,eles)){
                            fillCount++;
                        }
                        if (fillTailArea012(tailAreaList,eles)){
                            fillCount++;
                        }
                        if (fillArea0Num(area0List,eles)){
                            fillCount++;
                        }
                        if (fillArea1Num(area1List,eles)){
                            fillCount++;
                        }
                        if (fillArea2Num(area2List,eles)){
                            fillCount++;
                        }
                        if (fillCount==occur){
                            result.add((first==10?first:"0"+first) + "-" + (second==10?second:"0"+second)
                                    + "-" + (third==10?third:"0"+third));
                        }

                    }
                }
            }
        }
        System.out.println(result.size());
        return  result;

    }

    private static boolean fillArea2Num(List<Integer> area2List, List<Integer> eles) {
        int count=0;
        for(Integer a:eles){
            if (a%3==2){
                count++;
            }
        }
        return area2List.contains(count);
    }

    private static boolean fillArea1Num(List<Integer> area1List, List<Integer> eles) {
        int count=0;
        for(Integer a:eles){
            if (a%3==1){
                count++;
            }
        }
        return area1List.contains(count);
    }

    private static boolean fillArea0Num(List<Integer> area0List, List<Integer> eles) {
        int count=0;
        for(Integer a:eles){
            if (a%3==0){
                count++;
            }
        }
        return area0List.contains(count);
    }


    private static boolean fillTailArea012(List<Integer> tailAreaList, List<Integer> eles) {
        int tail=Collections.max(eles);
        if (tailAreaList.contains(tail%3)){
            return  true;
        }
        return false;
    }

    /**
     * 龙头满足 0 1 2 路条件，1，2 表示龙头在 1 2 路都符合条件
     * @param headAreaList
     * @param eles
     * @return
     */
    private static boolean fillHeadArea012(List<Integer> headAreaList, List<Integer> eles) {
        int head=Collections.min(eles);
        if (headAreaList.contains(head%3)){
            return  true;
        }
        return false;
    }

    /**
     * 数字满足龙头凤尾质合，及条件。
     * @param headAndTailList
     * @param eles
     * @return
     */
    private static boolean fillHeadAndTail(List<String> headAndTailList,
                                           List<Integer> eles) {
        int head=Collections.min(eles);
        int tail=Collections.max(eles);

        if (headAndTailList.get(0).split(":").length<2){
            return  false;
        }
        if (headAndTailList.get(1).split(":").length<2){
            return  false;
        }
        //龙头是zhishu
        int fill=0;
        //如果龙头是质数
        if (headAndTailList.get(0).split(":")[1].equals("1")){
            if (zhishuList.contains(head)){
                fill++;
            }
        }else{
            if (!zhishuList.contains(head)){
                fill++;
            }
        }
        //如果凤尾是质数
        if (headAndTailList.get(1).split(":")[1].equals("1")){
            if (zhishuList.contains(tail)){
                fill++;
            }
        }else{
            if (!zhishuList.contains(tail)){
                fill++;
            }
        }
        List<Integer> occus=intCommonsStrToList(headAndTailList.get(2));
        return occus.contains(fill);
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
       /* List<Integer> list =new ArrayList<>();
        String [] arr= org.springframework.util.StringUtils.commaDelimitedListToStringArray(regionsPredict);
        for (String s : arr){
            if (StringUtils.isNotBlank(s)){
                list.add(Integer.valueOf(s));
            }
        }*/

        List<String> myList= Splitter.on(",")
                .omitEmptyStrings()
                .splitToList(regionsPredict);
        if (CollectionUtils.isEmpty(myList)){
            return  Lists.newArrayList();
        }
        return myList.stream().map(s->Integer.valueOf(s)).collect(Collectors.toList());
    }

    /**
     * 判断一个数是质数
     * true  质数
     * false 合数
     */

    public static boolean isPrimeNumber(Integer num){
        return  zhishuList.contains(num);
    }

    public static boolean isPrimeNumber(String num){
        return  zhishuList.contains(Integer.valueOf(num));
    }



    /**
     * 求出第几路元素第个数
     * @param numsThree
     * @param i
     * @return
     */
    public static int getAreaNums(List<Integer> numsThree, int i) {

        int count =0;
        for (Integer e: numsThree
        ) {
            if (e%3==i){
                count++;
            }
        }
        return  count;

    }
}