package com.rmd.wms.service.impl;

import com.rmd.wms.bean.CheckUser;
import com.rmd.wms.common.service.BaseService;
import com.rmd.wms.dao.CheckUserMapper;
import com.rmd.wms.service.CheckUserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author : liu
 * @Date : 2017/4/18
 */
@Service("checkUserService")
public class CheckUserServiceImpl extends BaseService implements CheckUserService {

    @Override
    public List<CheckUser> selectByCheckNo(String checkNo) {
        return this.getMapper(CheckUserMapper.class).selectByCheckNo(checkNo);
    }

    @Override
    public List<CheckUser> selectByCriteria(Map<String, Object> map) {
        return this.getMapper(CheckUserMapper.class).selectByCriteria(map);
    }
}
