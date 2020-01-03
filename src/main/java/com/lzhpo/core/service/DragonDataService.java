package com.lzhpo.core.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lzhpo.common.config.MySysUser;
import com.lzhpo.core.config.RedisUtil;
import com.lzhpo.core.domain.*;
import com.lzhpo.core.domain.dragon.DragonPhoenixStaticVo;
import com.lzhpo.core.domain.dragon.DragonPhoenixVo;
import com.lzhpo.core.utils.CalculateUtil;
import com.lzhpo.core.utils.RedisConstant;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.max;
import static java.util.Comparator.comparingInt;

/**
 * @author <a href="lijing1@wxchina.com@wxchina.com">Lijin</a>
 * @Description TODO
 * @Date 2019/12/20 13:10
 * @Version 1.0
 **/
@Service
public class DragonDataService {

    @Autowired
    private PrizeDataService prizeDataService;


    /**
     * 从字符串集合中找到最小的数
     * @param newArrayList
     * @return
     */
    public Integer getMinValueFromStrLists(List<String> newArrayList) {
       List<Integer> list=  newArrayList.stream().map(s->Integer.valueOf(s)).collect(Collectors.toList());
        return Collections.min(list);
    }

    /**
     * 从字符串集合中找到最大的数
     * @param newArrayList
     * @return
     */
    public Integer getMaxValueFromStrLists(List<String> newArrayList) {
        List<Integer> list=  newArrayList.stream().map(s->Integer.valueOf(s)).collect(Collectors.toList());
        return Collections.max(list);
    }

    public List<DragonPhoenixVo> getDragonAndPhoenIndexList() {

        List<PrizeInfoEntity> remoteList = prizeDataService.queryPrizeDataLimit();
        List<DragonPhoenixVo> result=Lists.newArrayList();
        for (int i = 0; i < remoteList.size(); i++) {
            DragonPhoenixVo dragonPhoenixVo=new DragonPhoenixVo();
            PrizeInfoEntity origin=remoteList.get(i);
            dragonPhoenixVo.setId(origin.getId());
            dragonPhoenixVo.setTermNo(origin.getTermNo());
            dragonPhoenixVo.setPrizeNo01(origin.getPrizeNo01());
            dragonPhoenixVo.setPrizeNo02(origin.getPrizeNo02());
            dragonPhoenixVo.setPrizeNo03(origin.getPrizeNo03());
            //第一条数据 不用计算
            setDragonPhoenBasicData(origin,dragonPhoenixVo);
            if (result.size()>0){
                DragonPhoenixVo dragonPhoenixVoPre=result.get(result.size()-1);
                setCurRecordByPreRecord(dragonPhoenixVoPre,dragonPhoenixVo);
            }
            result.add(dragonPhoenixVo);
        }
        return  result;

    }

