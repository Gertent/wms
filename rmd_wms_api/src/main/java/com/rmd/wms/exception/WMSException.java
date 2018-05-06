package com.rmd.wms.exception;

/**
 * Created by liu on 2017/3/7.
 */
public class WMSException extends RuntimeException {

    private String code;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * @see Exception#Exception()
     */
    public WMSException() {
        super();
    }

    /**
     * @param message 异常信息
     * @param cause   {@link Throwable}对象
     * @see Exception#Exception(String, Throwable)
     */
    public WMSException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message 异常信息
     * @see Exception#Exception(String)
     */
    public WMSException(String message) {
        super(message);
        setMsg(message);
    }

    /**
     * @param cause {@link Throwable}对象
     * @see Exception#Exception(Throwable)
     */
    public WMSException(Throwable cause) {
        super(cause);
    }

    /**
     * 自定义属性异常
     * @param code
     * @param msg
     */
    public WMSException(String code, String msg) {
        setCode(code);
        setMsg(msg);
    }

    /**
     * 自定义属性异常
     * @param code
     * @param msg
     */
    public WMSException(String code, String msg, String error) {
        super(error);
        setCode(code);
        setMsg(msg);
    }

    /**
     * 自定义属性异常
     * @param code
     * @param msg
     */
    public WMSException(String code, String msg, String error, Exception e) {
        super(error, e);
        setCode(code);
        setMsg(msg);
    }

}
