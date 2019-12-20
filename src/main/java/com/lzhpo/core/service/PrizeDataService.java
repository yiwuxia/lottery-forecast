package com.lzhpo.core.service;

import com.lzhpo.core.domain.PrizeData;
import com.lzhpo.core.domain.PrizeInfoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="lijing1@wxchina.com@wxchina.com">Lijin</a>
 * @Description TODO
 * @Date 2019/12/20 13:10
 * @Version 1.0
 **/
@Service
public class PrizeDataService {


    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
     * 基本走势
     * 先获取最新的一条数据
     * @param prizeData
     */
    public void handlerOriginDataTrend(PrizeData prizeData) {
        //保存到数据库
        String sql=" insert into t_prize_base_info(term_no,prize_no1,prize_no2,prize_no3,prize_no4,prize_no5,prize_no6,prize_no7," +
                " prize_no8,prize_no9,prize_no10,open_time" +
                " )values(?,?,?,?,?,?,?,?,?,?,?,?)";
        String [] nums=prizeData.getPrizeNums();
        Object[] arr=new Object[] {
                prizeData.getTermNo(),nums[0],nums[1],nums[2],nums[3],nums[4],nums[5],nums[6],nums[7],
                nums[8],nums[9],prizeData.getOpenTIme()
        };
        jdbcTemplate.update(sql,arr);

    }


    public List<PrizeInfoEntity> queryPrizeDataLimit() {

        String sql="select id,term_no termNo,prize_no1 prizeNo01,prize_no2 prizeNo02,prize_no3 prizeNo03," +
                "prize_no4 prizeNo04," +
                "prize_no5 prizeNo05,prize_no6 prizeNo06,prize_no7 prizeNo07," +
                "prize_no8 prizeMo08,prize_no9 prizeNo09,prize_no10 prizeNo10,open_time prizeNo10 from  t_prize_base_info order by id desc limit 500";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(PrizeInfoEntity.class));

    }



}
