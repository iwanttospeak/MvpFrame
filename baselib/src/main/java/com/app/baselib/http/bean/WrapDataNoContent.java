package com.app.baselib.http.bean;

/**
 * @author by Wang
 */

public class WrapDataNoContent<T> {
    private Integer errcode;
    private String message;

    public Integer getErrcode() {
        return errcode;
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
