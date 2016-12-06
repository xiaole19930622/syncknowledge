package com.xuebang.o2o.core.service;

import com.xuebang.o2o.core.exception.ServiceException;
import com.xuebang.o2o.core.repository.GenericDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 通用CRUD Service实现
 * Created by xuwen on 2015/3/28.
 */
public abstract class GenericServiceImpl<T, ID extends Serializable> implements GenericService<T, ID> {

    @Autowired
    protected GenericDao<T, ID> genericDao;

    /**
     * 获取当前类泛型实体类型Class
     *
     * @return
     */
    @Override
    public Class<T> getEntityClass() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        return (Class) params[0];
    }

    /**
     * 提交缓存
     */
    @Override
    public void flush() {
        genericDao.flush();
    }

    /**
     * 动态条件分页查询
     *
     * @param specification
     * @param pageRequest
     * @return
     */
    @Override
    public Page<T> findPage(Specification<T> specification, PageRequest pageRequest, final T entity) {
        return genericDao.findAll(specification, pageRequest);
    }

    /**
     * 通过ID查找
     *
     * @param id
     * @return
     */
    @Override
    public T findById(ID id) {
        return genericDao.findOne(id);
    }

    /**
     * 通过ID获取
     * 直接初始化级联对象
     *
     * @param id
     * @return
     */
    @Override
    public T getById(ID id) {
        return genericDao.getOne(id);
    }

    /**
     * 通过动态条件查找
     *
     * @param specification
     * @return
     */
    @Override
    public T findBySpecification(Specification<T> specification) {
        return genericDao.findOne(specification);
    }

    /**
     * 通过ID判断是否存在
     *
     * @param id
     * @return
     */
    @Override
    public boolean exists(ID id) {
        return genericDao.exists(id);
    }

    /**
     * 查询数量
     *
     * @return
     */
    @Override
    public long count() {
        return genericDao.count();
    }

    /**
     * 动态条件查询数量
     *
     * @param specification
     * @return
     */
    @Override
    public long count(Specification<T> specification) {
        return genericDao.count(specification);
    }

    /**
     * 查询所有
     *
     * @return
     */
    @Override
    public List<T> findAll() {
        return genericDao.findAll();
    }

    /**
     * 动态条件查询所有
     *
     * @param specification
     * @return
     */
    @Override
    public List<T> findAll(Specification<T> specification) {
        return genericDao.findAll(specification);
    }

    /**
     * 保存
     *
     * @param entity
     */
    @Override
    public <S extends T> void save(S entity) throws ServiceException {
        genericDao.save(entity);
    }

    /**
     * 批量保存
     *
     * @param entities
     * @return
     */
    @Override
    public <S extends T> Iterable<S> save(Iterable<S> entities) {
        return genericDao.save(entities);
    }

    /**
     * 保存并立即提交缓存
     *
     * @param entity
     */
    @Override
    public void saveAndFlush(T entity) {
        genericDao.saveAndFlush(entity);
    }

    /**
     * 通过ID删除
     *
     * @param id
     */
    @Override
    public void delete(ID id) {
        genericDao.delete(id);
    }

    /**
     * 删除实体
     *
     * @param entity
     */
    @Override
    public void delete(T entity) {
        genericDao.delete(entity);
    }

    /**
     * 批量删除实体
     * 循环调用delete
     *
     * @param entities
     */
    @Override
    public void delete(Iterable<? extends T> entities) {
        genericDao.delete(entities);
    }

    /**
     * 批量删除实体
     * 通过SQL删除
     *
     * @param entities
     */
    @Override
    public void deleteInBatch(Iterable<T> entities) {
        genericDao.deleteInBatch(entities);
    }

    /**
     * 删除所有实体
     * 循环调用delete
     */
    @Override
    public void deleteAll() {
        genericDao.deleteAll();
    }

    /**
     * 删除所有实体
     * 通过SQL删除
     */
    @Override
    public void deleteAllInBatch() {
        genericDao.deleteAllInBatch();
    }
}
