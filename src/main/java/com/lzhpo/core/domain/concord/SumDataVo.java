package com.lzhpo.core.domain.concord;

import lombok.Data;
import lombok.ToString;

/**
 * @author <a href="lijing1@wxchina.com@wxchina.com">Lijin</a>
 * @Description TODO
 * @Date 2020/1/7 11:43
 * @Version 1.0
 **/
@Data
@ToString
public class SumDataVo {


    private Integer id;
    private String termNo;
    /**
     * 前3 开奖号码
     */
    private String prizeNo01;
    private String prizeNo02;
    private String prizeNo03;


    /**
     * 合值走势
     */

    private String[] rightValueArr;//10
    /**
     * 合传断
     */
    private String leftPass;//左传
    private String break1;//断
    private String rightPass;//右传

    /**
     * 合断落
     */
    private String break2;//断
    private String fall;//落
}
