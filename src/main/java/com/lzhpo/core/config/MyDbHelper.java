package com.lzhpo.core.config;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author <a href="lijing1@wxchina.com@wxchina.com">Lijin</a>
 * @Description TODO
 * @Date 2019/12/19 15:30
 * @Version 1.0
 **/
@Component
@AllArgsConstructor
public class MyDbHelper {


    private JdbcTemplate template;


    public List<String> queryForStrList(String sql){
        return template.queryForList(sql,String.class);
    }

    public List<String> queryForStrList(String sql,Object [] arr){
        return  template.queryForList(sql,arr,String.class);
    }

    public <T> T queryForObject(String sql,Class<T> t){
        return  template.queryForObject(sql,new BeanPropertyRowMapper<>(t));
    }

    public <T> T queryForObject(String sql,Class<T> t,Object[] arr){
        return  template.queryForObject(sql,new BeanPropertyRowMapper<>(t),arr);
    }

    public List<Integer> queryForIntList(String sql,Object [] arr){
        return  template.queryForList(sql,arr,Integer.class);
    }
    public <T> List<T> queryForObjList(String sql,Class<T> t){
        return  template.queryForList(sql,t);
    }

    public <T> List<T> queryForObjList(String sql,Class<T> t,Object[] arr){
        return   template.queryForList(sql,t,arr);
    }


}
