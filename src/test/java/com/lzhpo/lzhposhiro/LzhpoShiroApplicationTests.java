package com.lzhpo.lzhposhiro;
import	java.lang.reflect.Method;

import com.google.common.collect.Lists;
import com.lzhpo.core.domain.PrizeData;
import com.lzhpo.core.domain.PrizeDetailVo;
import com.lzhpo.core.domain.PrizeInfoEntity;
import com.lzhpo.core.service.PrizeDataService;
import com.lzhpo.core.utils.DataGeneratorUtil;
import com.lzhpo.core.work.MainWork;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Method;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LzhpoShiroApplicationTests {


    @Autowired
    private  MainWork work;

    @Autowired
    private PrizeDataService service;


    @Test
    public void generateData() {
        /**
         * 生成数据
         */

        for (int i = 0; i <20 ; i++) {
            String str= work.generateSimulationData();
            PrizeData data=DataGeneratorUtil.converStrToPrizeData(str);
            service.handlerOriginDataTrend(data);
        }
    }

    @Test
    public void printViewTable() {

      List<PrizeInfoEntity> list=service.queryPrizeDataLimit();
        for (int i = 0; i <list.size() ; i++) {
            PrizeInfoEntity entity=list.get(i);
            PrizeDetailVo vo=new PrizeDetailVo();
            String no1,no2,no3;
            no1=entity.getPrizeNo01();
            no2=entity.getPrizeNo02();
            no3=entity.getPrizeNo03();
            vo.setPrizeNo01(no1);
            vo.setPrizeNo02(no2);
            vo.setPrizeNo03(no3);
            PrizeDetailVo preVo=null;
            if (i==0){
                vo.setId(entity.getId());
                vo.setTermNo(entity.getTermNo());
                String [] arr={no1,no2,no3};
                setValueVo(vo,preVo,arr);
            }else {

            }
        }
    }

    private void setValueVo(PrizeDetailVo vo, PrizeDetailVo preVo, String [] nums) {
        Class clazz=vo.getClass();
        Method[] methods= clazz.getDeclaredMethods();
        List<Method> listNumRegion= Lists.newArrayList();
        List<Method> listFirst= Lists.newArrayList();
        List<Method> listSecond= Lists.newArrayList();
        List<Method> listThird= Lists.newArrayList();
        for (Method method : methods){
            String methodName =method.getName();
            if (!methodName.startsWith("set")){
                continue;
            }
            System.out.println(methodName);
            if (methodName.contains("NumRegion")){
                listNumRegion.add(method);
            }
            if (methodName.contains("First")){
                listFirst.add(method);
            }
            if (methodName.contains("Second")){
                listSecond.add(method);
            }
            if (methodName.contains("Third")){
                listThird.add(method);
            }
        }
        //和哪个方法的位数对应上就设置该方法的值
        for (String no : nums){

        }


    }

}
