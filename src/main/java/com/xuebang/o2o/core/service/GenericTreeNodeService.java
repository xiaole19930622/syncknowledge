package com.xuebang.o2o.core.service;

import com.xuebang.o2o.core.vo.ITreeNode;
import com.xuebang.o2o.core.vo.TreeNode;

import java.io.Serializable;
import java.util.List;

/**
 * 树接口
 * Created by xuwen on 2015/4/26.
 */
public interface GenericTreeNodeService<T extends ITreeNode<T>,ID extends Serializable> extends GenericService<T,ID> {

    /**
     * 从根节点递归获取所有children
     * 数据量较大的实体请避免调用此函数，否则可能导致内存不足
     * 为避免循环引用，parent节点中children属性为空，children节点中parent属性为空
     * @return
     */
    List<TreeNode> getNodes();

    /**
     * 根据ID递归所有
     * @param id
     * @return
     */
    List<TreeNode> getNodes(ID id);

    /**
     * 从根节点指定层级递归获取children
     * @param level
     * @return
     */
    List<TreeNode> getNodes(int level);

    /**
     * 根据ID递归该ID指定层级的parent节点和children节点
     * @param id
     * @param level
     * @return
     */
    List<TreeNode> getNodes(ID id,int level);

    /**
     * 根据ID获取子节点数组
     * @param id
     * @return
     */
    public List<TreeNode> getChildren(ID id);

}