    private void setCurRecordByPreRecord(DragonPhoenixVo prev,
                                         DragonPhoenixVo now) {
        //如果当前值为no.且上一个也为no。则在上一个计数基础上加1
        if (now.getDragonPrime().startsWith("no")){
            if (prev.getDragonPrime().startsWith("no")){
                String value=prev.getDragonPrime().split(",")[1];
                int times=Integer.valueOf(value)+1;
                now.setDragonPrime("no,"+times);
            }
        }
        if (now.getDragonComposite().startsWith("no")){
            if (prev.getDragonComposite().startsWith("no")){
                String value=prev.getDragonComposite().split(",")[1];
                int times=Integer.valueOf(value)+1;
                now.setDragonComposite("no,"+times);
            }
        }
        if (now.getPhoenPrime().startsWith("no")){
            if (prev.getPhoenPrime().startsWith("no")){
                String value=prev.getPhoenPrime().split(",")[1];
                int times=Integer.valueOf(value)+1;
                now.setPhoenPrime("no,"+times);
            }
        }
        if (now.getPhoenComposite().startsWith("no")){
            if (prev.getPhoenComposite().startsWith("no")){
                String value=prev.getPhoenComposite().split(",")[1];
                int times=Integer.valueOf(value)+1;
                now.setPhoenComposite("no,"+times);
            }
        }
        if (now.getDragonArea0().startsWith("no")){
            if (prev.getDragonArea0().startsWith("no")){
                String value=prev.getDragonArea0().split(",")[1];
                int times=Integer.valueOf(value)+1;
                now.setDragonArea0("no,"+times);
            }
        }
        if (now.getDragonArea1().startsWith("no")){
            if (prev.getDragonArea1().startsWith("no")){
                String value=prev.getDragonArea1().split(",")[1];
                int times=Integer.valueOf(value)+1;
                now.setDragonArea1("no,"+times);
            }
        }
        if (now.getDragonArea2().startsWith("no")){
            if (prev.getDragonArea2().startsWith("no")){
                String value=prev.getDragonArea2().split(",")[1];
                int times=Integer.valueOf(value)+1;
                now.setDragonArea2("no,"+times);
            }
        }
        if (now.getPhoenArea0().startsWith("no")){
            if (prev.getPhoenArea0().startsWith("no")){
                String value=prev.getPhoenArea0().split(",")[1];
                int times=Integer.valueOf(value)+1;
                now.setPhoenArea0("no,"+times);
            }
        }
        if (now.getPhoenArea1().startsWith("no")){
            if (prev.getPhoenArea1().startsWith("no")){
                String value=prev.getPhoenArea1().split(",")[1];
                int times=Integer.valueOf(value)+1;
                now.setPhoenArea1("no,"+times);
            }
        }
        if (now.getPhoenArea2().startsWith("no")){
            if (prev.getPhoenArea2().startsWith("no")){
                String value=prev.getPhoenArea2().split(",")[1];
                int times=Integer.valueOf(value)+1;
                now.setPhoenArea2("no,"+times);
            }
        }
        if (now.getArea0Num0().startsWith("no")){
            if (prev.getArea0Num0().startsWith("no")){
                String value=prev.getArea0Num0().split(",")[1];
                int times=Integer.valueOf(value)+1;
                now.setArea0Num0("no,"+times);
            }
        }
        if (now.getArea0Num1().startsWith("no")){
            if (prev.getArea0Num1().startsWith("no")){
                String value=prev.getArea0Num1().split(",")[1];
                int times=Integer.valueOf(value)+1;
                now.setArea0Num1("no,"+times);
            }
        }
        if (now.getArea0Num2().startsWith("no")){
            if (prev.getArea0Num2().startsWith("no")){
                String value=prev.getArea0Num2().split(",")[1];
                int times=Integer.valueOf(value)+1;
                now.setArea0Num2("no,"+times);
            }
        }
        if (now.getArea0Num3().startsWith("no")){
            if (prev.getArea0Num3().startsWith("no")){
                String value=prev.getArea0Num3().split(",")[1];
                int times=Integer.valueOf(value)+1;
                now.setArea0Num3("no,"+times);
            }
        }
        if (now.getArea1Num0().startsWith("no")){
            if (prev.getArea1Num0().startsWith("no")){
                String value=prev.getArea1Num0().split(",")[1];
                int times=Integer.valueOf(value)+1;
                now.setArea1Num0("no,"+times);
            }
        }
        if (now.getArea1Num1().startsWith("no")){
            if (prev.getArea1Num1().startsWith("no")){
                String value=prev.getArea1Num1().split(",")[1];
                int times=Integer.valueOf(value)+1;
                now.setArea1Num1("no,"+times);
            }
        }
        if (now.getArea1Num2().startsWith("no")){
            if (prev.getArea1Num2().startsWith("no")){
                String value=prev.getArea1Num2().split(",")[1];
                int times=Integer.valueOf(value)+1;
                now.setArea1Num2("no,"+times);
            }
        }
        if (now.getArea1Num3().startsWith("no")){
            if (prev.getArea1Num3().startsWith("no")){
                String value=prev.getArea1Num3().split(",")[1];
                int times=Integer.valueOf(value)+1;
                now.setArea1Num3("no,"+times);
            }
        }
        if (now.getArea2Num0().startsWith("no")){
            if (prev.getArea2Num0().startsWith("no")){
                String value=prev.getArea2Num0().split(",")[1];
                int times=Integer.valueOf(value)+1;
                now.setArea2Num0("no,"+times);
            }
        }
        if (now.getArea2Num1().startsWith("no")){
            if (prev.getArea2Num1().startsWith("no")){
                String value=prev.getArea2Num1().split(",")[1];
                int times=Integer.valueOf(value)+1;
                now.setArea2Num1("no,"+times);
            }
        }
        if (now.getArea2Num2().startsWith("no")){
            if (prev.getArea2Num2().startsWith("no")){
                String value=prev.getArea2Num2().split(",")[1];
                int times=Integer.valueOf(value)+1;
                now.setArea2Num2("no,"+times);
            }
        }
        if (now.getArea2Num3().startsWith("no")){
            if (prev.getArea2Num3().startsWith("no")){
                String value=prev.getArea2Num3().split(",")[1];
                int times=Integer.valueOf(value)+1;
                now.setArea2Num3("no,"+times);
            }
        }




    }

