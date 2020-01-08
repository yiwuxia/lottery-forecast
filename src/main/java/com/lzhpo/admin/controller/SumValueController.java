package com.lzhpo.admin.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Splitter;
import com.lzhpo.admin.entity.vo.SelectCondition;
import com.lzhpo.common.config.MySysUser;
import com.lzhpo.core.config.RedisUtil;
import com.lzhpo.core.domain.concord.SumDataStaticsVo;
import com.lzhpo.core.domain.concord.SumDataVo;
import com.lzhpo.core.domain.dragon.DragonPhoenixStaticVo;
import com.lzhpo.core.domain.dragon.DragonPhoenixVo;
import com.lzhpo.core.service.DragonDataService;
import com.lzhpo.core.service.PrizeDataService;
import com.lzhpo.core.service.SumValueDataService;
import com.lzhpo.core.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Set;

/**
 * <p> Author：lzhpo </p>
 * <p> Title：</p>
 * <p> Description：和合值</p>
 */
@Controller
@RequestMapping("/sum")
public class SumValueController {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SumValueDataService sumValueDataService;





    @GetMapping(value = "/index")
    public ModelAndView adminIndex() {
        //龙头凤尾显示页面统计数据（不包括最后三列）
        List<SumDataVo> listResult = sumValueDataService.getSumValueIndexList();
        List<SumDataStaticsVo> bottomStatic = sumValueDataService.getSumValueBottomStatics(listResult);
        ModelAndView mv = new ModelAndView();
        mv.addObject("lists", listResult);
        mv.addObject("statics", bottomStatic);
        mv.setViewName("admin/stat/sumValue");
        return mv;
    }


    @PostMapping("/dealWithSumValue")
    @ResponseBody
    public JsonResp getTrandIndexCodeData(
           String   sumValues,
           String  leftPass,
           String    break1,
           String   rightPass,
           String   break2,
           String  fall,
           String  occurs
    ) {
        String preTermSumValueStr=redisUtil.get(RedisConstant.NEWST_PRIZE_DATA_SUM_VALUE);
        int preTermSumValue=0;
        if (StringUtils.isBlank(preTermSumValueStr)){
            preTermSumValue=Integer.valueOf(preTermSumValueStr);
        }
        saveSumValueInfoToRedis(sumValues,leftPass,break1,
                rightPass,break2,fall,occurs,preTermSumValue,null);
        return JsonResp.success("");
    }

    private void saveSumValueInfoToRedis(String sumValues,String leftPass,String break1,
                                         String rightPass,String break2,String fall,String occurs,
                                          int preTermSumValue,
                                           String uuid
    ) {

        String conditionId="";
        Set<String> result= CalculateUtil.calcSumValue(sumValues,leftPass,break1,
                rightPass,break2,fall,preTermSumValue,occurs);
        System.out.println(result.size());
       /* String conditionId="";
        Set<String> result= CalculateUtil.calcSumValue(sumValues,leftPass,break1,
                rightPass,break2,fall,preTermSumValue,occurs);
        SelectCondition condition=new SelectCondition();
        if (StringUtils.isBlank(uuid)){
            conditionId=System.currentTimeMillis()+"";
        }else {
            conditionId=uuid;
        }
        condition.setType(ConditionEnum.DRAGONPHOEN.getLabel());
        condition.setCount(result.size());
        condition.setId(conditionId);
        StringBuffer showText=new StringBuffer();
        StringBuffer headAndTailBuffer=new StringBuffer();
        //headAndTail
        Splitter splitter=Splitter.on(";");
        List<String> headAndTailList= splitter.splitToList("");
        String head=headAndTailList.get(0);
        String tail=headAndTailList.get(1);
        String occus=headAndTailList.get(2);
        if (StringUtils.isNotBlank(head)){
            headAndTailBuffer.append(CalculateUtil.dragonHeadAndTail.get(head))
                    .append(",");
        }
        if (StringUtils.isNotBlank(tail)){
            headAndTailBuffer.append(CalculateUtil.dragonHeadAndTail.get(tail));
        }
        String htTemp1=headAndTailBuffer.toString();
        if (htTemp1.length()>0){
            if (htTemp1.endsWith(",")){
                htTemp1=htTemp1.substring(0,htTemp1.length()-1);
            }
            showText.append("头尾质合").append(":").append(htTemp1);
            showText.append(";").append("出:").append(occus);
        }
        if (StringUtils.isNotBlank(headArea)){
            showText.append(";头:").append(headArea).append("路");
        }
        if (StringUtils.isNotBlank(tailArea)){
            showText.append(";尾:").append(tailArea).append("路");
        }
        if (StringUtils.isNotBlank(area0)){
            showText.append(";0路个数:").append(area0);
        }
        if (StringUtils.isNotBlank(area1)){
            showText.append(";1路个数:").append(area1);
        }
        if (StringUtils.isNotBlank(area2)){
            showText.append(";2路个数:").append(area2);
        }
        System.out.println(showText);
        condition.setContent(showText.toString());
        redisUtil.hset(RedisConstant.USER_UUID_SET+MySysUser.id(),
                condition.getId(),
                JSON.toJSONString(result));
        redisUtil.hset(RedisConstant.USER_CONDITION+MySysUser.id(),
                condition.getId(),
                JSON.toJSONString(condition));
        StringBuffer conditionDeal=new StringBuffer();
        conditionDeal.append(MyStrUtil.joinMultiStrBySemi(
                String.valueOf(ConditionEnum.DRAGONPHOEN.getIndex()),
                // headAndTail,
                MyStrUtil.joinMultiStrByLine(head,tail,occus),
                headArea,tailArea,area0,area1,area2,occurs
        ));
        redisUtil.hset(RedisConstant.USER_CONDITION_INFO+MySysUser.id(),
                condition.getId(),
                conditionDeal.toString());*/

    }


/*
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private DragonDataService dataService;


    @Autowired
    private PrizeDataService prizeDataService;


    @GetMapping(value = "/index")
    public ModelAndView adminIndex() {
        //龙头凤尾显示页面统计数据（不包括最后三列）
        List<DragonPhoenixVo> listResult = dataService.getDragonAndPhoenIndexList();
        List<DragonPhoenixStaticVo> bottomStatic = dataService.getDragonBottomStatics(listResult);
        ModelAndView mv = new ModelAndView();
        mv.addObject("lists", listResult);
        mv.addObject("statics", bottomStatic);
        mv.setViewName("admin/stat/dragon");
        return mv;
    }





    @PostMapping("/dragonConditionChange")
    @ResponseBody
    public JsonResp dragonConditionChange(
            String   headAndTail,
            String   headArea,
            String  tailArea,
            String  area0,
            String   area1,
            String  area2,
            String   occurs,
            String   uuid
    ) {
        //总的条件必须选一个
        if (StringUtils.isNotBlank(occurs)){
            prizeDataService.deleteTrendConditionById(uuid);
            saveDragonAndPhoenToRedis(headAndTail,headArea,tailArea,area0,area1,area2,occurs,uuid);
        }
        return JsonResp.success("");
    }*/

}