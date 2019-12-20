package com.lzhpo.core.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author <a href="lijing1@wxchina.com@wxchina.com">Lijin</a>
 * @Description  响应原始数据
 * @Date 2019/12/19 15:15
 * @Version 1.0
 **/
@Data
public class PrizeData {

    private String termNo;//开奖号码 唯一

    private String [] prizeNums;

    private String type;//数据类型 可能是北京飞车或其他类型

    private Date openTIme;


}
