package com.xuebang.o2o.business.service.impl;

import com.xuebang.o2o.business.entity.OperateRecord;
import com.xuebang.o2o.business.service.realm.RealmUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.xuebang.o2o.business.service.OperateRecordService;
import com.xuebang.o2o.business.dao.OperateRecordDao;
import com.xuebang.o2o.core.service.GenericServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by 代码生成工具 on 2015-4-26 17:09:26
 */
@Service
@Transactional
public class OperateRecordServiceImpl extends GenericServiceImpl<OperateRecord,Long> implements OperateRecordService {

    @Autowired
    private OperateRecordDao operateRecordDao;

    /**
     * 记录操作
     *
     * @param name
     * @param val
     */
    @Override
    public void record(String name, String val) {
        OperateRecord record = new OperateRecord();
        record.setName(name);
        record.setVal(val);
        record.setCreateUser(RealmUtils.getCurrentUserId());
        record.setCreateTime(new Date());
        operateRecordDao.save(record);
    }
}
