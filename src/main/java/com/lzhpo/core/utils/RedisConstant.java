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

    //页面显示的condition
    public static  final  String  USER_CONDITION="user_condition";
    //计算需要的condtion
    public static  final  String  USER_CONDITION_INFO="user_condition_info";

    /**
     * 存放当期的中奖数据，产生数据时更新
     */
    public static  final  String  CUR_TERM_PRIZE_DATA="cur_term_prize_data";


    public static  final  String  NEWST_PRIZE_DATA_SUM_VALUE="newst_prize_data_sum_value";

    public static void main(String[] args) {
        System.out.println("index_combination_result".toUpperCase());
    }

}
