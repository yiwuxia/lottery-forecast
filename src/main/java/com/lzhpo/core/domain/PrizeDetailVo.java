package com.lzhpo.core.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author <a href="lijing1@wxchina.com@wxchina.com">Lijin</a>
 * @Description TODO
 * @Date 2019/12/20 13:43
 * @Version 1.0
 **/

@Data
public class PrizeDetailVo {

    private Integer id;
    private String termNo;
    /**
     * 前3 开奖号码
     */
    private String prizeNo01;
    private String prizeNo02;
    private String prizeNo03;
    /**
     * 期码分布
     */
    private String numRegion01;
    private String numRegion02;
    private String numRegion03;
    private String numRegion04;
    private String numRegion05;
    private String numRegion06;
    private String numRegion07;
    private String numRegion08;
    private String numRegion09;
    private String numRegion10;

    /**
     * 第一位是哪个数
     */
    private String first01;
    private String first02;
    private String first03;
    private String first04;
    private String first05;
    private String first06;
    private String first07;
    private String first08;
    private String first09;
    private String first10;

    private String second01;
    private String second02;
    private String second03;
    private String second04;
    private String second05;
    private String second06;
    private String second07;
    private String second08;
    private String second09;
    private String second10;

    private String third01;
    private String third02;
    private String third03;
    private String third04;
    private String third05;
    private String third06;
    private String third07;
    private String third08;
    private String third09;
    private String third10;

    private Date openTime;







}
