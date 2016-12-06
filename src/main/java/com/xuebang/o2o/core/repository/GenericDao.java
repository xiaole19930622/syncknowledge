package com.xuebang.o2o.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * 通用CRUD Dao接口
 * Created by xuwen on 2015/3/28.
 */
@NoRepositoryBean
public interface GenericDao<T, ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
}
