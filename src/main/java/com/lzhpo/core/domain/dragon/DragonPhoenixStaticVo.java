package com.lzhpo.core.domain.dragon;

import lombok.Data;
import lombok.ToString;

/**
 * @author <a href="lijing1@wxchina.com@wxchina.com">Lijin</a>
 * @Description TODO
 * @Date 2019/12/31 15:10
 * 龙头凤尾页面显示数据
 * @Version 1.0
 **/
@Data
@ToString
public class DragonPhoenixStaticVo {


    private String description;
    /**
     * 龙头质合
     */
    private Integer dragonPrime;
    private Integer dragonComposite;
    /**
     * 凤尾质合
     */
    private Integer phoenPrime;
    private Integer phoenComposite;

    /**
     * 龙头 0 1 2 路
     */
    private Integer dragonArea0;
    private Integer dragonArea1;
    private Integer dragonArea2;

    /**
     * 凤尾 0 1 2 路
     */
    private Integer phoenArea0;
    private Integer phoenArea1;
    private Integer phoenArea2;

    /**
     * 0路个数
     */
    private Integer area0Num0;
    private Integer area0Num1;
    private Integer area0Num2;
    private Integer area0Num3;

    /**
     * 1路个数
     */
    private Integer area1Num0;
    private Integer area1Num1;
    private Integer area1Num2;
    private Integer area1Num3;

    /**
     * 2路个数
     */
    private Integer area2Num0;
    private Integer area2Num1;
    private Integer area2Num2;
    private Integer area2Num3;


}
