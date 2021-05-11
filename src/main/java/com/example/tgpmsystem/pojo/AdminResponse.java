package com.example.tgpmsystem.pojo;

public class AdminResponse {

    private String adminId;
    private String adminAno;
    private String adminCollege;

    @Override
    public String toString() {
        return "AdminResponse{" +
                "adminId='" + adminId + '\'' +
                ", adminAno='" + adminAno + '\'' +
                ", adminCollege='" + adminCollege + '\'' +
                '}';
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getAdminAno() {
        return adminAno;
    }

    public void setAdminAno(String adminAno) {
        this.adminAno = adminAno;
    }

    public String getAdminCollege() {
        return adminCollege;
    }

    public void setAdminCollege(String adminCollege) {
        this.adminCollege = adminCollege;
    }
}
