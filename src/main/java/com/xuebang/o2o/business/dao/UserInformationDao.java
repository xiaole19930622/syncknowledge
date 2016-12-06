package com.xuebang.o2o.business.dao;

import com.xuebang.o2o.business.entity.UserInformation;
import com.xuebang.o2o.core.repository.GenericDao;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserInformationDao extends GenericDao<UserInformation,Long> {
    @Query(value = "select u.* from user_information u where u.user= :id",nativeQuery = true)
    public List<UserInformation> findByUserId(@Param("id") String id);

    @Query(value = "select ui.grade from user_information ui where ui.user= :id",nativeQuery = true)
    public List<String> findGradeforUser(@Param("id") String id);
}
