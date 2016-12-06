package com.xuebang.o2o.business.service;

import com.xuebang.o2o.business.entity.LoginLog;
import com.xuebang.o2o.business.entity.User;
import com.xuebang.o2o.core.service.GenericService;

import java.util.List;
import java.util.Map;

/**
 * Created by 代码生成工具 on Aug 10, 2015 5:13:30 PM
 */
public interface LoginLogService extends GenericService<LoginLog,Long> {
    /**
     * 登陆日志
     * @param user
     * @param ipAddress
     */
    void log(User user, String ipAddress);

    /**
     * 统计分公司登陆人次
     * @param startDate
     * @param endDate
     * @return
     */
    public List<Map<String, Object>> totalBranchLogin(String startDate, String endDate, String branchId, String campusId);

    /**
     * 统计分公司登陆人次
     * @param startDate
     * @param endDate
     * @return
     */
    public List<String[]> exportBranchLogin(String startDate, String endDate, String branchId, String campusId);
}
