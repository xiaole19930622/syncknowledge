package com.xuebang.o2o.business.dao;

import com.xuebang.o2o.business.entity.Knowledge;
import com.xuebang.o2o.core.repository.GenericDao;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Administrator on 2016/7/27.
 */
public interface KnowledgeDao extends GenericDao<Knowledge,Long> {

    @Query(value = "select * from knowledge where parent_Id = ?1",nativeQuery = true)
    public List<Knowledge> findChild(Long parent);
}
