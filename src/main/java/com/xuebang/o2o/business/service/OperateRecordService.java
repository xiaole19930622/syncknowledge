package com.xuebang.o2o.business.service;

import com.xuebang.o2o.business.entity.OperateRecord;
import com.xuebang.o2o.core.service.GenericService;

/**
 * Created by 代码生成工具 on 2015-4-26 17:09:26
 */
public interface OperateRecordService extends GenericService<OperateRecord,Long> {

    /**
     * 记录操作
     *
     * @param name
     * @param val
     */
    public void record(String name, String val);

}
