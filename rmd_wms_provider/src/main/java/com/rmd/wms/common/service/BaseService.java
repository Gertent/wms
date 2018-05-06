package com.rmd.wms.common.service;

import com.rmd.wms.db.DataSourceHolder;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class BaseService {

    @Resource(name = "sqlSession")
    private SqlSession sqlSessionTemplate;

    public SqlSession getSqlSession(String dataSource) {
        setDataSource(dataSource);
        return sqlSessionTemplate;
    }

    protected SqlSession getSqlSession() {
        return sqlSessionTemplate;
    }

    private void setDataSource(String dataSource) {
        DataSourceHolder.setDataSource(dataSource);
    }

    protected <T> T getMapper(Class<T> type) {
        return getSqlSession().getMapper(type);
    }
}
