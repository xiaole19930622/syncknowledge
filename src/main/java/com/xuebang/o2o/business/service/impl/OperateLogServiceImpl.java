package com.xuebang.o2o.business.service.impl;

import com.xuebang.o2o.business.dao.OperateLogDao;
import com.xuebang.o2o.business.entity.OperateLog;
import com.xuebang.o2o.business.service.OperateLogService;
import com.xuebang.o2o.core.service.GenericServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by 代码生成工具 on 2015-7-18 20:30:48
 */
@Service
@Transactional
public class OperateLogServiceImpl extends GenericServiceImpl<OperateLog, Long> implements OperateLogService {

    private static final Logger logger = LoggerFactory.getLogger(OperateLogServiceImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private OperateLogDao operateLogDao;

    @Override
    public List<Map<String, Object>> o2oUsage(String startDate, String endDate, String branchId, String campusId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT o.name, ");
        sql.append("SUM(IF(result.request_path = '/document/search', 1, 0)) AS o2o, ");
        sql.append("SUM(IF(result.request_path='/redirectXueYiYun', 1, 0)) AS xuekewang ");
        sql.append("FROM organization o INNER JOIN( ");
        sql.append("SELECT substr(o.org_level, 1, 8) AS org_level, ol.request_path AS request_path ");
        sql.append("FROM operate_log ol INNER JOIN user u ON ol.user= u.id INNER JOIN organization o ON u.organization= o.id ");
        sql.append("WHERE 1= 1 ");
        sql.append("AND length(org_level) >= 8 ");
        sql.append("AND ol.request_path IN ('/document/search', '/redirectXueYiYun') ");
        if(StringUtils.isNotBlank(startDate)){
            sql.append("and ol.request_time >= '").append(startDate).append("' ");
        }
        if(StringUtils.isNotBlank(endDate)){
            sql.append("and ol.request_time <= '").append(endDate).append(" 23:59:59' ");
        }
        if(StringUtils.isNotBlank(branchId)){
            sql.append("and substr(o.org_level,1,8) = (select org_level from organization where id = '").append(branchId).append("')");
        }
        if(StringUtils.isNotBlank(campusId)){
            sql.append("and substr(o.org_level,1,12) = (select org_level from organization where id = '").append(campusId).append("')");
        }
        sql.append(") result ON o.org_level=result.org_level ");
        sql.append("GROUP BY o.id");
        logger.info(sql.toString());
        return jdbcTemplate.queryForList(sql.toString());
    }
}
