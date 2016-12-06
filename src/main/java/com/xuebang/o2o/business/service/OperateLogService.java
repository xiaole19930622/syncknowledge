package com.xuebang.o2o.business.service;

import com.xuebang.o2o.business.entity.OperateLog;
import com.xuebang.o2o.core.service.GenericService;

import java.util.List;
import java.util.Map;

/**
 * Created by 代码生成工具 on 2015-7-18 20:30:48
 */
public interface OperateLogService extends GenericService<OperateLog,Long> {
    /**
     * 统计学科网点击数和平台搜索数
     * @return
     */
    public List<Map<String, Object>> o2oUsage(String startDate, String endDate, String branchId, String campusId);
}
