package com.lzhpo.core.utils;

public class RedisConstant {


    /**
     * 首页右侧组合到数据集合
     */
    public static  final  String  INDEX_COMBINATION_RESULT="index_combination_result";

    /**
     * hash .userid;uuid;list;
     * 每个用户每个条件对应的数据集合
     */
    public static  final  String  USER_UUID_SET="user_uuid_set";

    public static  final  String  USER_CONDITION="user_condition";

    

    public static void main(String[] args) {
        System.out.println("index_combination_result".toUpperCase());
    }

}
