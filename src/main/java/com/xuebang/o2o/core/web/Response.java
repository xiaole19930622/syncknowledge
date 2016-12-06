package com.xuebang.o2o.core.web;

/**
 * Rest返回对象
 * Created by xuwen on 2015/4/1.
 */
public class Response {

    private boolean ok = true; // 服务是否正常返回

    private String msg; // 返回消息

    public Response() {
        this.msg = "操作成功";
    }

    public Response(String msg) {
        this.msg = msg;
    }

    public Response(boolean ok, String msg) {
        this.ok = ok;
        this.msg = msg;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
