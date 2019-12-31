package com.lzhpo.core.service;

import com.google.common.collect.Lists;
import com.lzhpo.admin.entity.vo.SelectCondition;
import com.lzhpo.common.config.MySysUser;
import com.lzhpo.core.config.RedisUtil;
import com.lzhpo.core.domain.*;
import com.lzhpo.core.utils.CalculateUtil;
import com.lzhpo.core.utils.RedisConstant;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import  static  java.util.Collections.max;
import  static  java.util.Collections.min;


import static java.util.Comparator.comparingInt;

/**
 * @author <a href="lijing1@wxchina.com@wxchina.com">Lijin</a>
 * @Description TODO
 * @Date 2019/12/20 13:10
 * @Version 1.0
 **/
@Service
public class PrizeDataService {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RedisUtil redisUtil;



    /**
     * 基本走势
     * 先获取最新的一条数据
     *
     * @param prizeData
     */
    public void handlerOriginDataTrend(PrizeData prizeData) {
        //保存到数据库
        String sql = " insert into t_prize_base_info(term_no,prize_no1,prize_no2,prize_no3,prize_no4,prize_no5,prize_no6,prize_no7," +
                " prize_no8,prize_no9,prize_no10,open_time" +
                " )values(?,?,?,?,?,?,?,?,?,?,?,?)";
        String[] nums = prizeData.getPrizeNums();
        Object[] arr = new Object[]{
                prizeData.getTermNo(), nums[0], nums[1], nums[2], nums[3], nums[4], nums[5], nums[6], nums[7],
                nums[8], nums[9], prizeData.getOpenTIme()
        };
        jdbcTemplate.update(sql, arr);

    }


