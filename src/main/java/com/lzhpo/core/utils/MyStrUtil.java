package com.lzhpo.core.utils;

import com.google.common.base.Joiner;

/**
 * 处理字符串的各种操作
 */
public class MyStrUtil {

    /**
     * 多个对象按;连接起来
     * @param arr
     * @return
     */
    public static String  joinMultiStrBySemi(Object ... arr) {
        Joiner joiner = Joiner.on(";").skipNulls();
        return  joiner.join(arr);
    }

    /**
     * 多个对象按,连接起来
     * @param arr
     * @return
     */
    public static String  joinMultiStrByComma(Object ... arr) {
        Joiner joiner = Joiner.on(",").skipNulls();
        return  joiner.join(arr);
    }

    /**
     * 多个对象按-连接起来
     * @param arr
     * @return
     */
    public static String  joinMultiStrByLine(Object ... arr) {
        Joiner joiner = Joiner.on("-").skipNulls();
        return  joiner.join(arr);
    }


    public static  String[] getInitSumValueArr(int length) {
        String [] sumValueArr =new String[length];
        for (int i = 0; i <sumValueArr.length ; i++) {
            sumValueArr[i]="no,1";
        }
        return sumValueArr;

    }

    public static void main(String[] args) {

    }

}
