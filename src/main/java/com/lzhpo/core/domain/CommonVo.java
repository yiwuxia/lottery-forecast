package com.lzhpo.core.domain;

import lombok.Data;

/**
 * @author <a href="lijing1@wxchina.com@wxchina.com">Lijin</a>
 * @Description TODO
 * @Date 2020/1/10 13:44
 * @Version 1.0
 **/
@Data
public class CommonVo {


    private Integer id;
    private String termNo;
    /**
     * 前3 开奖号码
     */
    private String prizeNo01;
    private String prizeNo02;
    private String prizeNo03;

    
}
