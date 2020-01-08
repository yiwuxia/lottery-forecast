package com.lzhpo.admin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.common.collect.Lists;
import com.lzhpo.admin.entity.vo.SelectCondition;
import com.lzhpo.admin.service.MenuService;
import com.lzhpo.admin.service.UserService;
import com.lzhpo.common.config.MySysUser;
import com.lzhpo.core.config.RedisUtil;
import com.lzhpo.core.domain.PrizeDetailVo;
import com.lzhpo.core.domain.PrizeStaticVo;
import com.lzhpo.core.domain.PrizeVo;
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
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * <p> Author：lzhpo </p>
 * <p> Title：</p>
 * <p> Description：主要是基本走势</p>
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
     * 目前数据是每次请求实时计算出来的，其实可以在入库的时候计算好。有空再改 free to code
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
        //从字符串中解析出待处理数据
        List<Integer> region= CalculateUtil.intCommonsStrToList(regionsPredict);
        List<Integer> first= CalculateUtil.intCommonsStrToList(firstPredict);
        List<Integer> second= CalculateUtil.intCommonsStrToList(secondPredict);
        List<Integer> third= CalculateUtil.intCommonsStrToList(thirdPredict);
        List<Integer> regionOccurs= CalculateUtil.intCommonsStrToList(occurTimesRegion);
        List<Integer> occurs= CalculateUtil.intCommonsStrToList(occurTimes);
      //  List<SelectCondition> conditions=Lists.newArrayList();
        //生成胆码条件
        if (regionOccurs.size()>0){
           // SelectCondition selectCondition=
                    saveTrendRegionDataToRedis(region,regionOccurs, null);
          //  conditions.add(selectCondition);
        }
        if (occurs.size()>0){
          //  SelectCondition selectCondition=
                    saveTrendFirstSecondThirdDataToRedis(first,second,third,occurs, null);
           // conditions.add(selectCondition);
        }
        //将条件存到redis中。只有页面删除时才删除。
        return JsonResp.success("");
    }

    //修改时保持uuid不变
    private void saveTrendFirstSecondThirdDataToRedis(List<Integer> first,
                                                                 List<Integer> second,
                                                                 List<Integer> third,
                                                                 List<Integer> occurs, String uuid) {
        String conditionId="";
        Set<String> dingweima=  CalculateUtil.calcDingweiMa(first,second,third,occurs);
        SelectCondition condition=new SelectCondition();
        if (org.apache.commons.lang3.StringUtils.isBlank(uuid)){
            conditionId=System.currentTimeMillis()+"";
        }else {
            conditionId=uuid;
        }
        StringBuffer buffer=new StringBuffer();
        condition.setType(ConditionEnum.DINGWEIMA.getLabel());
        condition.setCount(dingweima.size());
        condition.setId(conditionId);
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
        //存放数据集
        redisUtil.hset(RedisConstant.USER_UUID_SET+MySysUser.id(),
                condition.getId(),
                JSON.toJSONString(dingweima));
        //将条件缓存起来
        redisUtil.hset(RedisConstant.USER_CONDITION+MySysUser.id(),
                condition.getId(),
                JSON.toJSONString(condition));
       // buffer=new StringBuffer();

       String  recordMainInfo= MyStrUtil.joinMultiStrBySemi(ConditionEnum.DINGWEIMA.getIndex(),
               StringUtils.collectionToCommaDelimitedString(first),
               StringUtils.collectionToCommaDelimitedString(second),
               StringUtils.collectionToCommaDelimitedString(third),
               StringUtils.collectionToCommaDelimitedString(occurs)
       );

        /*buffer.append(ConditionEnum.DINGWEIMA.getIndex());
        buffer.append(";");
        buffer.append(StringUtils.collectionToCommaDelimitedString(first));
        buffer.append(";");
        buffer.append(StringUtils.collectionToCommaDelimitedString(second));
        buffer.append(";");
        buffer.append(StringUtils.collectionToCommaDelimitedString(third));
        buffer.append(";");
        buffer.append(StringUtils.collectionToCommaDelimitedString(occurs));*/
     //   redisUtil.hset(RedisConstant.USER_CONDITION_INFO+MySysUser.id(),condition.getId(),buffer.toString());
        redisUtil.hset(RedisConstant.USER_CONDITION_INFO+MySysUser.id(),
                condition.getId(),recordMainInfo);
      //  return  condition;
    }

    /**
     * 根据胆码的基本信息，计算并保存胆码的一切缓存信息。
     * @param region
     * @param regionOccurs
     * @param uuid
     * @return
     */
    private SelectCondition saveTrendRegionDataToRedis(List<Integer> region,
                                                       List<Integer> regionOccurs,
                                                       String uuid) {

        String conditionId="";
        if (org.apache.commons.lang3.StringUtils.isBlank(uuid)){
            conditionId=System.currentTimeMillis()+"";
        }else {
            conditionId=uuid;
        }
        Set<String> danma=  CalculateUtil.calcDanMa(region,regionOccurs);
        SelectCondition condition=new SelectCondition();
        StringBuffer buffer=new StringBuffer();
        condition.setType(ConditionEnum.DANMA.getLabel());
        condition.setCount(danma.size());
        condition.setId(conditionId);
        buffer.append(org.springframework.util.StringUtils.collectionToCommaDelimitedString(region));
        buffer.append("  出");
        buffer.append(StringUtils.collectionToCommaDelimitedString(regionOccurs));
        condition.setContent(buffer.toString());
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
        return    condition;
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
       // conditionsResult.stream().sorted(Comparator.comparing(SelectCondition::getId));
        conditionsResult.sort((o1, o2) -> (int)(Long.valueOf(o1.getId()) - Long.valueOf(o2.getId())));
        conditionsResult.forEach(s->{
            System.out.println(s);
        });
        return JsonResp.success(conditionsResult);

    }

    @PostMapping("/delConditionById")
    @ResponseBody
    public JsonResp delConditionById(
            String id
    ) {
        prizeDataService.deleteTrendConditionById(id);
        return JsonResp.success("ok");
    }



    @PostMapping("/getConditionById")
    @ResponseBody
    public JsonResp getConditionById(
            String id
    ) {
        String conditionStr= redisUtil.hget(RedisConstant.USER_CONDITION_INFO+MySysUser.id(),id);
        System.out.println(conditionStr);
        return JsonResp.success(conditionStr);

    }

    @PostMapping("/danmaConditionChange")
    @ResponseBody
    public JsonResp danmaConditionChange(
          String   regions,
          String  occurs,
          String uuid
    ) {
        List<Integer>  regionsList=CalculateUtil.intCommonsStrToList(regions);
        List<Integer>  occursList=CalculateUtil.intCommonsStrToList(occurs);
        if (occursList.size()>0){
            //先删除再更新
            prizeDataService.deleteTrendConditionById(uuid);
            saveTrendRegionDataToRedis(regionsList,occursList,uuid);
        }
        return JsonResp.success("ok");
    }

    @PostMapping("/dingweimaConditionChange")
    @ResponseBody
    public JsonResp dingweimaConditionChange(
            String firstPredict,
            String secondPredict,
            String thirdPredict,
            String occurTimes,String uuid
    ) {
        List<Integer> first= CalculateUtil.intCommonsStrToList(firstPredict);
        List<Integer> second= CalculateUtil.intCommonsStrToList(secondPredict);
        List<Integer> third= CalculateUtil.intCommonsStrToList(thirdPredict);
        List<Integer> occurs= CalculateUtil.intCommonsStrToList(occurTimes);
        if (occurs.size()>0){
            //先删除再更新
            prizeDataService.deleteTrendConditionById(uuid);
            saveTrendFirstSecondThirdDataToRedis(first,second,third,occurs,uuid);
        }
        return JsonResp.success("ok");
    }
}
