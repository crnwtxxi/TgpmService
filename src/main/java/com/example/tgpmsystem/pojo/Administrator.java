package com.example.tgpmsystem.pojo;


public class Administrator {

  private String adminId;
  private String adminAno;
  private String adminPwd;
  private String adminCollege;

  @Override
  public String toString() {
    return "Administrator{" +
            "adminId='" + adminId + '\'' +
            ", adminAno='" + adminAno + '\'' +
            ", adminPwd='" + adminPwd + '\'' +
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


  public String getAdminPwd() {
    return adminPwd;
  }

  public void setAdminPwd(String adminPwd) {
    this.adminPwd = adminPwd;
  }


  public String getAdminCollege() {
    return adminCollege;
  }

  public void setAdminCollege(String adminCollege) {
    this.adminCollege = adminCollege;
  }

}
