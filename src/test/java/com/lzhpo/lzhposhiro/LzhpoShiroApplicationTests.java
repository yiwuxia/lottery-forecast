package com.lzhpo.lzhposhiro;

import com.google.common.collect.Lists;
import com.lzhpo.core.config.RedisUtil;
import com.lzhpo.core.domain.PrizeInfoEntity;
import com.lzhpo.core.domain.PrizeVo;
import com.lzhpo.core.domain.dragon.DragonPhoenixStaticVo;
import com.lzhpo.core.domain.dragon.DragonPhoenixVo;
import com.lzhpo.core.service.DragonDataService;
import com.lzhpo.core.service.PrizeDataService;
import com.lzhpo.core.utils.CalculateUtil;
import com.lzhpo.core.work.MainWork;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LzhpoShiroApplicationTests {


    @Autowired
    private PrizeDataService prizeDataService;

    @Autowired
    private DragonDataService dragonDataService;


    @Test
    public void generateData() {

        List<DragonPhoenixVo> listResult=dragonDataService.getDragonAndPhoenIndexList();
        List<DragonPhoenixStaticVo> bottomStatic= dragonDataService.getDragonBottomStatics(listResult);
        listResult.forEach(s->{
            System.out.println(s);
        });
        bottomStatic.forEach(s->{
            System.out.println(s);
        });


    }
  //根据上一条记录来设置 主要处理字段值no开头的计数问题
    private void setCurRecordByPreRecord(DragonPhoenixVo prev,
                                         DragonPhoenixVo now) {
        //如果当前值为no.且上一个也为no。则在上一个计数基础上加1
        if (now.getDragonSingle().startsWith("no")){
            if (prev.getDragonSingle().startsWith("no")){
                String value=prev.getDragonSingle().split(",")[1];
                int times=Integer.valueOf(value)+1;
                now.setDragonSingle("no,"+times);
            }
        }
        if (now.getDragonDouble().startsWith("no")){
            if (prev.getDragonDouble().startsWith("no")){
                String value=prev.getDragonDouble().split(",")[1];
                int times=Integer.valueOf(value)+1;
                now.setDragonDouble("no,"+times);
            }
        }
        if (now.getPhoenSingle().startsWith("no")){
            if (prev.getPhoenSingle().startsWith("no")){
                String value=prev.getPhoenSingle().split(",")[1];
                int times=Integer.valueOf(value)+1;
                now.setPhoenSingle("no,"+times);
            }
        }
        if (now.getPhoenDouble().startsWith("no")){
            if (prev.getPhoenDouble().startsWith("no")){
                String value=prev.getPhoenDouble().split(",")[1];
                int times=Integer.valueOf(value)+1;
                now.setPhoenDouble("no,"+times);
            }
        }
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
        Integer head=dragonDataService.getMinValueFromStrLists(Lists.newArrayList(origin.getPrizeNo01(),origin.getPrizeNo02(),origin.getPrizeNo03()));
        //凤尾
        Integer tail=dragonDataService.getMaxValueFromStrLists(Lists.newArrayList(origin.getPrizeNo01(),origin.getPrizeNo02(),origin.getPrizeNo03()));
        if (head%2==0){
            //当期龙头出双，龙头 单  累计一次没出
            dragonPhoenixVo.setDragonDouble("ok,双");
            dragonPhoenixVo.setDragonSingle("no,1");
        }else {
            dragonPhoenixVo.setDragonSingle("ok,单");
            dragonPhoenixVo.setDragonDouble("no,1");
        }
        if (tail%2==0){
            dragonPhoenixVo.setPhoenDouble("ok,双");
            dragonPhoenixVo.setPhoenSingle("no,1");
        }else {
            dragonPhoenixVo.setPhoenSingle("ok,单");
            dragonPhoenixVo.setPhoenDouble("no,1");
        }
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

}
