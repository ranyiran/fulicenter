package cn.ran.flicenter.bean;

import java.io.Serializable;

/**
 * Created by Ran on 2016/10/23.
 */
public class CartResultBean implements Serializable {
    private boolean success;

    private String msg;

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean getSuccess() {
        return this.success;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }
}
