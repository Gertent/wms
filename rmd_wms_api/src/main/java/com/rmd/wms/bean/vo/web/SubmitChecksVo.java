package com.rmd.wms.bean.vo.web;

import java.io.Serializable;
import java.util.List;

/**
 * 提报参数
 * @author : liu
 * @Date : 2017/4/25
 */
public class SubmitChecksVo implements Serializable {

    private static final long serialVersionUID = 2242918560298527471L;

    Integer userId;
    String userName;
    String desc;
    String fileUrls;
    List<Integer> ids;

    public SubmitChecksVo() {
    }

    public SubmitChecksVo(Integer userId, String userName, String desc, List<Integer> ids, String fileUrls) {
        this.userId = userId;
        this.userName = userName;
        this.desc = desc;
        this.ids = ids;
        this.fileUrls = fileUrls;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public String getFileUrls() {
        return fileUrls;
    }

    public void setFileUrls(String fileUrls) {
        this.fileUrls = fileUrls;
    }
}
