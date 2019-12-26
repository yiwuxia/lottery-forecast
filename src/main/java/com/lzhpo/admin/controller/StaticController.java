package com.lzhpo.admin.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.lzhpo.admin.service.MenuService;
import com.lzhpo.admin.service.UserService;
import com.lzhpo.core.domain.PrizeDetailVo;
import com.lzhpo.core.domain.PrizeStaticVo;
import com.lzhpo.core.domain.PrizeVo;
import com.lzhpo.core.domain.TrendCode;
import com.lzhpo.core.service.PrizeDataService;
import com.lzhpo.core.utils.CommonResp;
import com.lzhpo.core.utils.JsonResp;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
     * 基本走势页面选择，后返回算法计算的数据
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
        System.out.println();
        //所有可能的组合
        List<String> combination=prizeDataService.calculateTrendIndexData(region,first,second,third,regionOccurs,occurs);
        //返回两个数据
        return JsonResp.success(combination);
    }



    /**
     * 基本走势页面选择，返回720中可能
     */
    @PostMapping("/getTrendFullData")
    @ResponseBody
    public CommonResp getTrandIndexCodeData(
    ) {
        //所有可能的组合
        List<String> combination=prizeDataService.getTrandIndexCodeData();
        Collections.sort(combination);
        List<TrendCode>  result=combination.stream().map(s->new TrendCode(s)).collect(Collectors.toList());
        return CommonResp.success(result);
    }

    private List<Integer> intCommonsStrToList(String regionsPredict) {
        List<Integer> list =new ArrayList<>();
        String [] arr= org.springframework.util.StringUtils.commaDelimitedListToStringArray(regionsPredict);
        for (String s : arr){
            list.add(Integer.valueOf(s));
        }
        return list;
    }





}
