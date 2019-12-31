package com.lzhpo.core.service;

import com.google.common.collect.Lists;
import com.lzhpo.common.config.MySysUser;
import com.lzhpo.core.config.RedisUtil;
import com.lzhpo.core.domain.*;
import com.lzhpo.core.utils.CalculateUtil;
import com.lzhpo.core.utils.RedisConstant;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.max;
import static java.util.Comparator.comparingInt;

/**
 * @author <a href="lijing1@wxchina.com@wxchina.com">Lijin</a>
 * @Description TODO
 * @Date 2019/12/20 13:10
 * @Version 1.0
 **/
@Service
public class DragonDataService {


    /**
     * 从字符串集合中找到最小的数
     * @param newArrayList
     * @return
     */
    public Integer getMinValueFromStrLists(List<String> newArrayList) {
       List<Integer> list=  newArrayList.stream().map(s->Integer.valueOf(s)).collect(Collectors.toList());
        return Collections.min(list);
    }

    /**
     * 从字符串集合中找到最大的数
     * @param newArrayList
     * @return
     */
    public Integer getMaxValueFromStrLists(List<String> newArrayList) {
        List<Integer> list=  newArrayList.stream().map(s->Integer.valueOf(s)).collect(Collectors.toList());
        return Collections.max(list);
    }
}
