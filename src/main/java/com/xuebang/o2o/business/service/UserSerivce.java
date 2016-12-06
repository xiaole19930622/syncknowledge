package com.xuebang.o2o.business.service;

import com.xuebang.o2o.business.entity.User;
import com.xuebang.o2o.business.entity.UserInformation;
import com.xuebang.o2o.core.service.GenericService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Created by xuwen on 2015/3/29.
 */
public interface UserSerivce extends GenericService<User, String> {

    public static final String HASH_ALGORITHM = "SHA-1";

    public static final int HASH_INTERATIONS = 1024;

    public static final int SALT_SIZE = 32;

    /**
     * 查询贡献最多的3个人
     * @return
     */
    List<User> getContributionTop3();

    List<UserInformation> findUserInfo(String id);

    public User findByUsername(String username);

    /**
     * 管理员对用户积分操作
     * @param user
     * @param score
     */
    void adminScore(User user, long score);

    Page<User> findUserListPage(final Specification<User> specification, PageRequest pageRequest, final User entity);
}
