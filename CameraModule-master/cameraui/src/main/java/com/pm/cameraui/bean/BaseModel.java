package com.pm.cameraui.bean;

/**
 * @author ch
 * @date 2020/5/25-17:24
 * @desc
 */
public class BaseModel<T> {
    private int code;
    private String msg;
    private T data;

    public BaseModel(int errorCode, String errorMsg, T data) {
        this.code = errorCode;
        this.msg = errorMsg;
        this.data = data;
    }

    public BaseModel() {
    }
    public int getErrorCode() {
        return code;
    }

    public void setErrorCode(int errorCode) {
        this.code = errorCode;
    }

    public String getErrorMsg() {
        return msg;
    }

    public void setErrorMsg(String errorMsg) {
        this.msg = errorMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
