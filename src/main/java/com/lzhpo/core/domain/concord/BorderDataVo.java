package com.lzhpo.core.domain.concord;

import com.lzhpo.core.domain.CommonVo;
import lombok.Data;

/**
 * @author <a href="lijing1@wxchina.com@wxchina.com">Lijin</a>
 * @Description TODO
 * @Date 2020/1/10 13:47
 * @Version 1.0
 **/
@Data
public class BorderDataVo  extends CommonVo {

    private String [] distanceArr;
    private String [] maxIntervalArr;

    //边临和
    private String[] borderSumArr;

    private String leftPass;
    private String break1;
    private String rightPass;


    private String break2;
    private String fall;


}
