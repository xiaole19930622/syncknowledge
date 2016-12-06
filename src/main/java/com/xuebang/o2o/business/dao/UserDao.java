package com.xuebang.o2o.business.dao;

import com.xuebang.o2o.business.entity.User;
import com.xuebang.o2o.core.repository.GenericDao;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by xuwen on 2015/3/29.
 */
public interface UserDao extends GenericDao<User, String> {

    @Query(value = "select u.* from document d inner join user u on u.id = d.create_user and d.check_status=1 group by u.id order by count(d.id) desc limit 6",nativeQuery = true)
    public List<User> getContributionTop3();


    @Query(value = "select * from user where username = ?1",nativeQuery = true)
    public User findByUsername(String username);



}
