package com.lzhpo.core.domain;

import lombok.Data;

import java.util.Arrays;

@Data
public class PrizeVo {

    private Integer id;
    private String termNo;
    private String [] prizeNums;
    private String [] region;
    private String [] first;
    private String [] second;
    private String [] third;


    public PrizeVo(){
        this.prizeNums=new String[3];
        this.region=new String[10];
        this.first=new String[10];
        this.second=new String[10];
        this.third=new String [10];
    }


    @Override
    public String toString() {
        return
                " 期号=" + termNo  +
                "\t 前三位=" + Arrays.toString(prizeNums) +
                "\t 前三分布=" + Arrays.toString(region) +
                "\t 第一位=" + Arrays.toString(first) +
                "\t 第二位=" + Arrays.toString(second) +
                "\t 第三位=" + Arrays.toString(third) +
                '}';
    }
}
