package kj.x.recruit.model;

import java.io.Serializable;

public class ResponseModel<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private T data;
    private int status;
    private String msg;
    private String t;
    private int server;
    private String serverDesc;
    private String statusDesc;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public int getServer() {
        return server;
    }

    public void setServer(int server) {
        this.server = server;
    }

    public String getServerDesc() {
        return serverDesc;
    }

    public void setServerDesc(String serverDesc) {
        this.serverDesc = serverDesc;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public boolean isSuccess() {
        return status == 200;
    }
}
