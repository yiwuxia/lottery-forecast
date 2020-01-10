package com.lzhpo.core.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lzhpo.core.config.RedisUtil;
import com.lzhpo.core.domain.PrizeInfoEntity;
import com.lzhpo.core.domain.concord.SumDataStaticsVo;
import com.lzhpo.core.domain.concord.SumDataVo;
import com.lzhpo.core.utils.MyStrUtil;
import com.lzhpo.core.utils.RedisConstant;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author <a href="lijing1@wxchina.com@wxchina.com">Lijin</a>
 * @Description TODO
 * @Date 2019/12/20 13:10
 * @Version 1.0
 **/
@Service
public class SumValueDataService {


    @Autowired
    private PrizeDataService prizeDataService;

    @Autowired
    private RedisUtil redisUtil;


    public List<SumDataVo> getSumValueIndexList() {
        List<PrizeInfoEntity> originData = prizeDataService.queryPrizeDataLimit();
        List<SumDataVo> result=Lists.newArrayList();
        for (int i = 0; i < originData.size(); i++) {
            PrizeInfoEntity entity=originData.get(i);
            SumDataVo dataVo=new SumDataVo();
            dataVo.setId(entity.getId());
            dataVo.setTermNo(entity.getTermNo());
            dataVo.setPrizeNo01(entity.getPrizeNo01());
            dataVo.setPrizeNo02(entity.getPrizeNo02());
            dataVo.setPrizeNo03(entity.getPrizeNo03());
            //当期的合值
            Integer sum= Lists.newArrayList(entity.getPrizeNo01(),entity.getPrizeNo02(),entity.getPrizeNo03())
                    .stream().map(s->Integer.valueOf(s)).reduce(Integer::sum).orElse(0);
            //
            int ge  = sum%10;
            //第一条
            String [] rightValueArr= MyStrUtil.getInitSumValueArr(10);
            rightValueArr[ge]="ok,"+ge;
            dataVo.setRightValueArr(rightValueArr);
            if (result.size()==0){
                dataVo.setBreak1("ok,断");
                dataVo.setBreak2("ok,断");
                dataVo.setLeftPass("no,1");
                dataVo.setRightPass("no,1");
                dataVo.setFall("no,1");
            }else{
                SumDataVo dataVoPre=result.get(result.size()-1);
                String[] rightValueArrPre=dataVoPre.getRightValueArr();
                String [] rightValueArrNow=dataVo.getRightValueArr();
                for (int j =0 ; j < rightValueArrPre.length;j++){
                    if ("no".equals(rightValueArrPre[j].split(",")[0])){
                        //与上期一样出现no，当期的no +1
                        if ("no".equals(rightValueArrNow[j].split(",")[0])){
                            rightValueArrNow[j]="no,"+(Integer.valueOf(rightValueArrPre[j]
                                    .split(",")[1])+1);
                        }
                    }
                }
                //上一期的合值
                int preSum=Lists.newArrayList(dataVoPre.getPrizeNo01(),dataVoPre.getPrizeNo02(),dataVoPre.getPrizeNo03())
                        .stream().map(s->Integer.valueOf(s)).reduce(Integer::sum).orElse(0);
                dataVo.setRightValueArr(rightValueArrNow);
                if (sum==preSum){
                    dataVo.setFall("ok,落");
                    dataVo.setBreak2("no,1");
                }else {
                    dataVo.setFall("no,1");
                    dataVo.setBreak2("ok,断");
                }
                if (sum==(preSum-1)){
                    dataVo.setBreak1("no,1");
                    dataVo.setLeftPass("ok,左");
                    dataVo.setRightPass("no,1");
                }else  if ((sum+1)==preSum){
                    dataVo.setBreak1("no,1");
                    dataVo.setLeftPass("no,1");
                    dataVo.setRightPass("ok,右");
                }else{
                    dataVo.setBreak1("ok,断");
                    dataVo.setLeftPass("no,1");
                    dataVo.setRightPass("no,1");
                }
                if ("no".equals(dataVoPre.getLeftPass().split(",")[0])){
                    if ("no".equals(dataVo.getLeftPass().split(",")[0])){
                        dataVo.setLeftPass(
                                "no,"+(Integer.valueOf(dataVoPre.getLeftPass().split(",")[1])+1)
                        );
                    }
                }
                if ("no".equals(dataVoPre.getBreak1().split(",")[0])){
                    if ("no".equals(dataVo.getBreak1().split(",")[0])){
                        dataVo.setBreak1(
                                "no,"+(Integer.valueOf(dataVoPre.getBreak1().split(",")[1])+1)
                        );
                    }
                }
                if ("no".equals(dataVoPre.getRightPass().split(",")[0])){
                    if ("no".equals(dataVo.getRightPass().split(",")[0])){
                        dataVo.setRightPass(
                                "no,"+(Integer.valueOf(dataVoPre.getRightPass().split(",")[1])+1)
                        );
                    }
                }
                if ("no".equals(dataVoPre.getBreak2().split(",")[0])){
                    if ("no".equals(dataVo.getBreak2().split(",")[0])){
                        dataVo.setBreak2(
                                "no,"+(Integer.valueOf(dataVoPre.getBreak2().split(",")[1])+1)
                        );
                    }
                }
                if ("no".equals(dataVoPre.getFall().split(",")[0])){
                    if ("no".equals(dataVo.getFall().split(",")[0])){
                        dataVo.setFall(
                                "no,"+(Integer.valueOf(dataVoPre.getFall().split(",")[1])+1)
                        );
                    }
                }

            }
            result.add(dataVo);

        }
        return result;
    }


