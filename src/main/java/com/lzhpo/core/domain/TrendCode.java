package com.lzhpo.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author <a href="lijing1@wxchina.com@wxchina.com">Lijin</a>
 * @Description TODO
 * @Date 2019/12/26 14:41
 * @Version 1.0
 **/

@Data
public class TrendCode {
    private String valueComma;
    private String first;
    private String second;
    private String third;

    public TrendCode(String first, String second, String third,String valueComma) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.valueComma=valueComma;
    }
}
