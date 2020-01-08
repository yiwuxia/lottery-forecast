package com.lzhpo.admin.controller;

import com.alibaba.fastjson.JSON;
import com.lzhpo.admin.entity.vo.SelectCondition;
import com.lzhpo.common.config.MySysUser;
import com.lzhpo.core.config.RedisUtil;
import com.lzhpo.core.domain.concord.SumDataStaticsVo;
import com.lzhpo.core.domain.concord.SumDataVo;
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
           String  valueFirst,
           String    valueSecond,
           String  occurs
    ) {
        String preTermSumValueStr=redisUtil.get(RedisConstant.NEWST_PRIZE_DATA_SUM_VALUE);
        int preTermSumValue=0;
        if (StringUtils.isNotBlank(preTermSumValueStr)){
            preTermSumValue=Integer.valueOf(preTermSumValueStr);
        }
        saveSumValueInfoToRedis(sumValues,valueFirst,valueSecond,
                occurs,preTermSumValue,null);
        return JsonResp.success("");
    }

    private void saveSumValueInfoToRedis(String sumValues,
                                         String valueFirst,
                                         String valueSecond,
                                         String occurs,
                                          int preTermSumValue,
                                           String uuid
    ) {

        String conditionId="";
        Set<String> result= CalculateUtil.calcSumValue(sumValues,valueFirst,valueSecond,
                preTermSumValue,occurs);
        SelectCondition condition=new SelectCondition();
        if (StringUtils.isBlank(uuid)){
            conditionId=System.currentTimeMillis()+"";
        }else {
            conditionId=uuid;
        }
        condition.setType(ConditionEnum.SUMVALUE.getLabel());//类型说明
        condition.setCount(result.size());
        condition.setId(conditionId);
        StringBuffer showText=new StringBuffer();
        if(StringUtils.isNotBlank(sumValues)){
            showText.append("合值:"+sumValues).append(";");
        }
        if(StringUtils.isNotBlank(valueFirst)){
            showText.append("合传断:"+valueFirst).append(";");
        }
        if(StringUtils.isNotBlank(valueSecond)){
            showText.append("合断落:"+valueSecond).append(";");
        }
        showText.append("出:"+occurs);
        String desc=showText.toString();
        desc=desc.endsWith(";")?desc.substring(0,desc.length()-1):desc;
        condition.setContent(desc);
        //保存集合值
        redisUtil.hset(RedisConstant.USER_UUID_SET+MySysUser.id(),
                condition.getId(),
                JSON.toJSONString(result));
        //保存列表展示的条件值
        redisUtil.hset(RedisConstant.USER_CONDITION+MySysUser.id(),
                condition.getId(),
                JSON.toJSONString(condition));
        String conditionStr= MyStrUtil.joinMultiStrBySemi(sumValues,valueFirst,valueSecond,occurs);
        conditionStr=ConditionEnum.SUMVALUE.getIndex()+";"+conditionStr;
        redisUtil.hset(RedisConstant.USER_CONDITION_INFO+MySysUser.id(),
                condition.getId(),
                conditionStr);


    }




    @Autowired
    private PrizeDataService prizeDataService;



    @PostMapping("/sumValueConditionChange")
    @ResponseBody
    public JsonResp dragonConditionChange(
            String   sumValues,
            String  valueFirst,
            String    valueSecond,
            String  occurs,
            String   uuid
    ) {
        //总的条件必须选一个
        if (StringUtils.isNotBlank(occurs)){
            prizeDataService.deleteTrendConditionById(uuid);
            String preTermSumValueStr=redisUtil.get(RedisConstant.NEWST_PRIZE_DATA_SUM_VALUE);
            int preTermSumValue=0;
            if (StringUtils.isNotBlank(preTermSumValueStr)){
                preTermSumValue=Integer.valueOf(preTermSumValueStr);
            }
            saveSumValueInfoToRedis(sumValues,valueFirst,valueSecond,
                    occurs,preTermSumValue,uuid);
        }
        return JsonResp.success("");
    }

}
