package com.example.tgpmsystem.pojo;

import java.util.Date;

public class Announce {

  private String annoId;
  private String annoTitle;
  private String annoContent;
  private String annoCollege;
  private String annoSenderId;
  private Date annoDate;


  public String getAnnoId() {
    return annoId;
  }

  public void setAnnoId(String annoId) {
    this.annoId = annoId;
  }


  public String getAnnoTitle() {
    return annoTitle;
  }

  public void setAnnoTitle(String annoTitle) {
    this.annoTitle = annoTitle;
  }


  public String getAnnoContent() {
    return annoContent;
  }

  public void setAnnoContent(String annoContent) {
    this.annoContent = annoContent;
  }


  public String getAnnoCollege() {
    return annoCollege;
  }

  public void setAnnoCollege(String annoCollege) {
    this.annoCollege = annoCollege;
  }


  public String getAnnoSenderId() {
    return annoSenderId;
  }

  public void setAnnoSenderId(String annoSenderId) {
    this.annoSenderId = annoSenderId;
  }


  public Date getAnnoDate() {
    return annoDate;
  }

  public void setAnnoDate(Date annoDate) {
    this.annoDate = annoDate;
  }

}