    private void setDragonPhoenBasicData(PrizeInfoEntity origin, DragonPhoenixVo dragonPhoenixVo) {
        //龙头
        Integer head=getMinValueFromStrLists(Lists.newArrayList(origin.getPrizeNo01(),origin.getPrizeNo02(),origin.getPrizeNo03()));
        //凤尾
        Integer tail=getMaxValueFromStrLists(Lists.newArrayList(origin.getPrizeNo01(),origin.getPrizeNo02(),origin.getPrizeNo03()));
        //龙头为质数
        if (CalculateUtil.isPrimeNumber(head)){
            dragonPhoenixVo.setDragonPrime("ok,质");
            dragonPhoenixVo.setDragonComposite("no,1");
        }else {
            dragonPhoenixVo.setDragonComposite("ok,合");
            dragonPhoenixVo.setDragonPrime("no,1");
        }

        if (CalculateUtil.isPrimeNumber(tail)){
            dragonPhoenixVo.setPhoenPrime("ok,质");
            dragonPhoenixVo.setPhoenComposite("no,1");
        }else {
            dragonPhoenixVo.setPhoenComposite("ok,合");
            dragonPhoenixVo.setPhoenPrime("no,1");
        }
        //龙头在0路
        if (head%3==0){
            dragonPhoenixVo.setDragonArea0("ok,0");
            dragonPhoenixVo.setDragonArea1("no,1");
            dragonPhoenixVo.setDragonArea2("no,1");
            //龙头在1路
        }else if (head%3==1){
            dragonPhoenixVo.setDragonArea1("ok,1");
            dragonPhoenixVo.setDragonArea0("no,1");
            dragonPhoenixVo.setDragonArea2("no,1");
            //龙头在2路
        }else if (head%3==2){
            dragonPhoenixVo.setDragonArea2("ok,2");
            dragonPhoenixVo.setDragonArea0("no,1");
            dragonPhoenixVo.setDragonArea1("no,1");
        }

        //凤尾在0路
        if (tail%3==0){
            dragonPhoenixVo.setPhoenArea0("ok,0");
            dragonPhoenixVo.setPhoenArea1("no,1");
            dragonPhoenixVo.setPhoenArea2("no,1");
            //龙头在1路
        }else if (tail%3==1){
            dragonPhoenixVo.setPhoenArea1("ok,1");
            dragonPhoenixVo.setPhoenArea0("no,1");
            dragonPhoenixVo.setPhoenArea2("no,1");
            //龙头在2路
        }else if (tail%3==2){
            dragonPhoenixVo.setPhoenArea2("ok,2");
            dragonPhoenixVo.setPhoenArea0("no,1");
            dragonPhoenixVo.setPhoenArea1("no,1");
        }
        //0路个数
        List<Integer> numsThree=Lists.newArrayList(origin.getPrizeNo01(),origin.getPrizeNo02(),origin.getPrizeNo03()).stream().
                map(s->Integer.valueOf(s)).collect(Collectors.toList());
        int area0Nums=CalculateUtil.getAreaNums(numsThree,0);
        int area1Nums=CalculateUtil.getAreaNums(numsThree,1);
        int area2Nums=CalculateUtil.getAreaNums(numsThree,2);
        if (area0Nums==0){
            dragonPhoenixVo.setArea0Num0("ok,0");
            dragonPhoenixVo.setArea0Num1("no,1");
            dragonPhoenixVo.setArea0Num2("no,1");
            dragonPhoenixVo.setArea0Num3("no,1");
        }
        if (area0Nums==1){
            dragonPhoenixVo.setArea0Num0("no,1");
            dragonPhoenixVo.setArea0Num1("ok,1");
            dragonPhoenixVo.setArea0Num2("no,1");
            dragonPhoenixVo.setArea0Num3("no,1");
        }
        if (area0Nums==2){
            dragonPhoenixVo.setArea0Num0("no,1");
            dragonPhoenixVo.setArea0Num1("no,1");
            dragonPhoenixVo.setArea0Num2("ok,2");
            dragonPhoenixVo.setArea0Num3("no,1");
        }
        if (area0Nums==3){
            dragonPhoenixVo.setArea0Num0("no,1");
            dragonPhoenixVo.setArea0Num1("no,1");
            dragonPhoenixVo.setArea0Num2("no,1");
            dragonPhoenixVo.setArea0Num3("ok,3");
        }
        if (area1Nums==0){
            dragonPhoenixVo.setArea1Num0("ok,0");
            dragonPhoenixVo.setArea1Num1("no,1");
            dragonPhoenixVo.setArea1Num2("no,1");
            dragonPhoenixVo.setArea1Num3("no,1");
        }
        if (area1Nums==1){
            dragonPhoenixVo.setArea1Num0("no,1");
            dragonPhoenixVo.setArea1Num1("ok,1");
            dragonPhoenixVo.setArea1Num2("no,1");
            dragonPhoenixVo.setArea1Num3("no,1");
        }
        if (area1Nums==2){
            dragonPhoenixVo.setArea1Num0("no,1");
            dragonPhoenixVo.setArea1Num1("no,1");
            dragonPhoenixVo.setArea1Num2("ok,2");
            dragonPhoenixVo.setArea1Num3("no,1");
        }
        if (area1Nums==3){
            dragonPhoenixVo.setArea1Num0("no,1");
            dragonPhoenixVo.setArea1Num1("no,1");
            dragonPhoenixVo.setArea1Num2("no,1");
            dragonPhoenixVo.setArea1Num3("ok,3");
        }
        //2路个数
        if (area2Nums==0){
            dragonPhoenixVo.setArea2Num0("ok,0");
            dragonPhoenixVo.setArea2Num1("no,1");
            dragonPhoenixVo.setArea2Num2("no,1");
            dragonPhoenixVo.setArea2Num3("no,1");
        }
        if (area2Nums==1){
            dragonPhoenixVo.setArea2Num0("no,1");
            dragonPhoenixVo.setArea2Num1("ok,1");
            dragonPhoenixVo.setArea2Num2("no,1");
            dragonPhoenixVo.setArea2Num3("no,1");
        }
        if (area2Nums==2){
            dragonPhoenixVo.setArea2Num0("no,1");
            dragonPhoenixVo.setArea2Num1("no,1");
            dragonPhoenixVo.setArea2Num2("ok,2");
            dragonPhoenixVo.setArea2Num3("no,1");
        }
        if (area2Nums==3){
            dragonPhoenixVo.setArea2Num0("no,1");
            dragonPhoenixVo.setArea2Num1("no,1");
            dragonPhoenixVo.setArea2Num2("no,1");
            dragonPhoenixVo.setArea2Num3("ok,3");
        }
    }

