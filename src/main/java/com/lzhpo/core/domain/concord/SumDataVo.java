package com.lzhpo.core.domain.concord;

import com.lzhpo.core.domain.CommonVo;
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
public class SumDataVo extends CommonVo {

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