    public List<SumDataStaticsVo> getSumValueBottomStatics(List<SumDataVo> listResult) {
        //出现次数，最大连出，最大遗漏
        SumDataStaticsVo occurs=new SumDataStaticsVo();
        SumDataStaticsVo maxContinu=new SumDataStaticsVo();
        SumDataStaticsVo maxMiss=new SumDataStaticsVo();
        Set<Integer> rightValue0OkList= Sets.newHashSet();
        Set<Integer> rightValue0NoList= Sets.newHashSet();
        Integer rightValue0Ok=0;
        Integer rightValue0No=0;
        Set<Integer> rightValue1OkList= Sets.newHashSet();
        Set<Integer> rightValue1NoList= Sets.newHashSet();
        Integer rightValue1Ok=0;
        Integer rightValue1No=0;
        Set<Integer> rightValue2OkList= Sets.newHashSet();
        Set<Integer> rightValue2NoList= Sets.newHashSet();
        Integer rightValue2Ok=0;
        Integer rightValue2No=0;
        Set<Integer> rightValue3OkList= Sets.newHashSet();
        Set<Integer> rightValue3NoList= Sets.newHashSet();
        Integer rightValue3Ok=0;
        Integer rightValue3No=0;
        Set<Integer> rightValue4OkList= Sets.newHashSet();
        Set<Integer> rightValue4NoList= Sets.newHashSet();
        Integer rightValue4Ok=0;
        Integer rightValue4No=0;
        Set<Integer> rightValue5OkList= Sets.newHashSet();
        Set<Integer> rightValue5NoList= Sets.newHashSet();
        Integer rightValue5Ok=0;
        Integer rightValue5No=0;
        Set<Integer> rightValue6OkList= Sets.newHashSet();
        Set<Integer> rightValue6NoList= Sets.newHashSet();
        Integer rightValue6Ok=0;
        Integer rightValue6No=0;
        Set<Integer> rightValue7OkList= Sets.newHashSet();
        Set<Integer> rightValue7NoList= Sets.newHashSet();
        Integer rightValue7Ok=0;
        Integer rightValue7No=0;
        Set<Integer> rightValue8OkList= Sets.newHashSet();
        Set<Integer> rightValue8NoList= Sets.newHashSet();
        Integer rightValue8Ok=0;
        Integer rightValue8No=0;
        Set<Integer> rightValue9OkList= Sets.newHashSet();
        Set<Integer> rightValue9NoList= Sets.newHashSet();
        Integer rightValue9Ok=0;
        Integer rightValue9No=0;
        Set<Integer> leftPassOkList= Sets.newHashSet();
        Set<Integer> leftPassNoList= Sets.newHashSet();
        Integer leftPassOk=0;
        Integer leftPassNo=0;
        Set<Integer> break1OkList= Sets.newHashSet();
        Set<Integer> break1NoList= Sets.newHashSet();
        Integer break1Ok=0;
        Integer break1No=0;
        Set<Integer> rightPassOkList= Sets.newHashSet();
        Set<Integer> rightPassNoList= Sets.newHashSet();
        Integer rightPassOk=0;
        Integer rightPassNo=0;
        Set<Integer> break2OkList= Sets.newHashSet();
        Set<Integer> break2NoList= Sets.newHashSet();
        Integer break2Ok=0;
        Integer break2No=0;
        Set<Integer> fallOkList= Sets.newHashSet();
        Set<Integer> fallNoList= Sets.newHashSet();
        Integer fallOk=0;
        Integer fallNo=0;

        for(SumDataVo vo:listResult){

            if (vo.getRightValueArr()[0].startsWith("ok")){
                if (occurs.getRightValue0()==null){
                    occurs.setRightValue0(1);
                }else{
                    occurs.setRightValue0(1+occurs.getRightValue0());
                }
                //如果是ok,将 no的计数保存
                rightValue0NoList.add(rightValue0No);
                rightValue0No=0;
                rightValue0Ok++;
                rightValue0OkList.add(rightValue0Ok);
            }else {
                if (occurs.getRightValue0()==null){
                    occurs.setRightValue0(0);
                }
                rightValue0OkList.add(rightValue0Ok);
                rightValue0Ok=0;
                rightValue0No++;
                rightValue0NoList.add(rightValue0No);
            }
            if (vo.getRightValueArr()[1].startsWith("ok")){
                if (occurs.getRightValue1()==null){
                    occurs.setRightValue1(1);
                }else{
                    occurs.setRightValue1(1+occurs.getRightValue1());
                }
                //如果是ok,将 no的计数保存
                rightValue1NoList.add(rightValue1No);
                rightValue1No=0;
                rightValue1Ok++;
                rightValue1OkList.add(rightValue1Ok);
            }else {
                if (occurs.getRightValue1()==null){
                    occurs.setRightValue1(0);
                }
                rightValue1OkList.add(rightValue1Ok);
                rightValue1Ok=0;
                rightValue1No++;
                rightValue1NoList.add(rightValue1No);
            }
            if (vo.getRightValueArr()[2].startsWith("ok")){
                if (occurs.getRightValue2()==null){
                    occurs.setRightValue2(1);
                }else{
                    occurs.setRightValue2(1+occurs.getRightValue2());
                }
                //如果是ok,将 no的计数保存
                rightValue2NoList.add(rightValue2No);
                rightValue2No=0;
                rightValue2Ok++;
                rightValue2OkList.add(rightValue2Ok);
            }else {
                if (occurs.getRightValue2()==null){
                    occurs.setRightValue2(0);
                }
                rightValue2OkList.add(rightValue2Ok);
                rightValue2Ok=0;
                rightValue2No++;
                rightValue2NoList.add(rightValue2No);
            }
            if (vo.getRightValueArr()[3].startsWith("ok")){
                if (occurs.getRightValue3()==null){
                    occurs.setRightValue3(1);
                }else{
                    occurs.setRightValue3(1+occurs.getRightValue3());
                }
                //如果是ok,将 no的计数保存
                rightValue3NoList.add(rightValue3No);
                rightValue3No=0;
                rightValue3Ok++;
                rightValue3OkList.add(rightValue3Ok);
            }else {
                if (occurs.getRightValue3()==null){
                    occurs.setRightValue3(0);
                }
                rightValue3OkList.add(rightValue3Ok);
                rightValue3Ok=0;
                rightValue3No++;
                rightValue3NoList.add(rightValue3No);
            }
            if (vo.getRightValueArr()[4].startsWith("ok")){
                if (occurs.getRightValue4()==null){
                    occurs.setRightValue4(1);
                }else{
                    occurs.setRightValue4(1+occurs.getRightValue4());
                }
                //如果是ok,将 no的计数保存
                rightValue4NoList.add(rightValue4No);
                rightValue4No=0;
                rightValue4Ok++;
                rightValue4OkList.add(rightValue4Ok);
            }else {
                if (occurs.getRightValue4()==null){
                    occurs.setRightValue4(0);
                }
                rightValue4OkList.add(rightValue4Ok);
                rightValue4Ok=0;
                rightValue4No++;
                rightValue4NoList.add(rightValue4No);
            }
            if (vo.getRightValueArr()[5].startsWith("ok")){
                if (occurs.getRightValue5()==null){
                    occurs.setRightValue5(1);
                }else{
                    occurs.setRightValue5(1+occurs.getRightValue5());
                }
                //如果是ok,将 no的计数保存
                rightValue5NoList.add(rightValue5No);
                rightValue5No=0;
                rightValue5Ok++;
                rightValue5OkList.add(rightValue5Ok);
            }else {
                if (occurs.getRightValue5()==null){
                    occurs.setRightValue5(0);
                }
                rightValue5OkList.add(rightValue5Ok);
                rightValue5Ok=0;
                rightValue5No++;
                rightValue5NoList.add(rightValue5No);
            }
            if (vo.getRightValueArr()[6].startsWith("ok")){
                if (occurs.getRightValue6()==null){
                    occurs.setRightValue6(1);
                }else{
                    occurs.setRightValue6(1+occurs.getRightValue6());
                }
                //如果是ok,将 no的计数保存
                rightValue6NoList.add(rightValue6No);
                rightValue6No=0;
                rightValue6Ok++;
                rightValue6OkList.add(rightValue6Ok);
            }else {
                if (occurs.getRightValue6()==null){
                    occurs.setRightValue6(0);
                }
                rightValue6OkList.add(rightValue6Ok);
                rightValue6Ok=0;
                rightValue6No++;
                rightValue6NoList.add(rightValue6No);
            }
            if (vo.getRightValueArr()[7].startsWith("ok")){
                if (occurs.getRightValue7()==null){
                    occurs.setRightValue7(1);
                }else{
                    occurs.setRightValue7(1+occurs.getRightValue7());
                }
                //如果是ok,将 no的计数保存
                rightValue7NoList.add(rightValue7No);
                rightValue7No=0;
                rightValue7Ok++;
                rightValue7OkList.add(rightValue7Ok);
            }else {
                if (occurs.getRightValue7()==null){
                    occurs.setRightValue7(0);
                }
                rightValue7OkList.add(rightValue7Ok);
                rightValue7Ok=0;
                rightValue7No++;
                rightValue7NoList.add(rightValue7No);
            }
            if (vo.getRightValueArr()[8].startsWith("ok")){
                if (occurs.getRightValue8()==null){
                    occurs.setRightValue8(1);
                }else{
                    occurs.setRightValue8(1+occurs.getRightValue8());
                }
                //如果是ok,将 no的计数保存
                rightValue8NoList.add(rightValue8No);
                rightValue8No=0;
                rightValue8Ok++;
                rightValue8OkList.add(rightValue8Ok);
            }else {
                if (occurs.getRightValue8()==null){
                    occurs.setRightValue8(0);
                }
                rightValue8OkList.add(rightValue8Ok);
                rightValue8Ok=0;
                rightValue8No++;
                rightValue8NoList.add(rightValue8No);
            }
            if (vo.getRightValueArr()[9].startsWith("ok")){
                if (occurs.getRightValue9()==null){
                    occurs.setRightValue9(1);
                }else{
                    occurs.setRightValue9(1+occurs.getRightValue9());
                }
                //如果是ok,将 no的计数保存
                rightValue9NoList.add(rightValue9No);
                rightValue9No=0;
                rightValue9Ok++;
                rightValue9OkList.add(rightValue9Ok);
            }else {
                if (occurs.getRightValue9()==null){
                    occurs.setRightValue9(0);
                }
                rightValue9OkList.add(rightValue9Ok);
                rightValue9Ok=0;
                rightValue9No++;
                rightValue9NoList.add(rightValue9No);
            }


            if (vo.getLeftPass().startsWith("ok")){
                if (occurs.getLeftPass()==null){
                    occurs.setLeftPass(1);
                }else{
                    occurs.setLeftPass(1+occurs.getLeftPass());
                }
                //如果是ok,将 no的计数保存
                leftPassNoList.add(leftPassNo);
                leftPassNo=0;
                leftPassOk++;
                leftPassOkList.add(leftPassOk);
            }else {
                if (occurs.getLeftPass()==null){
                    occurs.setLeftPass(0);
                }
                leftPassOkList.add(leftPassOk);
                leftPassOk=0;
                leftPassNo++;
                leftPassNoList.add(leftPassNo);
            }
            if (vo.getBreak1().startsWith("ok")){
                if (occurs.getBreak1()==null){
                    occurs.setBreak1(1);
                }else{
                    occurs.setBreak1(1+occurs.getBreak1());
                }
                //如果是ok,将 no的计数保存
                break1NoList.add(break1No);
                break1No=0;
                break1Ok++;
                break1OkList.add(break1Ok);
            }else {
                if (occurs.getBreak1()==null){
                    occurs.setBreak1(0);
                }
                break1OkList.add(break1Ok);
                break1Ok=0;
                break1No++;
                break1NoList.add(break1No);
            }
            if (vo.getRightPass().startsWith("ok")){
                if (occurs.getRightPass()==null){
                    occurs.setRightPass(1);
                }else{
                    occurs.setRightPass(1+occurs.getRightPass());
                }
                //如果是ok,将 no的计数保存
                rightPassNoList.add(rightPassNo);
                rightPassNo=0;
                rightPassOk++;
                rightPassOkList.add(rightPassOk);
            }else {
                if (occurs.getRightPass()==null){
                    occurs.setRightPass(0);
                }
                rightPassOkList.add(rightPassOk);
                rightPassOk=0;
                rightPassNo++;
                rightPassNoList.add(rightPassNo);
            }
            if (vo.getBreak2().startsWith("ok")){
                if (occurs.getBreak2()==null){
                    occurs.setBreak2(1);
                }else{
                    occurs.setBreak2(1+occurs.getBreak2());
                }
                //如果是ok,将 no的计数保存
                break2NoList.add(break2No);
                break2No=0;
                break2Ok++;
                break2OkList.add(break2Ok);
            }else {
                if (occurs.getBreak2()==null){
                    occurs.setBreak2(0);
                }
                break2OkList.add(break2Ok);
                break2Ok=0;
                break2No++;
                break2NoList.add(break2No);
            }
            if (vo.getFall().startsWith("ok")){
                if (occurs.getFall()==null){
                    occurs.setFall(1);
                }else{
                    occurs.setFall(1+occurs.getFall());
                }
                //如果是ok,将 no的计数保存
                fallNoList.add(fallNo);
                fallNo=0;
                fallOk++;
                fallOkList.add(fallOk);
            }else {
                if (occurs.getFall()==null){
                    occurs.setFall(0);
                }
                fallOkList.add(fallOk);
                fallOk=0;
                fallNo++;
                fallNoList.add(fallNo);
            }
        }

        maxContinu.setRightValue0(Collections.max(rightValue0OkList));
        maxMiss.setRightValue0(Collections.max(rightValue0NoList));
        maxContinu.setRightValue1(Collections.max(rightValue1OkList));
        maxMiss.setRightValue1(Collections.max(rightValue1NoList));
        maxContinu.setRightValue2(Collections.max(rightValue2OkList));
        maxMiss.setRightValue2(Collections.max(rightValue2NoList));
        maxContinu.setRightValue3(Collections.max(rightValue3OkList));
        maxMiss.setRightValue3(Collections.max(rightValue3NoList));
        maxContinu.setRightValue4(Collections.max(rightValue4OkList));
        maxMiss.setRightValue4(Collections.max(rightValue4NoList));
        maxContinu.setRightValue5(Collections.max(rightValue5OkList));
        maxMiss.setRightValue5(Collections.max(rightValue5NoList));
        maxContinu.setRightValue6(Collections.max(rightValue6OkList));
        maxMiss.setRightValue6(Collections.max(rightValue6NoList));
        maxContinu.setRightValue7(Collections.max(rightValue7OkList));
        maxMiss.setRightValue7(Collections.max(rightValue7NoList));
        maxContinu.setRightValue8(Collections.max(rightValue8OkList));
        maxMiss.setRightValue8(Collections.max(rightValue8NoList));
        maxContinu.setRightValue9(Collections.max(rightValue9OkList));
        maxMiss.setRightValue9(Collections.max(rightValue9NoList));
        maxContinu.setLeftPass(Collections.max(leftPassOkList));
        maxMiss.setLeftPass(Collections.max(leftPassNoList));
        maxContinu.setBreak1(Collections.max(break1OkList));
        maxMiss.setBreak1(Collections.max(break1NoList));
        maxContinu.setRightPass(Collections.max(rightPassOkList));
        maxMiss.setRightPass(Collections.max(rightPassNoList));
        maxContinu.setBreak2(Collections.max(break2OkList));
        maxMiss.setBreak2(Collections.max(break2NoList));
        maxContinu.setFall(Collections.max(fallOkList));
        maxMiss.setFall(Collections.max(fallNoList));
        List<SumDataStaticsVo> lastResult=Lists.newArrayList();
        occurs.setDescription("出现次数");
        lastResult.add(occurs);
        maxContinu.setDescription("最大连出");
        lastResult.add(maxContinu);
        maxMiss.setDescription("最大遗漏");
        lastResult.add(maxMiss);
        return lastResult;
    }


    /**
     * 将最新一期数据的合值保存到redis
     * 生成数据时也保存到redis
     */

    @PostConstruct
    private void saveNewestPrizeDataToRedis(){
        List<PrizeInfoEntity> originData = prizeDataService.queryPrizeDataLimitOne();
        if (CollectionUtils.isNotEmpty(originData)){
            PrizeInfoEntity entity=originData.get(0);
            int sumValue= Lists.newArrayList(entity.getPrizeNo01(),entity.getPrizeNo02(),entity.getPrizeNo03())
                    .stream().map(s->Integer.valueOf(s)).reduce(Integer::sum).orElse(0);
            sumValue=sumValue%10;
            saveNewestPrizeSumValue(sumValue);
        }
    }

    public void  saveNewestPrizeSumValue(int sumValue){
        redisUtil.set(RedisConstant.NEWST_PRIZE_DATA_SUM_VALUE,String.valueOf(sumValue));
    }

}
