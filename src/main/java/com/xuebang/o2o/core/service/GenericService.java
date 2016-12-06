package com.xuebang.o2o.core.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.List;

/**
 * 通用CRUD Service接口
 * Created by xuwen on 2015/3/28.
 */
public interface GenericService<T, ID extends Serializable> {

    /**
     * 获取当前类泛型实体类型Class
     *
     * @return
     */
    Class<T> getEntityClass();

    /**
     * 提交缓存
     */
    void flush();

    /**
     * 动态条件分页查询
     *
     * @param specification
     * @param pageRequest
     * @return
     */
    Page<T> findPage(Specification<T> specification, PageRequest pageRequest,final T entity);

    /**
     * 通过ID查找
     *
     * @param id
     * @return
     */
    T findById(ID id);

    /**
     * 通过ID获取
     * 直接初始化级联对象
     *
     * @param id
     * @return
     */
    T getById(ID id);

    /**
     * 通过动态条件查找
     *
     * @param specification
     * @return
     */
    T findBySpecification(Specification<T> specification);

    /**
     * 通过ID判断是否存在
     *
     * @param id
     * @return
     */
    boolean exists(ID id);

    /**
     * 查询数量
     *
     * @return
     */
    long count();

    /**
     * 动态条件查询数量
     *
     * @param specification
     * @return
     */
    long count(Specification<T> specification);

    /**
     * 查询所有
     *
     * @return
     */
    List<T> findAll();

    /**
     * 动态条件查询所有
     *
     * @return
     */
    List<T> findAll(Specification<T> specification);

    /**
     * 保存
     *
     * @param entity
     */
    <S extends T> void save(S entity);

    /**
     * 批量保存
     *
     * @param entities
     * @param <S>
     * @return
     */
    <S extends T> Iterable<S> save(Iterable<S> entities);

    /**
     * 保存并立即提交缓存
     *
     * @param entity
     */
    void saveAndFlush(T entity);

    /**
     * 通过ID删除
     *
     * @param id
     */
    void delete(ID id);

    /**
     * 删除实体
     *
     * @param entity
     */
    void delete(T entity);

    /**
     * 批量删除实体
     * 循环调用delete
     *
     * @param entities
     */
    void delete(Iterable<? extends T> entities);

    /**
     * 批量删除实体
     * 通过SQL删除
     *
     * @param entities
     */
    void deleteInBatch(Iterable<T> entities);

    /**
     * 删除所有实体
     * 循环调用delete
     */
    void deleteAll();

    /**
     * 删除所有实体
     * 通过SQL删除
     */
    void deleteAllInBatch();

}