    public List<DragonPhoenixStaticVo> getDragonBottomStatics(List<DragonPhoenixVo> listResult) {
        DragonPhoenixStaticVo occurs=new DragonPhoenixStaticVo();
        DragonPhoenixStaticVo maxContinu=new DragonPhoenixStaticVo();
        DragonPhoenixStaticVo maxMiss=new DragonPhoenixStaticVo();
        Set<Integer> dragonPrimeOkList= Sets.newHashSet();
        Set<Integer> dragonPrimeNoList= Sets.newHashSet();
        Integer dragonPrimeOk=0;
        Integer dragonPrimeNo=0;
        Set<Integer> dragonCompositeOkList= Sets.newHashSet();
        Set<Integer> dragonCompositeNoList= Sets.newHashSet();
        Integer dragonCompositeOk=0;
        Integer dragonCompositeNo=0;
        Set<Integer> phoenPrimeOkList= Sets.newHashSet();
        Set<Integer> phoenPrimeNoList= Sets.newHashSet();
        Integer phoenPrimeOk=0;
        Integer phoenPrimeNo=0;
        Set<Integer> phoenCompositeOkList= Sets.newHashSet();
        Set<Integer> phoenCompositeNoList= Sets.newHashSet();
        Integer phoenCompositeOk=0;
        Integer phoenCompositeNo=0;
        Set<Integer> dragonArea0OkList= Sets.newHashSet();
        Set<Integer> dragonArea0NoList= Sets.newHashSet();
        Integer dragonArea0Ok=0;
        Integer dragonArea0No=0;
        Set<Integer> dragonArea1OkList= Sets.newHashSet();
        Set<Integer> dragonArea1NoList= Sets.newHashSet();
        Integer dragonArea1Ok=0;
        Integer dragonArea1No=0;
        Set<Integer> dragonArea2OkList= Sets.newHashSet();
        Set<Integer> dragonArea2NoList= Sets.newHashSet();
        Integer dragonArea2Ok=0;
        Integer dragonArea2No=0;
        Set<Integer> phoenArea0OkList= Sets.newHashSet();
        Set<Integer> phoenArea0NoList= Sets.newHashSet();
        Integer phoenArea0Ok=0;
        Integer phoenArea0No=0;
        Set<Integer> phoenArea1OkList= Sets.newHashSet();
        Set<Integer> phoenArea1NoList= Sets.newHashSet();
        Integer phoenArea1Ok=0;
        Integer phoenArea1No=0;
        Set<Integer> phoenArea2OkList= Sets.newHashSet();
        Set<Integer> phoenArea2NoList= Sets.newHashSet();
        Integer phoenArea2Ok=0;
        Integer phoenArea2No=0;
        Set<Integer> area0Num0OkList= Sets.newHashSet();
        Set<Integer> area0Num0NoList= Sets.newHashSet();
        Integer area0Num0Ok=0;
        Integer area0Num0No=0;
        Set<Integer> area0Num1OkList= Sets.newHashSet();
        Set<Integer> area0Num1NoList= Sets.newHashSet();
        Integer area0Num1Ok=0;
        Integer area0Num1No=0;
        Set<Integer> area0Num2OkList= Sets.newHashSet();
        Set<Integer> area0Num2NoList= Sets.newHashSet();
        Integer area0Num2Ok=0;
        Integer area0Num2No=0;
        Set<Integer> area0Num3OkList= Sets.newHashSet();
        Set<Integer> area0Num3NoList= Sets.newHashSet();
        Integer area0Num3Ok=0;
        Integer area0Num3No=0;
        Set<Integer> area1Num0OkList= Sets.newHashSet();
        Set<Integer> area1Num0NoList= Sets.newHashSet();
        Integer area1Num0Ok=0;
        Integer area1Num0No=0;
        Set<Integer> area1Num1OkList= Sets.newHashSet();
        Set<Integer> area1Num1NoList= Sets.newHashSet();
        Integer area1Num1Ok=0;
        Integer area1Num1No=0;
        Set<Integer> area1Num2OkList= Sets.newHashSet();
        Set<Integer> area1Num2NoList= Sets.newHashSet();
        Integer area1Num2Ok=0;
        Integer area1Num2No=0;
        Set<Integer> area1Num3OkList= Sets.newHashSet();
        Set<Integer> area1Num3NoList= Sets.newHashSet();
        Integer area1Num3Ok=0;
        Integer area1Num3No=0;
        Set<Integer> area2Num0OkList= Sets.newHashSet();
        Set<Integer> area2Num0NoList= Sets.newHashSet();
        Integer area2Num0Ok=0;
        Integer area2Num0No=0;
        Set<Integer> area2Num1OkList= Sets.newHashSet();
        Set<Integer> area2Num1NoList= Sets.newHashSet();
        Integer area2Num1Ok=0;
        Integer area2Num1No=0;
        Set<Integer> area2Num2OkList= Sets.newHashSet();
        Set<Integer> area2Num2NoList= Sets.newHashSet();
        Integer area2Num2Ok=0;
        Integer area2Num2No=0;
        Set<Integer> area2Num3OkList= Sets.newHashSet();
        Set<Integer> area2Num3NoList= Sets.newHashSet();
        Integer area2Num3Ok=0;
        Integer area2Num3No=0;
        for(DragonPhoenixVo vo:listResult){
            if (vo.getDragonPrime().startsWith("ok")){
                if (occurs.getDragonPrime()==null){
                    occurs.setDragonPrime(1);
                }else{
                    occurs.setDragonPrime(1+occurs.getDragonPrime());
                }
                //如果是ok,将 no的计数保存
                dragonPrimeNoList.add(dragonPrimeNo);
                dragonPrimeNo=0;
                dragonPrimeOk++;
                dragonPrimeOkList.add(dragonPrimeOk);
            }else {
                if (occurs.getDragonPrime()==null){
                    occurs.setDragonPrime(0);
                }
                dragonPrimeOkList.add(dragonPrimeOk);
                dragonPrimeOk=0;
                dragonPrimeNo++;
                dragonPrimeNoList.add(dragonPrimeNo);
            }
            if (vo.getDragonComposite().startsWith("ok")){
                if (occurs.getDragonComposite()==null){
                    occurs.setDragonComposite(1);
                }else{
                    occurs.setDragonComposite(1+occurs.getDragonComposite());
                }
                //如果是ok,将 no的计数保存
                dragonCompositeNoList.add(dragonCompositeNo);
                dragonCompositeNo=0;
                dragonCompositeOk++;
                dragonCompositeOkList.add(dragonCompositeOk);
            }else {
                if (occurs.getDragonComposite()==null){
                    occurs.setDragonComposite(0);
                }
                dragonCompositeOkList.add(dragonCompositeOk);
                dragonCompositeOk=0;
                dragonCompositeNo++;
                dragonCompositeNoList.add(dragonCompositeNo);
            }
            if (vo.getPhoenPrime().startsWith("ok")){
                if (occurs.getPhoenPrime()==null){
                    occurs.setPhoenPrime(1);
                }else{
                    occurs.setPhoenPrime(1+occurs.getPhoenPrime());
                }
                //如果是ok,将 no的计数保存
                phoenPrimeNoList.add(phoenPrimeNo);
                phoenPrimeNo=0;
                phoenPrimeOk++;
                phoenPrimeOkList.add(phoenPrimeOk);
            }else {
                if (occurs.getPhoenPrime()==null){
                    occurs.setPhoenPrime(0);
                }
                phoenPrimeOkList.add(phoenPrimeOk);
                phoenPrimeOk=0;
                phoenPrimeNo++;
                phoenPrimeNoList.add(phoenPrimeNo);
            }
            if (vo.getPhoenComposite().startsWith("ok")){
                if (occurs.getPhoenComposite()==null){
                    occurs.setPhoenComposite(1);
                }else{
                    occurs.setPhoenComposite(1+occurs.getPhoenComposite());
                }
                //如果是ok,将 no的计数保存
                phoenCompositeNoList.add(phoenCompositeNo);
                phoenCompositeNo=0;
                phoenCompositeOk++;
                phoenCompositeOkList.add(phoenCompositeOk);
            }else {
                if (occurs.getPhoenComposite()==null){
                    occurs.setPhoenComposite(0);
                }
                phoenCompositeOkList.add(phoenCompositeOk);
                phoenCompositeOk=0;
                phoenCompositeNo++;
                phoenCompositeNoList.add(phoenCompositeNo);
            }
            if (vo.getDragonArea0().startsWith("ok")){
                if (occurs.getDragonArea0()==null){
                    occurs.setDragonArea0(1);
                }else{
                    occurs.setDragonArea0(1+occurs.getDragonArea0());
                }
                //如果是ok,将 no的计数保存
                dragonArea0NoList.add(dragonArea0No);
                dragonArea0No=0;
                dragonArea0Ok++;
                dragonArea0OkList.add(dragonArea0Ok);
            }else {
                if (occurs.getDragonArea0()==null){
                    occurs.setDragonArea0(0);
                }
                dragonArea0OkList.add(dragonArea0Ok);
                dragonArea0Ok=0;
                dragonArea0No++;
                dragonArea0NoList.add(dragonArea0No);
            }
            if (vo.getDragonArea1().startsWith("ok")){
                if (occurs.getDragonArea1()==null){
                    occurs.setDragonArea1(1);
                }else{
                    occurs.setDragonArea1(1+occurs.getDragonArea1());
                }
                //如果是ok,将 no的计数保存
                dragonArea1NoList.add(dragonArea1No);
                dragonArea1No=0;
                dragonArea1Ok++;
                dragonArea1OkList.add(dragonArea1Ok);
            }else {
                if (occurs.getDragonArea1()==null){
                    occurs.setDragonArea1(0);
                }
                dragonArea1OkList.add(dragonArea1Ok);
                dragonArea1Ok=0;
                dragonArea1No++;
                dragonArea1NoList.add(dragonArea1No);
            }
            if (vo.getDragonArea2().startsWith("ok")){
                if (occurs.getDragonArea2()==null){
                    occurs.setDragonArea2(1);
                }else{
                    occurs.setDragonArea2(1+occurs.getDragonArea2());
                }
                //如果是ok,将 no的计数保存
                dragonArea2NoList.add(dragonArea2No);
                dragonArea2No=0;
                dragonArea2Ok++;
                dragonArea2OkList.add(dragonArea2Ok);
            }else {
                if (occurs.getDragonArea2()==null){
                    occurs.setDragonArea2(0);
                }
                dragonArea2OkList.add(dragonArea2Ok);
                dragonArea2Ok=0;
                dragonArea2No++;
                dragonArea2NoList.add(dragonArea2No);
            }
            if (vo.getPhoenArea0().startsWith("ok")){
                if (occurs.getPhoenArea0()==null){
                    occurs.setPhoenArea0(1);
                }else{
                    occurs.setPhoenArea0(1+occurs.getPhoenArea0());
                }
                //如果是ok,将 no的计数保存
                phoenArea0NoList.add(phoenArea0No);
                phoenArea0No=0;
                phoenArea0Ok++;
                phoenArea0OkList.add(phoenArea0Ok);
            }else {
                if (occurs.getPhoenArea0()==null){
                    occurs.setPhoenArea0(0);
                }
                phoenArea0OkList.add(phoenArea0Ok);
                phoenArea0Ok=0;
                phoenArea0No++;
                phoenArea0NoList.add(phoenArea0No);
            }
            if (vo.getPhoenArea1().startsWith("ok")){
                if (occurs.getPhoenArea1()==null){
                    occurs.setPhoenArea1(1);
                }else{
                    occurs.setPhoenArea1(1+occurs.getPhoenArea1());
                }
                //如果是ok,将 no的计数保存
                phoenArea1NoList.add(phoenArea1No);
                phoenArea1No=0;
                phoenArea1Ok++;
                phoenArea1OkList.add(phoenArea1Ok);
            }else {
                if (occurs.getPhoenArea1()==null){
                    occurs.setPhoenArea1(0);
                }
                phoenArea1OkList.add(phoenArea1Ok);
                phoenArea1Ok=0;
                phoenArea1No++;
                phoenArea1NoList.add(phoenArea1No);
            }
            if (vo.getPhoenArea2().startsWith("ok")){
                if (occurs.getPhoenArea2()==null){
                    occurs.setPhoenArea2(1);
                }else{
                    occurs.setPhoenArea2(1+occurs.getPhoenArea2());
                }
                //如果是ok,将 no的计数保存
                phoenArea2NoList.add(phoenArea2No);
                phoenArea2No=0;
                phoenArea2Ok++;
                phoenArea2OkList.add(phoenArea2Ok);
            }else {
                if (occurs.getPhoenArea2()==null){
                    occurs.setPhoenArea2(0);
                }
                phoenArea2OkList.add(phoenArea2Ok);
                phoenArea2Ok=0;
                phoenArea2No++;
                phoenArea2NoList.add(phoenArea2No);
            }
            if (vo.getArea0Num0().startsWith("ok")){
                if (occurs.getArea0Num0()==null){
                    occurs.setArea0Num0(1);
                }else{
                    occurs.setArea0Num0(1+occurs.getArea0Num0());
                }
                //如果是ok,将 no的计数保存
                area0Num0NoList.add(area0Num0No);
                area0Num0No=0;
                area0Num0Ok++;
                area0Num0OkList.add(area0Num0Ok);
            }else {
                if (occurs.getArea0Num0()==null){
                    occurs.setArea0Num0(0);
                }
                area0Num0OkList.add(area0Num0Ok);
                area0Num0Ok=0;
                area0Num0No++;
                area0Num0NoList.add(area0Num0No);
            }
            if (vo.getArea0Num1().startsWith("ok")){
                if (occurs.getArea0Num1()==null){
                    occurs.setArea0Num1(1);
                }else{
                    occurs.setArea0Num1(1+occurs.getArea0Num1());
                }
                //如果是ok,将 no的计数保存
                area0Num1NoList.add(area0Num1No);
                area0Num1No=0;
                area0Num1Ok++;
                area0Num1OkList.add(area0Num1Ok);
            }else {
                if (occurs.getArea0Num1()==null){
                    occurs.setArea0Num1(0);
                }
                area0Num1OkList.add(area0Num1Ok);
                area0Num1Ok=0;
                area0Num1No++;
                area0Num1NoList.add(area0Num1No);
            }
            if (vo.getArea0Num2().startsWith("ok")){
                if (occurs.getArea0Num2()==null){
                    occurs.setArea0Num2(1);
                }else{
                    occurs.setArea0Num2(1+occurs.getArea0Num2());
                }
                //如果是ok,将 no的计数保存
                area0Num2NoList.add(area0Num2No);
                area0Num2No=0;
                area0Num2Ok++;
                area0Num2OkList.add(area0Num2Ok);
            }else {
                if (occurs.getArea0Num2()==null){
                    occurs.setArea0Num2(0);
                }
                area0Num2OkList.add(area0Num2Ok);
                area0Num2Ok=0;
                area0Num2No++;
                area0Num2NoList.add(area0Num2No);
            }
            if (vo.getArea0Num3().startsWith("ok")){
                if (occurs.getArea0Num3()==null){
                    occurs.setArea0Num3(1);
                }else{
                    occurs.setArea0Num3(1+occurs.getArea0Num3());
                }
                //如果是ok,将 no的计数保存
                area0Num3NoList.add(area0Num3No);
                area0Num3No=0;
                area0Num3Ok++;
                area0Num3OkList.add(area0Num3Ok);
            }else {
                if (occurs.getArea0Num3()==null){
                    occurs.setArea0Num3(0);
                }
                area0Num3OkList.add(area0Num3Ok);
                area0Num3Ok=0;
                area0Num3No++;
                area0Num3NoList.add(area0Num3No);
            }
            if (vo.getArea1Num0().startsWith("ok")){
                if (occurs.getArea1Num0()==null){
                    occurs.setArea1Num0(1);
                }else{
                    occurs.setArea1Num0(1+occurs.getArea1Num0());
                }
                //如果是ok,将 no的计数保存
                area1Num0NoList.add(area1Num0No);
                area1Num0No=0;
                area1Num0Ok++;
                area1Num0OkList.add(area1Num0Ok);
            }else {
                if (occurs.getArea1Num0()==null){
                    occurs.setArea1Num0(0);
                }
                area1Num0OkList.add(area1Num0Ok);
                area1Num0Ok=0;
                area1Num0No++;
                area1Num0NoList.add(area1Num0No);
            }
            if (vo.getArea1Num1().startsWith("ok")){
                if (occurs.getArea1Num1()==null){
                    occurs.setArea1Num1(1);
                }else{
                    occurs.setArea1Num1(1+occurs.getArea1Num1());
                }
                //如果是ok,将 no的计数保存
                area1Num1NoList.add(area1Num1No);
                area1Num1No=0;
                area1Num1Ok++;
                area1Num1OkList.add(area1Num1Ok);
            }else {
                if (occurs.getArea1Num1()==null){
                    occurs.setArea1Num1(0);
                }
                area1Num1OkList.add(area1Num1Ok);
                area1Num1Ok=0;
                area1Num1No++;
                area1Num1NoList.add(area1Num1No);
            }
            if (vo.getArea1Num2().startsWith("ok")){
                if (occurs.getArea1Num2()==null){
                    occurs.setArea1Num2(1);
                }else{
                    occurs.setArea1Num2(1+occurs.getArea1Num2());
                }
                //如果是ok,将 no的计数保存
                area1Num2NoList.add(area1Num2No);
                area1Num2No=0;
                area1Num2Ok++;
                area1Num2OkList.add(area1Num2Ok);
            }else {
                if (occurs.getArea1Num2()==null){
                    occurs.setArea1Num2(0);
                }
                area1Num2OkList.add(area1Num2Ok);
                area1Num2Ok=0;
                area1Num2No++;
                area1Num2NoList.add(area1Num2No);
            }
            if (vo.getArea1Num3().startsWith("ok")){
                if (occurs.getArea1Num3()==null){
                    occurs.setArea1Num3(1);
                }else{
                    occurs.setArea1Num3(1+occurs.getArea1Num3());
                }
                //如果是ok,将 no的计数保存
                area1Num3NoList.add(area1Num3No);
                area1Num3No=0;
                area1Num3Ok++;
                area1Num3OkList.add(area1Num3Ok);
            }else {
                if (occurs.getArea1Num3()==null){
                    occurs.setArea1Num3(0);
                }
                area1Num3OkList.add(area1Num3Ok);
                area1Num3Ok=0;
                area1Num3No++;
                area1Num3NoList.add(area1Num3No);
            }
            if (vo.getArea2Num0().startsWith("ok")){
                if (occurs.getArea2Num0()==null){
                    occurs.setArea2Num0(1);
                }else{
                    occurs.setArea2Num0(1+occurs.getArea2Num0());
                }
                //如果是ok,将 no的计数保存
                area2Num0NoList.add(area2Num0No);
                area2Num0No=0;
                area2Num0Ok++;
                area2Num0OkList.add(area2Num0Ok);
            }else {
                if (occurs.getArea2Num0()==null){
                    occurs.setArea2Num0(0);
                }
                area2Num0OkList.add(area2Num0Ok);
                area2Num0Ok=0;
                area2Num0No++;
                area2Num0NoList.add(area2Num0No);
            }
            if (vo.getArea2Num1().startsWith("ok")){
                if (occurs.getArea2Num1()==null){
                    occurs.setArea2Num1(1);
                }else{
                    occurs.setArea2Num1(1+occurs.getArea2Num1());
                }
                //如果是ok,将 no的计数保存
                area2Num1NoList.add(area2Num1No);
                area2Num1No=0;
                area2Num1Ok++;
                area2Num1OkList.add(area2Num1Ok);
            }else {
                if (occurs.getArea2Num1()==null){
                    occurs.setArea2Num1(0);
                }
                area2Num1OkList.add(area2Num1Ok);
                area2Num1Ok=0;
                area2Num1No++;
                area2Num1NoList.add(area2Num1No);
            }
            if (vo.getArea2Num2().startsWith("ok")){
                if (occurs.getArea2Num2()==null){
                    occurs.setArea2Num2(1);
                }else{
                    occurs.setArea2Num2(1+occurs.getArea2Num2());
                }
                //如果是ok,将 no的计数保存
                area2Num2NoList.add(area2Num2No);
                area2Num2No=0;
                area2Num2Ok++;
                area2Num2OkList.add(area2Num2Ok);
            }else {
                if (occurs.getArea2Num2()==null){
                    occurs.setArea2Num2(0);
                }
                area2Num2OkList.add(area2Num2Ok);
                area2Num2Ok=0;
                area2Num2No++;
                area2Num2NoList.add(area2Num2No);
            }
            if (vo.getArea2Num3().startsWith("ok")){
                if (occurs.getArea2Num3()==null){
                    occurs.setArea2Num3(1);
                }else{
                    occurs.setArea2Num3(1+occurs.getArea2Num3());
                }
                //如果是ok,将 no的计数保存
                area2Num3NoList.add(area2Num3No);
                area2Num3No=0;
                area2Num3Ok++;
                area2Num3OkList.add(area2Num3Ok);
            }else {
                if (occurs.getArea2Num3()==null){
                    occurs.setArea2Num3(0);
                }
                area2Num3OkList.add(area2Num3Ok);
                area2Num3Ok=0;
                area2Num3No++;
                area2Num3NoList.add(area2Num3No);
            }
        }

        maxContinu.setDragonPrime(Collections.max(dragonPrimeOkList));
        maxMiss.setDragonPrime(Collections.max(dragonPrimeNoList));
        maxContinu.setDragonComposite(Collections.max(dragonCompositeOkList));
        maxMiss.setDragonComposite(Collections.max(dragonCompositeNoList));
        maxContinu.setPhoenPrime(Collections.max(phoenPrimeOkList));
        maxMiss.setPhoenPrime(Collections.max(phoenPrimeNoList));
        maxContinu.setPhoenComposite(Collections.max(phoenCompositeOkList));
        maxMiss.setPhoenComposite(Collections.max(phoenCompositeNoList));
        maxContinu.setDragonArea0(Collections.max(dragonArea0OkList));
        maxMiss.setDragonArea0(Collections.max(dragonArea0NoList));
        maxContinu.setDragonArea1(Collections.max(dragonArea1OkList));
        maxMiss.setDragonArea1(Collections.max(dragonArea1NoList));
        maxContinu.setDragonArea2(Collections.max(dragonArea2OkList));
        maxMiss.setDragonArea2(Collections.max(dragonArea2NoList));
        maxContinu.setPhoenArea0(Collections.max(phoenArea0OkList));
        maxMiss.setPhoenArea0(Collections.max(phoenArea0NoList));
        maxContinu.setPhoenArea1(Collections.max(phoenArea1OkList));
        maxMiss.setPhoenArea1(Collections.max(phoenArea1NoList));
        maxContinu.setPhoenArea2(Collections.max(phoenArea2OkList));
        maxMiss.setPhoenArea2(Collections.max(phoenArea2NoList));
        maxContinu.setArea0Num0(Collections.max(area0Num0OkList));
        maxMiss.setArea0Num0(Collections.max(area0Num0NoList));
        maxContinu.setArea0Num1(Collections.max(area0Num1OkList));
        maxMiss.setArea0Num1(Collections.max(area0Num1NoList));
        maxContinu.setArea0Num2(Collections.max(area0Num2OkList));
        maxMiss.setArea0Num2(Collections.max(area0Num2NoList));
        maxContinu.setArea0Num3(Collections.max(area0Num3OkList));
        maxMiss.setArea0Num3(Collections.max(area0Num3NoList));
        maxContinu.setArea1Num0(Collections.max(area1Num0OkList));
        maxMiss.setArea1Num0(Collections.max(area1Num0NoList));
        maxContinu.setArea1Num1(Collections.max(area1Num1OkList));
        maxMiss.setArea1Num1(Collections.max(area1Num1NoList));
        maxContinu.setArea1Num2(Collections.max(area1Num2OkList));
        maxMiss.setArea1Num2(Collections.max(area1Num2NoList));
        maxContinu.setArea1Num3(Collections.max(area1Num3OkList));
        maxMiss.setArea1Num3(Collections.max(area1Num3NoList));
        maxContinu.setArea2Num0(Collections.max(area2Num0OkList));
        maxMiss.setArea2Num0(Collections.max(area2Num0NoList));
        maxContinu.setArea2Num1(Collections.max(area2Num1OkList));
        maxMiss.setArea2Num1(Collections.max(area2Num1NoList));
        maxContinu.setArea2Num2(Collections.max(area2Num2OkList));
        maxMiss.setArea2Num2(Collections.max(area2Num2NoList));
        maxContinu.setArea2Num3(Collections.max(area2Num3OkList));
        maxMiss.setArea2Num3(Collections.max(area2Num3NoList));
        List<DragonPhoenixStaticVo> statics=new ArrayList<>();
        occurs.setDescription("出现次数");
        maxContinu.setDescription("最大连出");
        maxMiss.setDescription("最大遗漏");
        statics.add(occurs);
        statics.add(maxContinu);
        statics.add(maxMiss);
        return  statics;
    }
}
