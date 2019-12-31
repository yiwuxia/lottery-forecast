package com.lzhpo.lzhposhiro;

import com.google.common.collect.Lists;
import com.lzhpo.core.config.RedisUtil;
import com.lzhpo.core.domain.PrizeInfoEntity;
import com.lzhpo.core.domain.PrizeVo;
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

        //List<PrizeVo> prizeVoList = prizeDataService.getPrizeVoList();
        List<PrizeInfoEntity> remoteList = prizeDataService.queryPrizeDataLimit();
        DragonPhoenixVo dragonPhoenixVo=new DragonPhoenixVo();
        for (int i = 0; i < remoteList.size(); i++) {
            PrizeInfoEntity origin=remoteList.get(i);
            dragonPhoenixVo.setId(origin.getId());
            dragonPhoenixVo.setTermNo(origin.getTermNo());
            dragonPhoenixVo.setPrizeNo01(origin.getPrizeNo01());
            dragonPhoenixVo.setPrizeNo02(origin.getPrizeNo02());
            dragonPhoenixVo.setPrizeNo03(origin.getPrizeNo03());
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


        }


    }

}
