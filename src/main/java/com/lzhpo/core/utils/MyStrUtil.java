package com.lzhpo.core.utils;

import com.google.common.base.Joiner;

/**
 * 处理字符串的各种操作
 */
public class MyStrUtil {

    public static String  joinMultiStrBySemi(String ... arr) {
        Joiner joiner = Joiner.on(";").skipNulls();
        return  joiner.join(arr);
    }

    public static String  joinMultiStrByComma(String ... arr) {
        Joiner joiner = Joiner.on(",").skipNulls();
        return  joiner.join(arr);
    }

    public static void main(String[] args) {
        System.out.println(joinMultiStrBySemi("","b","","c"));
    }

}
