package com.lzhpo.admin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lzhpo.admin.entity.vo.SelectCondition;
import com.lzhpo.admin.service.MenuService;
import com.lzhpo.admin.service.UserService;
import com.lzhpo.common.config.MySysUser;
import com.lzhpo.core.config.RedisUtil;
import com.lzhpo.core.domain.PrizeDetailVo;
import com.lzhpo.core.domain.PrizeStaticVo;
import com.lzhpo.core.domain.PrizeVo;
import com.lzhpo.core.domain.TrendCode;
import com.lzhpo.core.service.PrizeDataService;
import com.lzhpo.core.utils.*;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p> Author：lzhpo </p>
 * <p> Title：</p>
 * <p> Description：</p>
 */
@Controller
@RequestMapping("/stat")
public class StaticController {

    private final static Logger LOGGER = LoggerFactory.getLogger(StaticController.class);

    public final static String LOGIN_TYPE = "loginType";

    @Autowired
    @Qualifier("captchaProducer")
    DefaultKaptcha captchaProducer;

    @Autowired
    UserService userService;

    @Autowired
    MenuService menuService;

    @Autowired
    private PrizeDataService prizeDataService;

    @Autowired
    private RedisUtil redisUtil;

    public   static List<String> result720;


    @PostConstruct
    public void init(){
        result720=prizeDataService.getTrandIndexCodeData();
    }



    @GetMapping(value = "/trend")
    public ModelAndView adminIndex() {
        List<PrizeVo> prizeVoList = prizeDataService.getPrizeVoList();
        List<PrizeDetailVo> prizeDetailVoList = prizeDataService.transPirzeVoToDetailVo(prizeVoList);
        ModelAndView mv = new ModelAndView();
        //列表数据 暂时没有统计数据
        mv.addObject("result", prizeDetailVoList);
        //出现次数，最大连出，最大遗漏
        List<PrizeStaticVo> statics = prizeDataService.getButtomPrizeStatics(prizeDetailVoList);
        mv.setViewName("admin/stat/trend");
        mv.addObject("statics", statics);
        return mv;
    }


