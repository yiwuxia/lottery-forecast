package com.lzhpo.core.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * FileName: UserSexEnum
 * Author:   0685
 * Date:     2018/8/28 16:51
 * Description:
 */
public enum ConditionEnum {

    DANMA(1,"胆码"),
    DINGWEIMA(2,"定位码"),
    DRAGONPHOEN(3,"龙头凤尾"),
    SUMVALUE(4,"和合值")
    ;


    private int index;
    private String label;

    public static ConditionEnum getUserSex(Integer index){
        if(index!=null){
            for (ConditionEnum state : ConditionEnum.values()) {
                if (state.getIndex() == index) {
                    return state;
                }
            }
        }
        return null;
    }
    public static ConditionEnum getUserSex(String label){
        if(StringUtils.isNoneBlank(label)){
            for (ConditionEnum state : ConditionEnum.values()) {
                if (state.getLabel().equals(label)) {
                    return state;
                }
            }
        }
        return null;
    }


    ConditionEnum(int index, String label) {
        this.index = index;
        this.label = label;
    }

    public int getIndex() {
        return index;
    }

    public String getLabel() {
        return label;
    }
}
