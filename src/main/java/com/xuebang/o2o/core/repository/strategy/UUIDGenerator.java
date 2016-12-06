package com.xuebang.o2o.core.repository.strategy;

import com.xuebang.o2o.core.repository.entity.UUIDEntity;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.UUID;

/**
 * 自定义UUID策略
 * Created by xuwen on 2015/4/27.
 */
public class UUIDGenerator implements IdentifierGenerator {
    @Override
    public Serializable generate(SessionImplementor sessionImplementor, Object o) throws HibernateException {
        if(o instanceof UUIDEntity){
            UUIDEntity entity = (UUIDEntity) o;
            return StringUtils.isNotBlank(entity.getId()) ? entity.getId() : UUID.randomUUID().toString();
        }else{
            throw new HibernateException("使用自定义UUID策略的实体非UUIDEntity：" + o.getClass().getName());
        }
    }
}
