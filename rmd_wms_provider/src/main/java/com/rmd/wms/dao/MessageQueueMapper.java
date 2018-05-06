package com.rmd.wms.dao;

import com.rmd.wms.bean.MessageQueue;

import java.util.List;

public interface MessageQueueMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MessageQueue record);

    int insertSelective(MessageQueue record);

    MessageQueue selectByPrimaryKey(Integer id);

    List<MessageQueue> selectByCriteria(MessageQueue record);

    int updateByPrimaryKeySelective(MessageQueue record);

    int updateByPrimaryKey(MessageQueue record);
}