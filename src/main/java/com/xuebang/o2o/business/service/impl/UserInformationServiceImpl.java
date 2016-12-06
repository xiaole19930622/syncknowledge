package com.xuebang.o2o.business.service.impl;

import com.xuebang.o2o.business.entity.UserInformation;
import org.springframework.beans.factory.annotation.Autowired;
import com.xuebang.o2o.business.service.UserInformationService;
import com.xuebang.o2o.business.dao.UserInformationDao;
import com.xuebang.o2o.core.service.GenericServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class UserInformationServiceImpl extends GenericServiceImpl<UserInformation,Long> implements UserInformationService {

    @Autowired
    private UserInformationDao userInformationDao;

}
