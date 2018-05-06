package com.rmd.wms.bean.vo.app;

import java.io.Serializable;

/**
 * Created by liu on 2017/2/21.
 */
public class ServerStatus<T> implements Serializable {

    private Integer flag;
    private String status;
    private String message;
    private T result;

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
