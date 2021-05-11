package com.example.tgpmsystem.vo;

public class UpdatePwdVo {

    public String oldPwd;
    public String newPwd;

    @Override
    public String toString() {
        return "UpdatePwdVo{" +
                "oldPwd='" + oldPwd + '\'' +
                ", newPwd='" + newPwd + '\'' +
                '}';
    }

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }
}
