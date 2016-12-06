package com.xuebang.o2o.core.service.impl;

import com.xuebang.o2o.core.repository.search.DynamicSpecifications;
import com.xuebang.o2o.core.vo.ITreeNode;
import com.xuebang.o2o.core.repository.search.SearchFilter;
import com.xuebang.o2o.core.service.GenericServiceImpl;
import com.xuebang.o2o.core.service.GenericTreeNodeService;
import com.xuebang.o2o.core.vo.TreeNode;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by xuwen on 2015/4/26.
 */
public class GenericTreeNodeServiceImpl<T extends ITreeNode<T>, ID extends Serializable> extends GenericServiceImpl<T, ID> implements GenericTreeNodeService<T, ID> {

    /**
     * 从根节点递归获取所有children
     * 数据量较大的实体请避免调用此函数，否则可能导致内存不足
     * 为避免循环引用，parent节点中children属性为空，children节点中parent属性为空
     * @return
     */
    @Override
    public List<TreeNode> getNodes() {
        return getNodes(Integer.MAX_VALUE);
    }

    /**
     * 根据ID递归所有
     * @param id
     * @return
     */
    @Override
    public List<TreeNode> getNodes(ID id) {
        return getNodes(id,Integer.MAX_VALUE);
    }

    /**
     * 从根节点指定层级递归获取children
     *
     * @param level
     * @return
     */
    @Override
    public List<TreeNode> getNodes(int level) {
        List<TreeNode> nodes = new ArrayList<>();
        List<SearchFilter> filters = new ArrayList<>();
        filters.add(new SearchFilter("id", SearchFilter.Operator.LT, 100));
        Specification<T> spec = DynamicSpecifications.bySearchFilter(filters);
        List<T> list = genericDao.findAll(spec);
        for (int i = 0; i < list.size(); i++) {
            nodes.add(new TreeNode(list.get(i), level));
        }
        Collections.sort(nodes);
        return nodes;
    }

    /**
     * 根据ID递归该ID指定层级的parent节点和children节点
     *
     * @param id
     * @param level
     * @return
     */
    @Override
    public List<TreeNode> getNodes(ID id, int level) {
        List<TreeNode> nodes = new ArrayList<>();
        T treeNode = genericDao.findOne(id);
        if(treeNode == null)return nodes;
        nodes.add(new TreeNode(treeNode,level));
        Collections.sort(nodes);
        return nodes;
    }

    /**
     * 根据ID获取子节点数组
     *
     * @param id
     * @return
     */
    @Override
    public List<TreeNode> getChildren(ID id) {
        List<TreeNode> list = getNodes(id, 1);
        if (list.isEmpty()) {
            return new ArrayList<>();
        } else {
            if(list.get(0).getChildren() != null){
                Collections.sort(list.get(0).getChildren());
            }
            return list.get(0).getChildren();
        }
    }
}
