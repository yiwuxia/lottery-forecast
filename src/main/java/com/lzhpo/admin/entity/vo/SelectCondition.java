package com.lzhpo.admin.entity.vo;


import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class SelectCondition  implements  Serializable{
    private String  type;//1 是胆码，2定位码
    private String  content;
    private Integer count;
    private String id;//uuid


}
