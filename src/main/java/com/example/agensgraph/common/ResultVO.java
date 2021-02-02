package com.example.agensgraph.common;

import java.io.Serializable;

public class ResultVO<T> implements Serializable {

    private String msg;

    private T data;

    private Boolean stauts;

    private String code="20000";

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Boolean getStauts() {
        return stauts;
    }

    public void setStauts(Boolean stauts) {
        this.stauts = stauts;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ResultVO(String msg, T data, Boolean stauts) {
        this.msg = msg;
        this.data = data;
        this.stauts = stauts;
    }

    public ResultVO(String msg, T data, Boolean stauts, String code) {
        this.msg = msg;
        this.data = data;
        this.stauts = stauts;
        this.code = code;
    }

}
