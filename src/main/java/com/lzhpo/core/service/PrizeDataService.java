package com.lzhpo.core.service;

import com.google.common.collect.Lists;
import com.lzhpo.core.domain.PrizeData;
import com.lzhpo.core.domain.PrizeDetailVo;
import com.lzhpo.core.domain.PrizeInfoEntity;
import com.lzhpo.core.domain.PrizeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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


    /**
     * 基本走势
     * 先获取最新的一条数据
     * @param prizeData
     */
    public void handlerOriginDataTrend(PrizeData prizeData) {
        //保存到数据库
        String sql=" insert into t_prize_base_info(term_no,prize_no1,prize_no2,prize_no3,prize_no4,prize_no5,prize_no6,prize_no7," +
                " prize_no8,prize_no9,prize_no10,open_time" +
                " )values(?,?,?,?,?,?,?,?,?,?,?,?)";
        String [] nums=prizeData.getPrizeNums();
        Object[] arr=new Object[] {
                prizeData.getTermNo(),nums[0],nums[1],nums[2],nums[3],nums[4],nums[5],nums[6],nums[7],
                nums[8],nums[9],prizeData.getOpenTIme()
        };
        jdbcTemplate.update(sql,arr);

    }


    public List<PrizeInfoEntity> queryPrizeDataLimit() {

        String sql="select id,term_no termNo,prize_no1 prizeNo01,prize_no2 prizeNo02,prize_no3 prizeNo03," +
                "prize_no4 prizeNo04," +
                "prize_no5 prizeNo05,prize_no6 prizeNo06,prize_no7 prizeNo07," +
                "prize_no8 prizeMo08,prize_no9 prizeNo09,prize_no10 prizeNo10,open_time prizeNo10 from  t_prize_base_info order by id desc limit 500";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(PrizeInfoEntity.class));

    }




    public List<PrizeVo> getPrizeVoList() {

        List<PrizeInfoEntity> remoteList = queryPrizeDataLimit();
        remoteList.sort(comparingInt(PrizeInfoEntity::getId));
        List<PrizeVo> voList = new ArrayList<>(remoteList.size());
        for (int i = 0; i < remoteList.size(); i++) {
            PrizeInfoEntity entity = remoteList.get(i);
            PrizeVo vo = new PrizeVo();
            vo.setId(entity.getId());
            vo.setTermNo(entity.getTermNo());
            String[] prizeNumArr = {entity.getPrizeNo01(), entity.getPrizeNo02(), entity.getPrizeNo03()};
            vo.setPrizeNums(prizeNumArr);
            //第一条数据
            if (voList.size() == 0) {
                staticFirstRecord( vo, entity);
            }else{
                //获取上一条统计记录
                PrizeVo preVo=voList.get(voList.size() -1);
                //设置了必要值，需要统计间隔
                staticFirstRecord( vo, entity);
                String[] region=vo.getRegion();
                String [] first=vo.getFirst();
                String [] second=vo.getSecond();
                String [] third=vo.getThird();
                String[] pre_region=preVo.getRegion();
                String [] pre_first=preVo.getFirst();
                String [] pre_second=preVo.getSecond();
                String [] pre_third=preVo.getThird();
                arrValueStatics(region,pre_region);
                arrValueStatics(first,pre_first);
                arrValueStatics(second,pre_second);
                arrValueStatics(third,pre_third);
            }
            voList.add(vo);
        }
        voList.forEach(s->{
            System.out.println(s);
        });

        return  voList;
    }

    private void arrValueStatics(String[] region, String[] pre_region) {

        for (int j = 0; j <region.length ; j++) {
            //如果当前位置为no;
            if (region[j].startsWith("no")){
                //如果上一条对应位置为no。值+1；否则为0
                if (pre_region[j].startsWith("no")){
                    int  temp=Integer.valueOf(pre_region[j].split(",")[1])+1;
                    region[j]="no,"+temp;
                }else {
                    region[j]="no,1";
                }
            }
        }


    }

    private void staticFirstRecord(PrizeVo vo, PrizeInfoEntity entity) {
        int num1 = Integer.valueOf(entity.getPrizeNo01());
        int num2 = Integer.valueOf(entity.getPrizeNo02());
        int num3 = Integer.valueOf(entity.getPrizeNo03());
        String [] region=vo.getRegion();
        List<String[]> list2= Lists.newArrayList(vo.getFirst(),vo.getSecond(),vo.getThird());
        List<Integer> list3= Lists.newArrayList(num1,num2, num3);
        for (int j = 0; j < region.length; j++) {
            if ((j+1) == num1){
                region[j]="ok,"+num1;
            }else if ((j+1) == num2){
                region[j]="ok,"+num2;
            }else  if ((j+1) == num3){
                region[j]="ok,"+num3;
            }else{
                region[j]="no,"+1;
            }
        }
        for(int n=0;n<list2.size();n++){
            String [] arr=list2.get(n);
            int locationNum=list3.get(n);
            for (int j = 0; j < arr.length; j++) {
                if ((j+1) == locationNum){
                    arr[j]="ok,"+locationNum;
                }else{
                    arr[j]="no,"+1;
                }
            }

        }
    }

    public List<PrizeDetailVo> transPirzeVoToDetailVo(List<PrizeVo> prizeVoList) {
        List<PrizeDetailVo> result=new ArrayList<>(prizeVoList.size());
        for(PrizeVo vo:prizeVoList){
            PrizeDetailVo target=new PrizeDetailVo();
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
}
