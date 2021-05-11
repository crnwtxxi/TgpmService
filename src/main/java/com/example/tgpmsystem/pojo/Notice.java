package com.example.tgpmsystem.pojo;


import java.util.Date;

public class Notice {

  private String notiId;
  private String notiSendId;
  private String notiReciveId;
  private String notiContent;
  private Date notiTime;
  private String notiStatus;


  public String getNotiId() {
    return notiId;
  }

  public void setNotiId(String notiId) {
    this.notiId = notiId;
  }


  public String getNotiSendId() {
    return notiSendId;
  }

  public void setNotiSendId(String notiSendId) {
    this.notiSendId = notiSendId;
  }


  public String getNotiReciveId() {
    return notiReciveId;
  }

  public void setNotiReciveId(String notiReciveId) {
    this.notiReciveId = notiReciveId;
  }


  public String getNotiContent() {
    return notiContent;
  }

  public void setNotiContent(String notiContent) {
    this.notiContent = notiContent;
  }


  public Date getNotiTime() {
    return notiTime;
  }

  public void setNotiTime(Date notiTime) {
    this.notiTime = notiTime;
  }


  public String getNotiStatus() {
    return notiStatus;
  }

  public void setNotiStatus(String notiStatus) {
    this.notiStatus = notiStatus;
  }

}
