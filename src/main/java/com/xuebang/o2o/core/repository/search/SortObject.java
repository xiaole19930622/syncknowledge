package com.xuebang.o2o.core.repository.search;

import com.xuebang.o2o.core.repository.search.SortItem;

import java.util.List;

/**
 * 排序对象
 * Created by xuwen on 2015/4/1.
 */
public class SortObject {

    private List<SortItem> sorts;

    public List<SortItem> getSorts() {
        return sorts;
    }

    public void setSorts(List<SortItem> sorts) {
        this.sorts = sorts;
    }
}
