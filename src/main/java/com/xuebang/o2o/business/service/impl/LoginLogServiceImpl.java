package com.xuebang.o2o.business.service.impl;

import com.xuebang.o2o.business.entity.LoginLog;
import com.xuebang.o2o.business.entity.User;
import com.xuebang.o2o.business.service.LoginLogService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.xuebang.o2o.business.dao.LoginLogDao;
import com.xuebang.o2o.core.service.GenericServiceImpl;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by 代码生成工具 on Aug 10, 2015 5:13:30 PM
 */
@Service
@Transactional
public class LoginLogServiceImpl extends GenericServiceImpl<LoginLog,Long> implements LoginLogService {

    private static final Logger logger = LoggerFactory.getLogger(LoginLogServiceImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private LoginLogDao loginLogDao;

    @Override
    public void log(User user, String ipAddress) {
        LoginLog loginLog = new LoginLog();
        loginLog.setUser(user);
        loginLog.setIpAddress(ipAddress);
        loginLog.setLoginTime(new Date());
        loginLogDao.save(loginLog);
    }

    @Override
    public List<Map<String, Object>> totalBranchLogin(String startDate, String endDate, String branchId, String campusId) {
        StringBuffer sql = new StringBuffer();
        sql.append("select o.name, count(o.id) as count ");
        sql.append("from organization o ");
        sql.append("inner join ( ");
        sql.append("select substr(o.org_level, 1, 8) as org_level ");
        sql.append("from login_log ll inner join user u on ll.user = u.id ");
        sql.append("inner join organization o on u.organization = o.id ");
        sql.append("where 1 = 1 ");
        sql.append("and length(o.org_level) >= 8 ");
        if(StringUtils.isNotBlank(startDate)){
            sql.append("and ll.login_time >= '").append(startDate).append("' ");
        }
        if(StringUtils.isNotBlank(endDate)){
            sql.append("and ll.login_time <= '").append(endDate).append(" 23:59:59' ");
        }
        if(StringUtils.isNotBlank(branchId)){
            sql.append("and substr(o.org_level,1,8) = (select org_level from organization where id = '").append(branchId).append("')");
        }
        if(StringUtils.isNotBlank(campusId)){
            sql.append("and substr(o.org_level,1,12) = (select org_level from organization where id = '").append(campusId).append("')");
        }
        sql.append(") result ");
        sql.append("on o.org_level = result.org_level ");
        sql.append("group by o.id ");
        sql.append("order by count(o.id) desc ");
        logger.info(sql.toString());
        return jdbcTemplate.queryForList(sql.toString());
    }


    public List<String[]> exportBranchLogin(String startDate, String endDate, String branchId, String campusId){
        StringBuffer sql = new StringBuffer();
        sql.append("select o.name, count(o.id) as count ");
        sql.append("from organization o ");
        sql.append("inner join ( ");
        sql.append("select substr(o.org_level, 1, 8) as org_level ");
        sql.append("from login_log ll inner join user u on ll.user = u.id ");
        sql.append("inner join organization o on u.organization = o.id ");
        sql.append("where 1 = 1 ");
        sql.append("and length(o.org_level) >= 8 ");
        if(StringUtils.isNotBlank(startDate)){
            sql.append("and ll.login_time >= '").append(startDate).append("' ");
        }
        if(StringUtils.isNotBlank(endDate)){
            sql.append("and ll.login_time <= '").append(endDate).append(" 23:59:59' ");
        }
        if(StringUtils.isNotBlank(branchId)){
            sql.append("and substr(o.org_level,1,8) = (select org_level from organization where id = '").append(branchId).append("')");
        }
        if(StringUtils.isNotBlank(campusId)){
            sql.append("and substr(o.org_level,1,12) = (select org_level from organization where id = '").append(campusId).append("')");
        }
        sql.append(") result ");
        sql.append("on o.org_level = result.org_level ");
        sql.append("group by o.id ");
        sql.append("order by count(o.id) desc ");
        logger.info(sql.toString());
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sql.toString());
        List<String[]> dataList = new ArrayList<>();
        while(srs.next()){
            String[] data = new String[2];
            data[0] = srs.getString(1);
            data[1] = srs.getString(2);
            dataList.add(data);
        }
        return  dataList;
    }
}
