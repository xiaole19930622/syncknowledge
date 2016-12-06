package com.xuebang.o2o.core.web;

import com.xuebang.o2o.core.repository.search.DynamicSpecifications;
import com.xuebang.o2o.core.repository.search.SearchFilter;
import com.xuebang.o2o.core.repository.search.SortObject;
import com.xuebang.o2o.core.service.GenericService;
import com.xuebang.o2o.utils.ServletUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 通用CRUD Controller
 * Created by xuwen on 2015/4/1.
 */
public abstract class GenericRestController<T, ID extends Serializable> {

    @Autowired
    protected GenericService<T, ID> genericService;

    /**
     * 通过ID查找
     *
     * @param id
     * @return
     */
    @RequestMapping("{id}")
    @ResponseBody
    public T findById(@PathVariable ID id) {
        return genericService.findById(id);
    }

    /**
     * 分页查询
     *
     * @param pageNumber 页码
     * @param pageSize   每页记录数
     * @param request    HttpRequest
     * @param sorts      排序对象
     * @return
     */
    @RequestMapping("page")
    @ResponseBody
    public Page<T> findPage(@RequestParam(defaultValue = "1") int pageNumber,
                            @RequestParam(defaultValue = "20") int pageSize,
                            SortObject sorts,
                            ServletRequest request,
                            T eneity) {
        // 多字段排序
        Sort sort = null;
        if (sorts.getSorts() != null) {
            for (int i = 0; i < sorts.getSorts().size(); i++) {
                if (StringUtils.isNotBlank(sorts.getSorts().get(i).getField())) {
                    Sort sortItem = "desc".equals(sorts.getSorts().get(i).getType().toLowerCase()) ? new Sort(Sort.Direction.DESC, sorts.getSorts().get(i).getField()) : new Sort(Sort.Direction.ASC, sorts.getSorts().get(i).getField());
                    sort = sort == null ? sortItem : sort.and(sortItem);
                }
            }
        }
        // 动态查询参数
        Map<String, Object> searchParams = ServletUtils.getParametersStartingWith(request, "search_");
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<T> spec = DynamicSpecifications.bySearchFilter(filters.values());
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize, sort);
        return genericService.findPage(spec, pageRequest, eneity);
    }

    /**
     * 查询数量
     *
     * @return
     */
    @RequestMapping("count")
    @ResponseBody
    public Response count() {
        return new Response(String.valueOf(genericService.count()));
    }

    /**
     * 查询所有
     *
     * @return
     */
    @RequestMapping("all")
    @ResponseBody
    List<T> findAll(ServletRequest request) {
        // 动态查询参数
        Map<String, Object> searchParams = ServletUtils.getParametersStartingWith(request, "search_");
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<T> spec = DynamicSpecifications.bySearchFilter(filters.values());
        return genericService.findAll(spec);
    }

    /**
     * 保存
     *
     * @param entity
     */
    @RequestMapping("save")
    @ResponseBody
    public <S extends T> Response save(S entity) {
        genericService.save(entity);
        return new Response("保存成功");
    }

    /**
     * 通过ID删除
     *
     * @param id
     */
    @RequestMapping("delete/{id}")
    @ResponseBody
    public Response delete(@PathVariable ID id) {
        genericService.delete(id);
        return new Response("删除成功");
    }

}
