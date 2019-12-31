package com.lzhpo.core.domain.dragon;

import lombok.Data;

/**
 * @author <a href="lijing1@wxchina.com@wxchina.com">Lijin</a>
 * @Description TODO
 * @Date 2019/12/31 15:10
 * 龙头凤尾页面显示数据
 * @Version 1.0
 **/
@Data
public class DragonPhoenixVo {

    private Integer id;
    private String termNo;
    /**
     * 前3 开奖号码
     */
    private String prizeNo01;
    private String prizeNo02;
    private String prizeNo03;
    /**
     * 龙头单双
     */
    private String dragonSingle;
    private String dragonDouble;
    /**
     * 凤尾单双
     */
    private String phoenSingle;
    private String phoenDouble;
    /**
     * 龙头质合
     */
    private String dragonPrime;
    private String dragonComposite;
    /**
     * 凤尾质合
     */
    private String phoenPrime;
    private String phoenComposite;

    /**
     * 龙头 0 1 2 路
     */
    private String dragonArea0;
    private String dragonArea1;
    private String dragonArea2;

    /**
     * 凤尾 0 1 2 路
     */
    private String phoenArea0;
    private String phoenArea1;
    private String phoenArea2;

    /**
     * 0路个数
     */
    private String area0Num0;
    private String area0Num1;
    private String area0Num2;
    private String area0Num3;

    /**
     * 1路个数
     */
    private String area1Num0;
    private String area1Num1;
    private String area1Num2;
    private String area1Num3;

    /**
     * 2路个数
     */
    private String area2Num0;
    private String area2Num1;
    private String area2Num2;
    private String area2Num3;







}
