package com.xuebang.o2o.business.service.impl;

import com.xuebang.o2o.business.dao.UserDao;
import com.xuebang.o2o.business.dao.UserInformationDao;
import com.xuebang.o2o.business.entity.User;
import com.xuebang.o2o.business.entity.UserInformation;
import com.xuebang.o2o.business.service.UserSerivce;
import com.xuebang.o2o.business.service.realm.RealmUtils;
import com.xuebang.o2o.core.exception.ServiceException;
import com.xuebang.o2o.core.service.GenericServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by xuwen on 2015/3/29.
 */
@Service
@Transactional
public class UserServiceImpl extends GenericServiceImpl<User, String> implements UserSerivce {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserInformationDao userInformationDao;

    /**
     * 查询贡献最多的3个人
     *
     * @return
     */
    @Override
    public List<User> getContributionTop3() {
        PageRequest pageRequest = new PageRequest(0, 6, new Sort(Sort.Direction.DESC, "score"));
        return userDao.getContributionTop3();
    }

    @Override
    public List<UserInformation> findUserInfo(String id) {

        return userInformationDao.findByUserId(id);
    }

    public User findByUsername(String username){
     return    userDao.findByUsername(username);
    }
    @Override
    public void adminScore(User user, long score) {
        if (score < 0) {
            throw new ServiceException("用户积分不能小于0");
        }
        user.setScore(score);
        userDao.save(user);
    }

    @Override
    public Page<User> findUserListPage(final Specification<User> specification, PageRequest pageRequest, final User entity) {
        return super.findPage(new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<Predicate>();
                list.add(specification.toPredicate(root, criteriaQuery, criteriaBuilder));
                // 超级管理员看到所有数据，非超级管理员只能看到所属组织架构及以下数据（组织架构归属分公司以下的管理员提升至分公司架构及以下数据）
                if (!SecurityUtils.getSubject().hasRole("超级管理员")) {
                    String orgLevel = userDao.findOne(RealmUtils.getCurrentUserId()).getOrganization().getOrgLevel();
                    // 如果是分公司以下组织架构的则提升到分公司角度查询数据
                    if (orgLevel.length() > 8) {
                        orgLevel = orgLevel.substring(0, 8);
                    }
                    list.add(criteriaBuilder.like(root.<String>get("organization").<String>get("orgLevel"), orgLevel + "%"));
                }
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        }, pageRequest, entity);
    }


    /**
     * 保存
     *
     * @param entity
     */
    @Override
    public void save(User entity) {
        if(entity.getId() == null){
            String salt = UUID.randomUUID().toString();
//            entity.setPassword(entryptPassword(entity.getPassword(),salt));
        }
        super.save(entity);
    }

    /**
     * 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
     */
    private String entryptPassword(String password,String salt) {
        SimpleHash hash = new SimpleHash(HASH_ALGORITHM,password,salt,HASH_INTERATIONS);
        return hash.toHex();
    }

    public static void main(String[] args) {
        System.out.println(new UserServiceImpl().entryptPassword("admin","admin"));
    }

}