    /**
     * 基本走势页面选择，生成条件和各条件对应的集合。 (胆码和定位码)
     * 1，判断是否会产生条件
     * 2，如果会产生条件，生成条件对象，uuid为键，生成条件对应一个数据集
     * 每个用户的每个条件都会对应一份集合数据。
     *
     */
    @PostMapping("/getTrendCalcData")
    @ResponseBody
    public JsonResp getTrandIndexCodeData(
            String regionsPredict,
            String firstPredict,
            String secondPredict,
            String thirdPredict,
            String occurTimesRegion,
            String occurTimes
    ) {

        List<Integer> region= intCommonsStrToList(regionsPredict);
        List<Integer> first= intCommonsStrToList(firstPredict);
        List<Integer> second= intCommonsStrToList(secondPredict);
        List<Integer> third= intCommonsStrToList(thirdPredict);
        List<Integer> regionOccurs= intCommonsStrToList(occurTimesRegion);
        List<Integer> occurs= intCommonsStrToList(occurTimes);
        List<SelectCondition> conditions=Lists.newArrayList();
        //生成胆码条件
        if (regionOccurs.size()>0){
               Set<String> danma=  CalculateUtil.calcDanMa(region,regionOccurs);
                SelectCondition condition=new SelectCondition();
                StringBuffer buffer=new StringBuffer();
                condition.setType("胆码");
                condition.setCount(danma.size());
                condition.setId(System.currentTimeMillis()+"");
                buffer.append(org.springframework.util.StringUtils.collectionToCommaDelimitedString(region));
                buffer.append("  出");
                buffer.append(StringUtils.collectionToCommaDelimitedString(regionOccurs));
                condition.setContent(buffer.toString());
                conditions.add(condition);
                //将id对应的数据集合放入redis
                redisUtil.hset(RedisConstant.USER_UUID_SET+MySysUser.id(),
                        condition.getId(),
                        JSON.toJSONString(danma));
                // //将条件缓存起来
           // redisUtil.lSet(RedisConstant.USER_CONDITION+MySysUser.id(),JSON.toJSONString(condition));
            //table record show
                redisUtil.hset(RedisConstant.USER_CONDITION+MySysUser.id(),condition.getId(),JSON.toJSONString(condition));

                //将条件关键数值放入redis
                 buffer=new StringBuffer();
                buffer.append(ConditionEnum.DANMA.getIndex());
                buffer.append(";");
                buffer.append(StringUtils.collectionToCommaDelimitedString(region));
                buffer.append(";");
                buffer.append(StringUtils.collectionToCommaDelimitedString(regionOccurs));
                //pure data for deal
                redisUtil.hset(RedisConstant.USER_CONDITION_INFO+MySysUser.id(),condition.getId(),buffer.toString());

        }
        if (occurs.size()>0){
            Set<String> dingweima=  CalculateUtil.calcDingweiMa(first,second,third,occurs);
            SelectCondition condition=new SelectCondition();
            StringBuffer buffer=new StringBuffer();
            condition.setType("定位码");
            condition.setCount(dingweima.size());
            condition.setId(System.currentTimeMillis()+"");
            if (CollectionUtils.isNotEmpty(first)){
                buffer.append("第一位:").append(StringUtils.collectionToCommaDelimitedString(first));
            }
            if (CollectionUtils.isNotEmpty(second)){
                buffer.append("第二位:").append(StringUtils.collectionToCommaDelimitedString(second));
            }
            if (CollectionUtils.isNotEmpty(third)){
                buffer.append("第三位:").append(StringUtils.collectionToCommaDelimitedString(third));
            }
            buffer.append("  出");
            buffer.append(StringUtils.collectionToCommaDelimitedString(occurs));
            condition.setContent(buffer.toString());
            conditions.add(condition);
            redisUtil.hset(RedisConstant.USER_UUID_SET+MySysUser.id(),
                    condition.getId(),
                    JSON.toJSONString(dingweima));
            //将条件缓存起来
            redisUtil.hset(RedisConstant.USER_CONDITION+MySysUser.id(),condition.getId(),JSON.toJSONString(condition));
            buffer=new StringBuffer();
            buffer.append(ConditionEnum.DINGWEIMA.getIndex());
            buffer.append(";");
            buffer.append(StringUtils.collectionToCommaDelimitedString(first));
            buffer.append(";");
            buffer.append(StringUtils.collectionToCommaDelimitedString(second));
            buffer.append(";");
            buffer.append(StringUtils.collectionToCommaDelimitedString(third));
            buffer.append(";");
            buffer.append(StringUtils.collectionToCommaDelimitedString(occurs));
            redisUtil.hset(RedisConstant.USER_CONDITION_INFO+MySysUser.id(),condition.getId(),buffer.toString());

        }
        //将条件存到redis中。只有页面删除时才删除。
        return JsonResp.success(conditions);
    }








    private List<Integer> intCommonsStrToList(String regionsPredict) {
        List<Integer> list =new ArrayList<>();
        String [] arr= org.springframework.util.StringUtils.commaDelimitedListToStringArray(regionsPredict);
        for (String s : arr){
            list.add(Integer.valueOf(s));
        }
        return list;
    }



    @PostMapping("/getInitCombination")
    @ResponseBody
    public JsonResp getInitCombination(
    ) {
        List<String> response=Lists.newArrayList();
        List<List<String>> resultFromRedis=
                redisUtil.hListValues(RedisConstant.USER_UUID_SET+MySysUser.id());
        resultFromRedis.add(result720);
        CalculateUtil.findIntersectionNew(response,resultFromRedis);
        Collections.sort(response);
        return JsonResp.success(response);
    }


    /**
     * 首页加载时就获取已选都条件
     * @return
     */
    @PostMapping("/getConditions")
    @ResponseBody
    public JsonResp getConditions(
    ) {
        //公共的获取初始化组合集合
        List<String> conditions=
                redisUtil.hStrValues(RedisConstant.USER_CONDITION+MySysUser.id());
        List<SelectCondition> conditionsResult=Lists.newArrayList();
        for(String str:conditions){
            SelectCondition con= JSONObject.parseObject(str,SelectCondition.class);
            conditionsResult.add(con);
        }
        return JsonResp.success(conditionsResult);

    }

    @PostMapping("/delConditionById")
    @ResponseBody
    public JsonResp delConditionById(
            String id
    ) {
        redisUtil.hdel(RedisConstant.USER_UUID_SET+MySysUser.id(),id);
        redisUtil.hdel(RedisConstant.USER_CONDITION+MySysUser.id(),id);
        return JsonResp.success("ok");

    }

    @PostMapping("/getConditionById")
    @ResponseBody
    public JsonResp getConditionById(
            String id
    ) {
        String conditionStr= redisUtil.hget(RedisConstant.USER_CONDITION_INFO+MySysUser.id(),id);
        return JsonResp.success(conditionStr);

    }



}
