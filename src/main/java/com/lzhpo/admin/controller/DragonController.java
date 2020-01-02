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
import com.lzhpo.core.domain.dragon.DragonPhoenixStaticVo;
import com.lzhpo.core.domain.dragon.DragonPhoenixVo;
import com.lzhpo.core.service.DragonDataService;
import com.lzhpo.core.service.PrizeDataService;
import com.lzhpo.core.utils.CalculateUtil;
import com.lzhpo.core.utils.ConditionEnum;
import com.lzhpo.core.utils.JsonResp;
import com.lzhpo.core.utils.RedisConstant;
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

//import org.springframework.util.StringUtils;
//import org.apache.commons.lang3.StringUtils;

/**
 * <p> Author：lzhpo </p>
 * <p> Title：</p>
 * <p> Description：主要是基本走势</p>
 */
@Controller
@RequestMapping("/dragon")
public class DragonController {

    @Autowired
    private DragonDataService dataService;


    @GetMapping(value = "/index")
    public ModelAndView adminIndex() {
        //龙头凤尾显示页面统计数据（不包括最后三列）
        List<DragonPhoenixVo> listResult=dataService.getDragonAndPhoenIndexList();
        List<DragonPhoenixStaticVo> bottomStatic= dataService.getDragonBottomStatics(listResult);
        ModelAndView mv = new ModelAndView();
        mv.addObject("lists",listResult);
        mv.addObject("statics",bottomStatic);
        mv.setViewName("admin/stat/dragon");
        return mv;
    }


}
