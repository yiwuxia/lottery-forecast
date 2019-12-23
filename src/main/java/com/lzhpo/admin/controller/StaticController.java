package com.lzhpo.admin.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.lzhpo.admin.entity.vo.ShowMenuVo;
import com.lzhpo.admin.service.MenuService;
import com.lzhpo.admin.service.UserService;
import com.lzhpo.common.annotation.SysLog;
import com.lzhpo.common.config.MySysUser;
import com.lzhpo.common.exception.UserTypeAccountException;
import com.lzhpo.common.realm.AuthRealm;
import com.lzhpo.common.util.Constants;
import com.lzhpo.common.util.ResponseEntity;
import com.lzhpo.core.domain.PrizeDetailVo;
import com.lzhpo.core.domain.PrizeStaticVo;
import com.lzhpo.core.domain.PrizeVo;
import com.lzhpo.core.service.PrizeDataService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

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
        List<PrizeVo> prizeVoList=prizeDataService.getPrizeVoList();
        List<PrizeDetailVo> prizeDetailVoList=prizeDataService.transPirzeVoToDetailVo(prizeVoList);
        ModelAndView mv=new ModelAndView();
        //列表数据 暂时没有统计数据
        mv.addObject("result",prizeDetailVoList);
        //出现次数，最大连出，最大遗漏
        List<PrizeStaticVo> statics=prizeDataService.getButtomPrizeStatics(prizeDetailVoList);
        mv.setViewName("admin/stat/trend");
        mv.addObject("statics",statics);
        return mv;
    }



}
