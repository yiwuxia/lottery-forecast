package com.lzhpo.core.domain.concord;

import com.lzhpo.core.domain.CommonVo;
import lombok.Data;

import java.util.Arrays;

/**
 * @author <a href="lijing1@wxchina.com@wxchina.com">Lijin</a>
 * @Description TODO
 * @Date 2020/1/10 13:47
 * @Version 1.0
 **/
@Data
public class BorderDataVo extends CommonVo {

    //8
    private String[] distanceArr;
    //8
    private String[] maxIntervalArr;
    //边临和 4
    private String[] borderSumArr;
    //5
    private String[] mixValuesArr;

   /* private String leftPass;
    private String break1;
    private String rightPass;
    private String break2;
    private String fall;*/

    @Override
    public String toString() {
        return "BorderDataVo{" +
                "distanceArr=" + Arrays.toString(distanceArr) +
                ", maxIntervalArr=" + Arrays.toString(maxIntervalArr) +
                ", borderSumArr=" + Arrays.toString(borderSumArr) +
                ", mixValuesArr=" + Arrays.toString(mixValuesArr) +
                '}';
    }
}
