package com.example.tgpmsystem.vo;

public class FailVerifyVo {

    private String id;
    private String reason;

    @Override
    public String toString() {
        return "FailVerifyVo{" +
                "id='" + id + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
