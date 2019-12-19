package com.lzhpo.core.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author <a href="lijing1@wxchina.com@wxchina.com">Lijin</a>
 * @Description TODO
 * @Date 2019/12/19 15:25
 * @Version 1.0
 **/
@Configuration
public class DbSourceConfig {


    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DruidDataSource druidDataSource(){
        return new DruidDataSource();
    }


    @Bean
    public JdbcTemplate template(){
        return new JdbcTemplate(druidDataSource());
    }

    @Bean
    public MyDbHelper dbHelper(){
        return new MyDbHelper(template());
    }



}