    public List<PrizeInfoEntity> queryPrizeDataLimit() {

        String sql = "select id,term_no termNo,prize_no1 prizeNo01,prize_no2 prizeNo02,prize_no3 prizeNo03," +
                "prize_no4 prizeNo04," +
                "prize_no5 prizeNo05,prize_no6 prizeNo06,prize_no7 prizeNo07," +
                "prize_no8 prizeNo08,prize_no9 prizeNo09,prize_no10 prizeNo10,open_time openTime from  t_prize_base_info order by id desc limit 500";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(PrizeInfoEntity.class));

    }


    public List<PrizeVo> getPrizeVoList() {
        //原始数据(接口返回的数据)
        List<PrizeInfoEntity> remoteList = queryPrizeDataLimit();
        remoteList.sort(comparingInt(PrizeInfoEntity::getId));
        List<PrizeVo> voList = new ArrayList<>(remoteList.size());
        for (int i = 0; i < remoteList.size(); i++) {
            PrizeInfoEntity entity = remoteList.get(i);
            PrizeVo vo = new PrizeVo();
            vo.setId(entity.getId());
            vo.setTermNo(entity.getTermNo());
            //前3开奖号码
            String[] prizeNumArr = {entity.getPrizeNo01(), entity.getPrizeNo02(), entity.getPrizeNo03()};
            vo.setPrizeNums(prizeNumArr);
            //第一条数据
            if (voList.size() == 0) {
                staticFirstRecord(vo, entity);
            } else {
                //获取上一条统计记录
                PrizeVo preVo = voList.get(voList.size() - 1);
                //设置了必要值，需要统计间隔
                staticFirstRecord(vo, entity);
                String[] region = vo.getRegion();
                String[] first = vo.getFirst();
                String[] second = vo.getSecond();
                String[] third = vo.getThird();
                String[] pre_region = preVo.getRegion();
                String[] pre_first = preVo.getFirst();
                String[] pre_second = preVo.getSecond();
                String[] pre_third = preVo.getThird();
                //每条数据和上一条比较，如果该位置为no。根据上一条同位置是否为no.来计算计数值。
                arrValueStatics(region, pre_region);
                arrValueStatics(first, pre_first);
                arrValueStatics(second, pre_second);
                arrValueStatics(third, pre_third);
            }
            voList.add(vo);
        }

        return voList;
    }

    private void arrValueStatics(String[] region, String[] pre_region) {

        for (int j = 0; j < region.length; j++) {
            //如果当前位置为no;
            if (region[j].startsWith("no")) {
                //如果上一条对应位置为no。值+1；否则为0
                if (pre_region[j].startsWith("no")) {
                    int temp = Integer.valueOf(pre_region[j].split(",")[1]) + 1;
                    region[j] = "no," + temp;
                } else {
                    region[j] = "no,1";
                }
            }
        }


    }

    //判断该位置是否显示了对应的数字
    private void staticFirstRecord(PrizeVo vo, PrizeInfoEntity entity) {
        int num1 = Integer.valueOf(entity.getPrizeNo01());
        int num2 = Integer.valueOf(entity.getPrizeNo02());
        int num3 = Integer.valueOf(entity.getPrizeNo03());
        String[] region = vo.getRegion();
        List<String[]> list2 = Lists.newArrayList(vo.getFirst(), vo.getSecond(), vo.getThird());
        List<Integer> list3 = Lists.newArrayList(num1, num2, num3);
        for (int j = 0; j < region.length; j++) {
            if ((j + 1) == num1) {
                region[j] = "ok," + num1;
            } else if ((j + 1) == num2) {
                region[j] = "ok," + num2;
            } else if ((j + 1) == num3) {
                region[j] = "ok," + num3;
            } else {
                region[j] = "no," + 1;
            }
        }
        for (int n = 0; n < list2.size(); n++) {
            String[] arr = list2.get(n);
            int locationNum = list3.get(n);
            for (int j = 0; j < arr.length; j++) {
                if ((j + 1) == locationNum) {
                    arr[j] = "ok," + locationNum;
                } else {
                    arr[j] = "no," + 1;
                }
            }

        }
    }

    public List<PrizeDetailVo> transPirzeVoToDetailVo(List<PrizeVo> prizeVoList) {
        List<PrizeDetailVo> result = new ArrayList<>(prizeVoList.size());
        for (PrizeVo vo : prizeVoList) {
            PrizeDetailVo target = new PrizeDetailVo();
            target.setId(vo.getId());
            target.setTermNo(vo.getTermNo());
            target.setPrizeNo01(vo.getPrizeNums()[0]);
            target.setPrizeNo02(vo.getPrizeNums()[1]);
            target.setPrizeNo03(vo.getPrizeNums()[2]);
            target.setFirst01(vo.getFirst()[0]);
            target.setFirst02(vo.getFirst()[1]);
            target.setFirst03(vo.getFirst()[2]);
            target.setFirst04(vo.getFirst()[3]);
            target.setFirst05(vo.getFirst()[4]);
            target.setFirst06(vo.getFirst()[5]);
            target.setFirst07(vo.getFirst()[6]);
            target.setFirst08(vo.getFirst()[7]);
            target.setFirst09(vo.getFirst()[8]);
            target.setFirst10(vo.getFirst()[9]);
            target.setSecond01(vo.getSecond()[0]);
            target.setSecond02(vo.getSecond()[1]);
            target.setSecond03(vo.getSecond()[2]);
            target.setSecond04(vo.getSecond()[3]);
            target.setSecond05(vo.getSecond()[4]);
            target.setSecond06(vo.getSecond()[5]);
            target.setSecond07(vo.getSecond()[6]);
            target.setSecond08(vo.getSecond()[7]);
            target.setSecond09(vo.getSecond()[8]);
            target.setSecond10(vo.getSecond()[9]);
            target.setThird01(vo.getThird()[0]);
            target.setThird02(vo.getThird()[1]);
            target.setThird03(vo.getThird()[2]);
            target.setThird04(vo.getThird()[3]);
            target.setThird05(vo.getThird()[4]);
            target.setThird06(vo.getThird()[5]);
            target.setThird07(vo.getThird()[6]);
            target.setThird08(vo.getThird()[7]);
            target.setThird09(vo.getThird()[8]);
            target.setThird10(vo.getThird()[9]);
            target.setNumRegion01(vo.getRegion()[0]);
            target.setNumRegion02(vo.getRegion()[1]);
            target.setNumRegion03(vo.getRegion()[2]);
            target.setNumRegion04(vo.getRegion()[3]);
            target.setNumRegion05(vo.getRegion()[4]);
            target.setNumRegion06(vo.getRegion()[5]);
            target.setNumRegion07(vo.getRegion()[6]);
            target.setNumRegion08(vo.getRegion()[7]);
            target.setNumRegion09(vo.getRegion()[8]);
            target.setNumRegion10(vo.getRegion()[9]);
            result.add(target);
        }
        return result;
    }

    public List<PrizeStaticVo> getButtomPrizeStatics(List<PrizeDetailVo> prizeDetailVoList) {
        PrizeStaticVo occurTimes = new PrizeStaticVo();
        PrizeStaticVo maxContinus = new PrizeStaticVo();
        PrizeStaticVo maxMiss = new PrizeStaticVo();
        List<PrizeStaticVo> result = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(prizeDetailVoList)) {
            int continousFirst = 0;
            //获得各项出现次数
            setOccurTimes(prizeDetailVoList, occurTimes,maxContinus,maxMiss);

        }
        occurTimes.setDescription("出现次数");
        maxContinus.setDescription("最大连出");
        maxMiss.setDescription("最大遗漏");
        result.add(occurTimes);
        result.add(maxContinus);
        result.add(maxMiss);
        return result;
    }

    private void setOccurTimes(List<PrizeDetailVo> prizeDetailVoList,
                               PrizeStaticVo occurTimes,
                               PrizeStaticVo maxContinus,
                               PrizeStaticVo maxMiss) {
        List<Integer> numRegion01OkList = new ArrayList<>();
        List<Integer> numRegion01NoList = new ArrayList<>();
        int numRegion01Ok = 0;
        int numRegion01No = 0;
        List<Integer> numRegion02OkList = new ArrayList<>();
        List<Integer> numRegion02NoList = new ArrayList<>();
        int numRegion02Ok = 0;
        int numRegion02No = 0;
        List<Integer> numRegion03OkList = new ArrayList<>();
        List<Integer> numRegion03NoList = new ArrayList<>();
        int numRegion03Ok = 0;
        int numRegion03No = 0;
        List<Integer> numRegion04OkList = new ArrayList<>();
        List<Integer> numRegion04NoList = new ArrayList<>();
        int numRegion04Ok = 0;
        int numRegion04No = 0;
        List<Integer> numRegion05OkList = new ArrayList<>();
        List<Integer> numRegion05NoList = new ArrayList<>();
        int numRegion05Ok = 0;
        int numRegion05No = 0;
        List<Integer> numRegion06OkList = new ArrayList<>();
        List<Integer> numRegion06NoList = new ArrayList<>();
        int numRegion06Ok = 0;
        int numRegion06No = 0;
        List<Integer> numRegion07OkList = new ArrayList<>();
        List<Integer> numRegion07NoList = new ArrayList<>();
        int numRegion07Ok = 0;
        int numRegion07No = 0;
        List<Integer> numRegion08OkList = new ArrayList<>();
        List<Integer> numRegion08NoList = new ArrayList<>();
        int numRegion08Ok = 0;
        int numRegion08No = 0;
        List<Integer> numRegion09OkList = new ArrayList<>();
        List<Integer> numRegion09NoList = new ArrayList<>();
        int numRegion09Ok = 0;
        int numRegion09No = 0;
        List<Integer> numRegion10OkList = new ArrayList<>();
        List<Integer> numRegion10NoList = new ArrayList<>();
        int numRegion10Ok = 0;
        int numRegion10No = 0;
        List<Integer> first01OkList = new ArrayList<>();
        List<Integer> first01NoList = new ArrayList<>();
        int first01Ok = 0;
        int first01No = 0;
        List<Integer> first02OkList = new ArrayList<>();
        List<Integer> first02NoList = new ArrayList<>();
        int first02Ok = 0;
        int first02No = 0;
        List<Integer> first03OkList = new ArrayList<>();
        List<Integer> first03NoList = new ArrayList<>();
        int first03Ok = 0;
        int first03No = 0;
        List<Integer> first04OkList = new ArrayList<>();
        List<Integer> first04NoList = new ArrayList<>();
        int first04Ok = 0;
        int first04No = 0;
        List<Integer> first05OkList = new ArrayList<>();
        List<Integer> first05NoList = new ArrayList<>();
        int first05Ok = 0;
        int first05No = 0;
        List<Integer> first06OkList = new ArrayList<>();
        List<Integer> first06NoList = new ArrayList<>();
        int first06Ok = 0;
        int first06No = 0;
        List<Integer> first07OkList = new ArrayList<>();
        List<Integer> first07NoList = new ArrayList<>();
        int first07Ok = 0;
        int first07No = 0;
        List<Integer> first08OkList = new ArrayList<>();
        List<Integer> first08NoList = new ArrayList<>();
        int first08Ok = 0;
        int first08No = 0;
        List<Integer> first09OkList = new ArrayList<>();
        List<Integer> first09NoList = new ArrayList<>();
        int first09Ok = 0;
        int first09No = 0;
        List<Integer> first10OkList = new ArrayList<>();
        List<Integer> first10NoList = new ArrayList<>();
        int first10Ok = 0;
        int first10No = 0;
        List<Integer> second01OkList = new ArrayList<>();
        List<Integer> second01NoList = new ArrayList<>();
        int second01Ok = 0;
        int second01No = 0;
        List<Integer> second02OkList = new ArrayList<>();
        List<Integer> second02NoList = new ArrayList<>();
        int second02Ok = 0;
        int second02No = 0;
        List<Integer> second03OkList = new ArrayList<>();
        List<Integer> second03NoList = new ArrayList<>();
        int second03Ok = 0;
        int second03No = 0;
        List<Integer> second04OkList = new ArrayList<>();
        List<Integer> second04NoList = new ArrayList<>();
        int second04Ok = 0;
        int second04No = 0;
        List<Integer> second05OkList = new ArrayList<>();
        List<Integer> second05NoList = new ArrayList<>();
        int second05Ok = 0;
        int second05No = 0;
        List<Integer> second06OkList = new ArrayList<>();
        List<Integer> second06NoList = new ArrayList<>();
        int second06Ok = 0;
        int second06No = 0;
        List<Integer> second07OkList = new ArrayList<>();
        List<Integer> second07NoList = new ArrayList<>();
        int second07Ok = 0;
        int second07No = 0;
        List<Integer> second08OkList = new ArrayList<>();
        List<Integer> second08NoList = new ArrayList<>();
        int second08Ok = 0;
        int second08No = 0;
        List<Integer> second09OkList = new ArrayList<>();
        List<Integer> second09NoList = new ArrayList<>();
        int second09Ok = 0;
        int second09No = 0;
        List<Integer> second10OkList = new ArrayList<>();
        List<Integer> second10NoList = new ArrayList<>();
        int second10Ok = 0;
        int second10No = 0;
        List<Integer> third01OkList = new ArrayList<>();
        List<Integer> third01NoList = new ArrayList<>();
        int third01Ok = 0;
        int third01No = 0;
        List<Integer> third02OkList = new ArrayList<>();
        List<Integer> third02NoList = new ArrayList<>();
        int third02Ok = 0;
        int third02No = 0;
        List<Integer> third03OkList = new ArrayList<>();
        List<Integer> third03NoList = new ArrayList<>();
        int third03Ok = 0;
        int third03No = 0;
        List<Integer> third04OkList = new ArrayList<>();
        List<Integer> third04NoList = new ArrayList<>();
        int third04Ok = 0;
        int third04No = 0;
        List<Integer> third05OkList = new ArrayList<>();
        List<Integer> third05NoList = new ArrayList<>();
        int third05Ok = 0;
        int third05No = 0;
        List<Integer> third06OkList = new ArrayList<>();
        List<Integer> third06NoList = new ArrayList<>();
        int third06Ok = 0;
        int third06No = 0;
        List<Integer> third07OkList = new ArrayList<>();
        List<Integer> third07NoList = new ArrayList<>();
        int third07Ok = 0;
        int third07No = 0;
        List<Integer> third08OkList = new ArrayList<>();
        List<Integer> third08NoList = new ArrayList<>();
        int third08Ok = 0;
        int third08No = 0;
        List<Integer> third09OkList = new ArrayList<>();
        List<Integer> third09NoList = new ArrayList<>();
        int third09Ok = 0;
        int third09No = 0;
        List<Integer> third10OkList = new ArrayList<>();
        List<Integer> third10NoList = new ArrayList<>();
        int third10Ok = 0;
        int third10No = 0;

        for (PrizeDetailVo p : prizeDetailVoList) {

            if (       p.getNumRegion01().startsWith("ok")) {
                if (occurTimes.getNumRegion01() == null) {
                    occurTimes.setNumRegion01(1);
                } else {
                    occurTimes.setNumRegion01(occurTimes.getNumRegion01() + 1);
                }
                //未出连续中断
                if (numRegion01No>=0){
                    numRegion01NoList.add(numRegion01No);
                    numRegion01No=0;
                }
                numRegion01Ok=numRegion01Ok+1;
                numRegion01OkList.add(numRegion01Ok);
            }else{
                if (numRegion01Ok>=0){
                    numRegion01OkList.add(numRegion01Ok);
                    numRegion01Ok=0;
                }
                numRegion01No=numRegion01No+1;
                numRegion01NoList.add(numRegion01No);
            }
            if (       p.getNumRegion02().startsWith("ok")) {
                if (occurTimes.getNumRegion02() == null) {
                    occurTimes.setNumRegion02(1);
                } else {
                    occurTimes.setNumRegion02(occurTimes.getNumRegion02() + 1);
                }
                //未出连续中断
                if (numRegion02No>=0){
                    numRegion02NoList.add(numRegion02No);
                    numRegion02No=0;
                }
                numRegion02Ok=numRegion02Ok+1;
                numRegion02OkList.add(numRegion02Ok);
            }else{
                if (numRegion02Ok>=0){
                    numRegion02OkList.add(numRegion02Ok);
                    numRegion02Ok=0;
                }
                numRegion02No=numRegion02No+1;
                numRegion02NoList.add(numRegion02No);
            }
            if (       p.getNumRegion03().startsWith("ok")) {
                if (occurTimes.getNumRegion03() == null) {
                    occurTimes.setNumRegion03(1);
                } else {
                    occurTimes.setNumRegion03(occurTimes.getNumRegion03() + 1);
                }
                //未出连续中断
                if (numRegion03No>=0){
                    numRegion03NoList.add(numRegion03No);
                    numRegion03No=0;
                }
                numRegion03Ok=numRegion03Ok+1;
                numRegion03OkList.add(numRegion03Ok);
            }else{
                if (numRegion03Ok>=0){
                    numRegion03OkList.add(numRegion03Ok);
                    numRegion03Ok=0;
                }
                numRegion03No=numRegion03No+1;
                numRegion03NoList.add(numRegion03No);
            }
            if (       p.getNumRegion04().startsWith("ok")) {
                if (occurTimes.getNumRegion04() == null) {
                    occurTimes.setNumRegion04(1);
                } else {
                    occurTimes.setNumRegion04(occurTimes.getNumRegion04() + 1);
                }
                //未出连续中断
                if (numRegion04No>=0){
                    numRegion04NoList.add(numRegion04No);
                    numRegion04No=0;
                }
                numRegion04Ok=numRegion04Ok+1;
                numRegion04OkList.add(numRegion04Ok);
            }else{
                if (numRegion04Ok>=0){
                    numRegion04OkList.add(numRegion04Ok);
                    numRegion04Ok=0;
                }
                numRegion04No=numRegion04No+1;
                numRegion04NoList.add(numRegion04No);
            }
            if (       p.getNumRegion05().startsWith("ok")) {
                if (occurTimes.getNumRegion05() == null) {
                    occurTimes.setNumRegion05(1);
                } else {
                    occurTimes.setNumRegion05(occurTimes.getNumRegion05() + 1);
                }
                //未出连续中断
                if (numRegion05No>=0){
                    numRegion05NoList.add(numRegion05No);
                    numRegion05No=0;
                }
                numRegion05Ok=numRegion05Ok+1;
                numRegion05OkList.add(numRegion05Ok);

            }else{
                if (numRegion05Ok>=0){
                    numRegion05OkList.add(numRegion05Ok);
                    numRegion05Ok=0;
                }
                numRegion05No=numRegion05No+1;
                numRegion05NoList.add(numRegion05No);

            }
            if (       p.getNumRegion06().startsWith("ok")) {
                if (occurTimes.getNumRegion06() == null) {
                    occurTimes.setNumRegion06(1);
                } else {
                    occurTimes.setNumRegion06(occurTimes.getNumRegion06() + 1);
                }
                //未出连续中断
                if (numRegion06No>=0){
                    numRegion06NoList.add(numRegion06No);
                    numRegion06No=0;
                }
                numRegion06Ok=numRegion06Ok+1;
                numRegion06OkList.add(numRegion06Ok);

            }else{
                if (numRegion06Ok>=0){
                    numRegion06OkList.add(numRegion06Ok);
                    numRegion06Ok=0;
                }
                numRegion06No=numRegion06No+1;
                numRegion06NoList.add(numRegion06No);

            }
            if (       p.getNumRegion07().startsWith("ok")) {
                if (occurTimes.getNumRegion07() == null) {
                    occurTimes.setNumRegion07(1);
                } else {
                    occurTimes.setNumRegion07(occurTimes.getNumRegion07() + 1);
                }
                //未出连续中断
                if (numRegion07No>=0){
                    numRegion07NoList.add(numRegion07No);
                    numRegion07No=0;
                }
                numRegion07Ok=numRegion07Ok+1;
                numRegion07OkList.add(numRegion07Ok);

            }else{
                if (numRegion07Ok>=0){
                    numRegion07OkList.add(numRegion07Ok);
                    numRegion07Ok=0;
                }
                numRegion07No=numRegion07No+1;
                numRegion07NoList.add(numRegion07No);

            }
            if (       p.getNumRegion08().startsWith("ok")) {
                if (occurTimes.getNumRegion08() == null) {
                    occurTimes.setNumRegion08(1);
                } else {
                    occurTimes.setNumRegion08(occurTimes.getNumRegion08() + 1);
                }
                //未出连续中断
                if (numRegion08No>=0){
                    numRegion08NoList.add(numRegion08No);
                    numRegion08No=0;
                }
                numRegion08Ok=numRegion08Ok+1;
                numRegion08OkList.add(numRegion08Ok);

            }else{
                if (numRegion08Ok>=0){
                    numRegion08OkList.add(numRegion08Ok);
                    numRegion08Ok=0;
                }
                numRegion08No=numRegion08No+1;
                numRegion08NoList.add(numRegion08No);

            }
            if (       p.getNumRegion09().startsWith("ok")) {
                if (occurTimes.getNumRegion09() == null) {
                    occurTimes.setNumRegion09(1);
                } else {
                    occurTimes.setNumRegion09(occurTimes.getNumRegion09() + 1);
                }
                //未出连续中断
                if (numRegion09No>=0){
                    numRegion09NoList.add(numRegion09No);
                    numRegion09No=0;
                }
                numRegion09Ok=numRegion09Ok+1;
                numRegion09OkList.add(numRegion09Ok);

            }else{
                if (numRegion09Ok>=0){
                    numRegion09OkList.add(numRegion09Ok);
                    numRegion09Ok=0;
                }
                numRegion09No=numRegion09No+1;
                numRegion09NoList.add(numRegion09No);

            }
            if (       p.getNumRegion10().startsWith("ok")) {
                if (occurTimes.getNumRegion10() == null) {
                    occurTimes.setNumRegion10(1);
                } else {
                    occurTimes.setNumRegion10(occurTimes.getNumRegion10() + 1);
                }
                //未出连续中断
                if (numRegion10No>=0){
                    numRegion10NoList.add(numRegion10No);
                    numRegion10No=0;
                }
                numRegion10Ok=numRegion10Ok+1;
                numRegion10OkList.add(numRegion10Ok);

            }else{
                if (numRegion10Ok>=0){
                    numRegion10OkList.add(numRegion10Ok);
                    numRegion10Ok=0;
                }
                numRegion10No=numRegion10No+1;
                numRegion10NoList.add(numRegion10No);

            }
            if (       p.getFirst01().startsWith("ok")) {
                if (occurTimes.getFirst01() == null) {
                    occurTimes.setFirst01(1);
                } else {
                    occurTimes.setFirst01(occurTimes.getFirst01() + 1);
                }
                //未出连续中断
                if (first01No>=0){
                    first01NoList.add(first01No);
                    first01No=0;
                }
                first01Ok=first01Ok+1;
                first01OkList.add(first01Ok);

            }else{
                if (first01Ok>=0){
                    first01OkList.add(first01Ok);
                    first01Ok=0;
                }
                first01No=first01No+1;
                first01NoList.add(first01No);

            }
            if (       p.getFirst02().startsWith("ok")) {
                if (occurTimes.getFirst02() == null) {
                    occurTimes.setFirst02(1);
                } else {
                    occurTimes.setFirst02(occurTimes.getFirst02() + 1);
                }
                //未出连续中断
                if (first02No>=0){
                    first02NoList.add(first02No);
                    first02No=0;
                }
                first02Ok=first02Ok+1;
                first02OkList.add(first02Ok);

            }else{
                if (first02Ok>=0){
                    first02OkList.add(first02Ok);
                    first02Ok=0;
                }
                first02No=first02No+1;
                first02NoList.add(first02No);

            }
            if (       p.getFirst03().startsWith("ok")) {
                if (occurTimes.getFirst03() == null) {
                    occurTimes.setFirst03(1);
                } else {
                    occurTimes.setFirst03(occurTimes.getFirst03() + 1);
                }
                //未出连续中断
                if (first03No>=0){
                    first03NoList.add(first03No);
                    first03No=0;
                }
                first03Ok=first03Ok+1;
                first03OkList.add(first03Ok);

            }else{
                if (first03Ok>=0){
                    first03OkList.add(first03Ok);
                    first03Ok=0;
                }
                first03No=first03No+1;
                first03NoList.add(first03No);

            }
            if (       p.getFirst04().startsWith("ok")) {
                if (occurTimes.getFirst04() == null) {
                    occurTimes.setFirst04(1);
                } else {
                    occurTimes.setFirst04(occurTimes.getFirst04() + 1);
                }
                //未出连续中断
                if (first04No>=0){
                    first04NoList.add(first04No);
                    first04No=0;
                }
                first04Ok=first04Ok+1;
                first04OkList.add(first04Ok);

            }else{
                if (first04Ok>=0){
                    first04OkList.add(first04Ok);
                    first04Ok=0;
                }
                first04No=first04No+1;
                first04NoList.add(first04No);

            }
            if (       p.getFirst05().startsWith("ok")) {
                if (occurTimes.getFirst05() == null) {
                    occurTimes.setFirst05(1);
                } else {
                    occurTimes.setFirst05(occurTimes.getFirst05() + 1);
                }
                //未出连续中断
                if (first05No>=0){
                    first05NoList.add(first05No);
                    first05No=0;
                }
                first05Ok=first05Ok+1;
                first05OkList.add(first05Ok);

            }else{
                if (first05Ok>=0){
                    first05OkList.add(first05Ok);
                    first05Ok=0;
                }
                first05No=first05No+1;
                first05NoList.add(first05No);

            }
            if (       p.getFirst06().startsWith("ok")) {
                if (occurTimes.getFirst06() == null) {
                    occurTimes.setFirst06(1);
                } else {
                    occurTimes.setFirst06(occurTimes.getFirst06() + 1);
                }
                //未出连续中断
                if (first06No>=0){
                    first06NoList.add(first06No);
                    first06No=0;
                }
                first06Ok=first06Ok+1;
                first06OkList.add(first06Ok);

            }else{
                if (first06Ok>=0){
                    first06OkList.add(first06Ok);
                    first06Ok=0;
                }
                first06No=first06No+1;
                first06NoList.add(first06No);

            }
            if (       p.getFirst07().startsWith("ok")) {
                if (occurTimes.getFirst07() == null) {
                    occurTimes.setFirst07(1);
                } else {
                    occurTimes.setFirst07(occurTimes.getFirst07() + 1);
                }
                //未出连续中断
                if (first07No>=0){
                    first07NoList.add(first07No);
                    first07No=0;
                }
                first07Ok=first07Ok+1;
                first07OkList.add(first07Ok);

            }else{
                if (first07Ok>=0){
                    first07OkList.add(first07Ok);
                    first07Ok=0;
                }
                first07No=first07No+1;
                first07NoList.add(first07No);

            }
            if (       p.getFirst08().startsWith("ok")) {
                if (occurTimes.getFirst08() == null) {
                    occurTimes.setFirst08(1);
                } else {
                    occurTimes.setFirst08(occurTimes.getFirst08() + 1);
                }
                //未出连续中断
                if (first08No>=0){
                    first08NoList.add(first08No);
                    first08No=0;
                }
                first08Ok=first08Ok+1;
                first08OkList.add(first08Ok);

            }else{
                if (first08Ok>=0){
                    first08OkList.add(first08Ok);
                    first08Ok=0;
                }
                first08No=first08No+1;
                first08NoList.add(first08No);

            }
            if (       p.getFirst09().startsWith("ok")) {
                if (occurTimes.getFirst09() == null) {
                    occurTimes.setFirst09(1);
                } else {
                    occurTimes.setFirst09(occurTimes.getFirst09() + 1);
                }
                //未出连续中断
                if (first09No>=0){
                    first09NoList.add(first09No);
                    first09No=0;
                }
                first09Ok=first09Ok+1;
                first09OkList.add(first09Ok);

            }else{
                if (first09Ok>=0){
                    first09OkList.add(first09Ok);
                    first09Ok=0;
                }
                first09No=first09No+1;
                first09NoList.add(first09No);

            }
            if (       p.getFirst10().startsWith("ok")) {
                if (occurTimes.getFirst10() == null) {
                    occurTimes.setFirst10(1);
                } else {
                    occurTimes.setFirst10(occurTimes.getFirst10() + 1);
                }
                //未出连续中断
                if (first10No>=0){
                    first10NoList.add(first10No);
                    first10No=0;
                }
                first10Ok=first10Ok+1;
                first10OkList.add(first10Ok);

            }else{
                if (first10Ok>=0){
                    first10OkList.add(first10Ok);
                    first10Ok=0;
                }
                first10No=first10No+1;
                first10NoList.add(first10No);

            }
            if (       p.getSecond01().startsWith("ok")) {
                if (occurTimes.getSecond01() == null) {
                    occurTimes.setSecond01(1);
                } else {
                    occurTimes.setSecond01(occurTimes.getSecond01() + 1);
                }
                //未出连续中断
                if (second01No>=0){
                    second01NoList.add(second01No);
                    second01No=0;
                }
                second01Ok=second01Ok+1;
                second01OkList.add(second01Ok);


            }else{
                if (second01Ok>=0){
                    second01OkList.add(second01Ok);
                    second01Ok=0;
                }
                second01No=second01No+1;
                second01NoList.add(second01No);

            }
            if (       p.getSecond02().startsWith("ok")) {
                if (occurTimes.getSecond02() == null) {
                    occurTimes.setSecond02(1);
                } else {
                    occurTimes.setSecond02(occurTimes.getSecond02() + 1);
                }
                //未出连续中断
                if (second02No>=0){
                    second02NoList.add(second02No);
                    second02No=0;
                }
                second02Ok=second02Ok+1;
                second02OkList.add(second02Ok);

            }else{
                if (second02Ok>=0){
                    second02OkList.add(second02Ok);
                    second02Ok=0;
                }
                second02No=second02No+1;
                second02NoList.add(second02No);
            }
            if (       p.getSecond03().startsWith("ok")) {
                if (occurTimes.getSecond03() == null) {
                    occurTimes.setSecond03(1);
                } else {
                    occurTimes.setSecond03(occurTimes.getSecond03() + 1);
                }
                //未出连续中断
                if (second03No>=0){
                    second03NoList.add(second03No);
                    second03No=0;
                }
                second03Ok=second03Ok+1;
                second03OkList.add(second03Ok);

            }else{
                if (second03Ok>=0){
                    second03OkList.add(second03Ok);
                    second03Ok=0;
                }
                second03No=second03No+1;
                second03NoList.add(second03No);
            }
            if (       p.getSecond04().startsWith("ok")) {
                if (occurTimes.getSecond04() == null) {
                    occurTimes.setSecond04(1);
                } else {
                    occurTimes.setSecond04(occurTimes.getSecond04() + 1);
                }
                //未出连续中断
                if (second04No>=0){
                    second04NoList.add(second04No);
                    second04No=0;
                }
                second04Ok=second04Ok+1;
                second04OkList.add(second04Ok);

            }else{
                if (second04Ok>=0){
                    second04OkList.add(second04Ok);
                    second04Ok=0;
                }
                second04No=second04No+1;
                second04NoList.add(second04No);

            }
            if (       p.getSecond05().startsWith("ok")) {
                if (occurTimes.getSecond05() == null) {
                    occurTimes.setSecond05(1);
                } else {
                    occurTimes.setSecond05(occurTimes.getSecond05() + 1);
                }
                //未出连续中断
                if (second05No>=0){
                    second05NoList.add(second05No);
                    second05No=0;
                }
                second05Ok=second05Ok+1;
                second05OkList.add(second05Ok);

            }else{
                if (second05Ok>=0){
                    second05OkList.add(second05Ok);
                    second05Ok=0;
                }
                second05No=second05No+1;
                second05NoList.add(second05No);
            }
            if (       p.getSecond06().startsWith("ok")) {
                if (occurTimes.getSecond06() == null) {
                    occurTimes.setSecond06(1);
                } else {
                    occurTimes.setSecond06(occurTimes.getSecond06() + 1);
                }
                //未出连续中断
                if (second06No>=0){
                    second06NoList.add(second06No);
                    second06No=0;
                }
                second06Ok=second06Ok+1;
                second06OkList.add(second06Ok);

            }else{
                if (second06Ok>=0){
                    second06OkList.add(second06Ok);
                    second06Ok=0;
                }
                second06No=second06No+1;
                second06NoList.add(second06No);

            }
            if (       p.getSecond07().startsWith("ok")) {
                if (occurTimes.getSecond07() == null) {
                    occurTimes.setSecond07(1);
                } else {
                    occurTimes.setSecond07(occurTimes.getSecond07() + 1);
                }
                //未出连续中断
                if (second07No>=0){
                    second07NoList.add(second07No);
                    second07No=0;
                }
                second07Ok=second07Ok+1;
                second07OkList.add(second07Ok);

            }else{
                if (second07Ok>=0){
                    second07OkList.add(second07Ok);
                    second07Ok=0;
                }
                second07No=second07No+1;
                second07NoList.add(second07No);

            }
            if (       p.getSecond08().startsWith("ok")) {
                if (occurTimes.getSecond08() == null) {
                    occurTimes.setSecond08(1);
                } else {
                    occurTimes.setSecond08(occurTimes.getSecond08() + 1);
                }
                //未出连续中断
                if (second08No>=0){
                    second08NoList.add(second08No);
                    second08No=0;
                }
                second08Ok=second08Ok+1;
                second08OkList.add(second08Ok);

            }else{
                if (second08Ok>=0){
                    second08OkList.add(second08Ok);
                    second08Ok=0;
                }
                second08No=second08No+1;
                second08NoList.add(second08No);

            }
            if (       p.getSecond09().startsWith("ok")) {
                if (occurTimes.getSecond09() == null) {
                    occurTimes.setSecond09(1);
                } else {
                    occurTimes.setSecond09(occurTimes.getSecond09() + 1);
                }
                //未出连续中断
                if (second09No>=0){
                    second09NoList.add(second09No);
                    second09No=0;
                }
                second09Ok=second09Ok+1;
                second09OkList.add(second09Ok);

            }else{
                if (second09Ok>=0){
                    second09OkList.add(second09Ok);
                    second09Ok=0;
                }
                second09No=second09No+1;
                second09NoList.add(second09No);

            }
            if (       p.getSecond10().startsWith("ok")) {
                if (occurTimes.getSecond10() == null) {
                    occurTimes.setSecond10(1);
                } else {
                    occurTimes.setSecond10(occurTimes.getSecond10() + 1);
                }
                //未出连续中断
                if (second10No>=0){
                    second10NoList.add(second10No);
                    second10No=0;
                }
                second10Ok=second10Ok+1;
                second10OkList.add(second10Ok);

            }else{
                if (second10Ok>=0){
                    second10OkList.add(second10Ok);
                    second10Ok=0;
                }
                second10No=second10No+1;
                second10NoList.add(second10No);

            }
            if (       p.getThird01().startsWith("ok")) {
                if (occurTimes.getThird01() == null) {
                    occurTimes.setThird01(1);
                } else {
                    occurTimes.setThird01(occurTimes.getThird01() + 1);
                }
                //未出连续中断
                if (third01No>=0){
                    third01NoList.add(third01No);
                    third01No=0;
                }
                third01Ok=third01Ok+1;
                third01OkList.add(third01Ok);

            }else{
                if (third01Ok>=0){
                    third01OkList.add(third01Ok);
                    third01Ok=0;
                }
                third01No=third01No+1;
                third01NoList.add(third01No);

            }
            if (       p.getThird02().startsWith("ok")) {
                if (occurTimes.getThird02() == null) {
                    occurTimes.setThird02(1);
                } else {
                    occurTimes.setThird02(occurTimes.getThird02() + 1);
                }
                //未出连续中断
                if (third02No>=0){
                    third02NoList.add(third02No);
                    third02No=0;
                }
                third02Ok=third02Ok+1;
                third02OkList.add(third02Ok);

            }else{
                if (third02Ok>=0){
                    third02OkList.add(third02Ok);
                    third02Ok=0;
                }
                third02No=third02No+1;
                third02NoList.add(third02No);
            }
            if (       p.getThird03().startsWith("ok")) {
                if (occurTimes.getThird03() == null) {
                    occurTimes.setThird03(1);
                } else {
                    occurTimes.setThird03(occurTimes.getThird03() + 1);
                }
                //未出连续中断
                if (third03No>=0){
                    third03NoList.add(third03No);
                    third03No=0;
                }
                third03Ok=third03Ok+1;
                third03OkList.add(third03Ok);
            }else{
                if (third03Ok>=0){
                    third03OkList.add(third03Ok);
                    third03Ok=0;
                }
                third03No=third03No+1;
                third03NoList.add(third03No);
            }
            if (       p.getThird04().startsWith("ok")) {
                if (occurTimes.getThird04() == null) {
                    occurTimes.setThird04(1);
                } else {
                    occurTimes.setThird04(occurTimes.getThird04() + 1);
                }
                //未出连续中断
                if (third04No>=0){
                    third04NoList.add(third04No);
                    third04No=0;
                }
                third04Ok=third04Ok+1;
                third04OkList.add(third04Ok);
            }else{
                if (third04Ok>=0){
                    third04OkList.add(third04Ok);
                    third04Ok=0;
                }
                third04No=third04No+1;
                third04NoList.add(third04No);
            }
            if (       p.getThird05().startsWith("ok")) {
                if (occurTimes.getThird05() == null) {
                    occurTimes.setThird05(1);
                } else {
                    occurTimes.setThird05(occurTimes.getThird05() + 1);
                }
                //未出连续中断
                if (third05No>=0){
                    third05NoList.add(third05No);
                    third05No=0;
                }
                third05Ok=third05Ok+1;
                third05OkList.add(third05Ok);
            }else{
                if (third05Ok>=0){
                    third05OkList.add(third05Ok);
                    third05Ok=0;
                }
                third05No=third05No+1;
                third05NoList.add(third05No);
            }
            if (       p.getThird06().startsWith("ok")) {
                if (occurTimes.getThird06() == null) {
                    occurTimes.setThird06(1);
                } else {
                    occurTimes.setThird06(occurTimes.getThird06() + 1);
                }
                //未出连续中断
                if (third06No>=0){
                    third06NoList.add(third06No);
                    third06No=0;
                }
                third06Ok=third06Ok+1;
                third06OkList.add(third06Ok);
            }else{
                if (third06Ok>=0){
                    third06OkList.add(third06Ok);
                    third06Ok=0;
                }
                third06No=third06No+1;
                third06NoList.add(third06No);
            }
            if (       p.getThird07().startsWith("ok")) {
                if (occurTimes.getThird07() == null) {
                    occurTimes.setThird07(1);
                } else {
                    occurTimes.setThird07(occurTimes.getThird07() + 1);
                }
                //未出连续中断
                if (third07No>=0){
                    third07NoList.add(third07No);
                    third07No=0;
                }
                third07Ok=third07Ok+1;
                third07OkList.add(third07Ok);
            }else{
                if (third07Ok>=0){
                    third07OkList.add(third07Ok);
                    third07Ok=0;
                }
                third07No=third07No+1;
                third07NoList.add(third07No);
            }
            if (       p.getThird08().startsWith("ok")) {
                if (occurTimes.getThird08() == null) {
                    occurTimes.setThird08(1);
                } else {
                    occurTimes.setThird08(occurTimes.getThird08() + 1);
                }
                //未出连续中断
                if (third08No>=0){
                    third08NoList.add(third08No);
                    third08No=0;
                }
                third08Ok=third08Ok+1;
                third08OkList.add(third08Ok);

            }else{
                if (third08Ok>=0){
                    third08OkList.add(third08Ok);
                    third08Ok=0;
                }
                third08No=third08No+1;
                third08NoList.add(third08No);

            }
            if (       p.getThird09().startsWith("ok")) {
                if (occurTimes.getThird09() == null) {
                    occurTimes.setThird09(1);
                } else {
                    occurTimes.setThird09(occurTimes.getThird09() + 1);
                }
                //未出连续中断
                if (third09No>=0){
                    third09NoList.add(third09No);
                    third09No=0;
                }
                third09Ok=third09Ok+1;
                third09OkList.add(third09Ok);

            }else{
                if (third09Ok>=0){
                    third09OkList.add(third09Ok);
                    third09Ok=0;
                }
                third09No=third09No+1;
                third09NoList.add(third09No);

            }
            if (       p.getThird10().startsWith("ok")) {
                if (occurTimes.getThird10() == null) {
                    occurTimes.setThird10(1);
                } else {
                    occurTimes.setThird10(occurTimes.getThird10() + 1);
                }
                //未出连续中断
                if (third10No>=0){
                    third10NoList.add(third10No);
                    third10No=0;
                }
                third10Ok=third10Ok+1;
                third10OkList.add(third10Ok);

            }else{
                if (third10Ok>=0){
                    third10OkList.add(third10Ok);
                    third10Ok=0;
                }
                third10No=third10No+1;
                third10NoList.add(third10No);

            }



        }
        //最大连续
        maxContinus.setNumRegion01(max(numRegion01OkList));
        maxContinus.setNumRegion02(max(numRegion02OkList));
        maxContinus.setNumRegion03(max(numRegion03OkList));
        maxContinus.setNumRegion04(max(numRegion04OkList));
        maxContinus.setNumRegion05(max(numRegion05OkList));
        maxContinus.setNumRegion06(max(numRegion06OkList));
        maxContinus.setNumRegion07(max(numRegion07OkList));
        maxContinus.setNumRegion08(max(numRegion08OkList));
        maxContinus.setNumRegion09(max(numRegion09OkList));
        maxContinus.setNumRegion10(max(numRegion10OkList));
        maxContinus.setFirst01(max(first01OkList));
        maxContinus.setFirst02(max(first02OkList));
        maxContinus.setFirst03(max(first03OkList));
        maxContinus.setFirst04(max(first04OkList));
        maxContinus.setFirst05(max(first05OkList));
        maxContinus.setFirst06(max(first06OkList));
        maxContinus.setFirst07(max(first07OkList));
        maxContinus.setFirst08(max(first08OkList));
        maxContinus.setFirst09(max(first09OkList));
        maxContinus.setFirst10(max(first10OkList));
        maxContinus.setSecond01(max(second01OkList));
        maxContinus.setSecond02(max(second02OkList));
        maxContinus.setSecond03(max(second03OkList));
        maxContinus.setSecond04(max(second04OkList));
        maxContinus.setSecond05(max(second05OkList));
        maxContinus.setSecond06(max(second06OkList));
        maxContinus.setSecond07(max(second07OkList));
        maxContinus.setSecond08(max(second08OkList));
        maxContinus.setSecond09(max(second09OkList));
        maxContinus.setSecond10(max(second10OkList));
        maxContinus.setThird01(max(third01OkList));
        maxContinus.setThird02(max(third02OkList));
        maxContinus.setThird03(max(third03OkList));
        maxContinus.setThird04(max(third04OkList));
        maxContinus.setThird05(max(third05OkList));
        maxContinus.setThird06(max(third06OkList));
        maxContinus.setThird07(max(third07OkList));
        maxContinus.setThird08(max(third08OkList));
        maxContinus.setThird09(max(third09OkList));
        maxContinus.setThird10(max(third10OkList));
        //最大遗漏
        maxMiss.setNumRegion01(max(numRegion01NoList));
        maxMiss.setNumRegion02(max(numRegion02NoList));
        maxMiss.setNumRegion03(max(numRegion03NoList));
        maxMiss.setNumRegion04(max(numRegion04NoList));
        maxMiss.setNumRegion05(max(numRegion05NoList));
        maxMiss.setNumRegion06(max(numRegion06NoList));
        maxMiss.setNumRegion07(max(numRegion07NoList));
        maxMiss.setNumRegion08(max(numRegion08NoList));
        maxMiss.setNumRegion09(max(numRegion09NoList));
        maxMiss.setNumRegion10(max(numRegion10NoList));
        maxMiss.setFirst01(max(first01NoList));
        maxMiss.setFirst02(max(first02NoList));
        maxMiss.setFirst03(max(first03NoList));
        maxMiss.setFirst04(max(first04NoList));
        maxMiss.setFirst05(max(first05NoList));
        maxMiss.setFirst06(max(first06NoList));
        maxMiss.setFirst07(max(first07NoList));
        maxMiss.setFirst08(max(first08NoList));
        maxMiss.setFirst09(max(first09NoList));
        maxMiss.setFirst10(max(first10NoList));
        maxMiss.setSecond01(max(second01NoList));
        maxMiss.setSecond02(max(second02NoList));
        maxMiss.setSecond03(max(second03NoList));
        maxMiss.setSecond04(max(second04NoList));
        maxMiss.setSecond05(max(second05NoList));
        maxMiss.setSecond06(max(second06NoList));
        maxMiss.setSecond07(max(second07NoList));
        maxMiss.setSecond08(max(second08NoList));
        maxMiss.setSecond09(max(second09NoList));
        maxMiss.setSecond10(max(second10NoList));
        maxMiss.setThird01(max(third01NoList));
        maxMiss.setThird02(max(third02NoList));
        maxMiss.setThird03(max(third03NoList));
        maxMiss.setThird04(max(third04NoList));
        maxMiss.setThird05(max(third05NoList));
        maxMiss.setThird06(max(third06NoList));
        maxMiss.setThird07(max(third07NoList));
        maxMiss.setThird08(max(third08NoList));
        maxMiss.setThird09(max(third09NoList));
        maxMiss.setThird10(max(third10NoList));
    }

    public List<String> calculateTrendIndexData(List<Integer> region,
                                                List<Integer> first,
                                                List<Integer> second,
                                                List<Integer> third,
                                                List<Integer> regionOccurs,
                                                List<Integer> occurs) {
        List<String> resut=Lists.newArrayList();
        Set<String> danma=  CalculateUtil.calcDanMa(region,regionOccurs);
        Set<String> dingweima=  CalculateUtil.calcDingweiMa(first,second,third,occurs);
        if(CollectionUtils.isEmpty(regionOccurs)){
            resut=Lists.newArrayList(dingweima);
        }else {
            if (occurs.size()>0){
                danma.retainAll(dingweima);
            }
            resut=Lists.newArrayList(danma);
        }
        return resut;
    }

    /**
     * 返回720种数据可能
     * @return
     */
    public List<String> getTrandIndexCodeData() {
        Set<String> danma=  CalculateUtil.calcNoCondition();
        List<String> resut=Lists.newArrayList();
        resut=Lists.newArrayList(danma);
        return resut;
    }

    public PrizeInfoEntity getNewestPrizeData() {
        String sql = "select id,term_no termNo,prize_no1 prizeNo01,prize_no2 prizeNo02,prize_no3 prizeNo03," +
                "prize_no4 prizeNo04," +
                "prize_no5 prizeNo05,prize_no6 prizeNo06,prize_no7 prizeNo07," +
                "prize_no8 prizeNo08,prize_no9 prizeNo09,prize_no10 prizeNo10," +
                "open_time openTime from  t_prize_base_info order by id desc limit 1";
        List<PrizeInfoEntity> list= jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(PrizeInfoEntity.class));
        if (CollectionUtils.isNotEmpty(list)){
            return  list.get(0);
        }else {
            return new PrizeInfoEntity();
        }

    }


    public void deleteTrendConditionById(String id) {

        //删除该条件对应的数据集合
        redisUtil.hdel(RedisConstant.USER_UUID_SET+ MySysUser.id(),id);
        //删除对应的条件 表格视图展示(含汉字)
        redisUtil.hdel(RedisConstant.USER_CONDITION+MySysUser.id(),id);
        //删除对应的条件 表格处理数据(只包含要处理的数据)
        redisUtil.hdel(RedisConstant.USER_CONDITION_INFO+MySysUser.id(),id);

    }


}
