package com.xuebang.o2o.core.repository.strategy;

import com.xuebang.o2o.core.vo.ITreeNode;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * 树型实体
 * Created by xuwen on 2015/5/11.
 */
@MappedSuperclass
public abstract class TreeEntityWithoutIdStrategy<T extends TreeEntityWithoutIdStrategy> implements Serializable, ITreeNode<T> {

    protected String id; // 树型ID

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
