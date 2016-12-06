package com.xuebang.o2o.business.dao;

import com.xuebang.o2o.business.entity.SysnKnowledge;
import com.xuebang.o2o.core.repository.GenericDao;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Administrator on 2016/7/27.
 */
public interface SysnKnowledgeDao extends GenericDao<SysnKnowledge,Long> {

    @Query(value = "select * from sync_knowledge where parent_Id = ?1",nativeQuery = true)
    public List<SysnKnowledge> findChild(Long parent);
}
