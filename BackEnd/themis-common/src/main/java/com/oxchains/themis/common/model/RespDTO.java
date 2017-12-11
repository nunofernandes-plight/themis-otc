package com.oxchains.themis.common.model;

import java.io.Serializable;

/**
 * ajax请求返回实体
 *
 * @author liuruichao
 * Created on 2016/11/29 18:01
 */
public class RespDTO<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private int status;

    private String message;

    private T data;

    private RespDTO() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> RespDTO<T> success(String message, T data) {
        RespDTO<T> resp = new RespDTO<>();
        resp.status = 0;
        resp.message = message;
        resp.data = data;
        return resp;
    }

    public static <T> RespDTO<T> success(T data) {
        return success("操作成功", data);
    }

    public static <T> RespDTO<T> success(String msg) {
        return success(msg, null);
    }

    public static <T> RespDTO<T> success() {
        return success(null);
    }
    
    public static <T> RespDTO<T> fail(String message) {
        RespDTO<T> resp = new RespDTO<>();
        resp.status = 1;
        resp.message = message;
        return resp;
    }

    public static <T> RespDTO<T> fail() {
        return fail("操作失败");
    }
    
    public static <T> RespDTO<T> fail(String message,int type) {
        RespDTO<T> resp = new RespDTO<>();
        resp.status = type;
        resp.message = message;
        return resp;
    }
}