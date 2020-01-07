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
public class SumDataStaticsVo {



    /**
     * 合值走势
     */

    private Integer rightValue0;//10
    private Integer rightValue1;//10
    private Integer rightValue2;//10
    private Integer rightValue3;//10
    private Integer rightValue4;//10
    private Integer rightValue5;//10
    private Integer rightValue6;//10
    private Integer rightValue7;//10
    private Integer rightValue8;//10
    private Integer rightValue9;//10
    private Integer rightValue10;//10
    /**
     * 合传断
     */
    private Integer leftPass;//左传
    private Integer break1;//断
    private Integer rightPass;//右传

    /**
     * 合断落
     */
    private Integer break2;//断
    private Integer fall;//落
}
