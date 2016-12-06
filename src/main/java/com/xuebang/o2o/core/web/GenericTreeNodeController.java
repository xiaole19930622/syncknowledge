package com.xuebang.o2o.core.web;

import com.xuebang.o2o.core.service.GenericTreeNodeService;
import com.xuebang.o2o.core.vo.ITreeNode;
import com.xuebang.o2o.core.vo.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 通用树Controller
 * Created by xuwen on 2015/4/27.
 */
public class GenericTreeNodeController<T extends ITreeNode<T>, ID extends Serializable> extends GenericRestController<T, ID> {

    @Autowired
    protected GenericTreeNodeService<T, ID> genericTreeNodeService;

    /**
     * 获取子节点数组
     *
     * @param id
     * @return
     */
    @RequestMapping("getChildren")
    @ResponseBody
    public Collection<TreeNode> getChildren(ID id) {
        return genericTreeNodeService.getChildren(id);
    }

    /**
     * 从顶级节点或指定ID节点迭代指定层数节点
     *
     * @param level
     * @return
     */
    @RequestMapping("getNodes")
    @ResponseBody
    public Collection<TreeNode> getNodes(ID id, @RequestParam(defaultValue = "0") int level) {
        if (id != null) {
            return genericTreeNodeService.getNodes(id, level);
        } else {
            return genericTreeNodeService.getNodes(level);
        }
    }

}
