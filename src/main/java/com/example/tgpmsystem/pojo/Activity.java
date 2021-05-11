package com.example.tgpmsystem.pojo;

import java.util.Date;
public class Activity {

  private String actiId;
  private String actiTitle;
  private Date actiDate;
  private String actiHost;
  private String actiType;
  private String actiForm;
  private String actiDesc;
  private String actiStatus;

  @Override
  public String toString() {
    return "Activity{" +
            "actiId='" + actiId + '\'' +
            ", actiTitle='" + actiTitle + '\'' +
            ", actiDate=" + actiDate +
            ", actiHost='" + actiHost + '\'' +
            ", actiType='" + actiType + '\'' +
            ", actiForm='" + actiForm + '\'' +
            ", actiDesc='" + actiDesc + '\'' +
            ", actiStatus='" + actiStatus + '\'' +
            '}';
  }

  public String getActiId() {
    return actiId;
  }

  public void setActiId(String actiId) {
    this.actiId = actiId;
  }


  public String getActiTitle() {
    return actiTitle;
  }

  public void setActiTitle(String actiTitle) {
    this.actiTitle = actiTitle;
  }


  public Date getActiDate() {
    return actiDate;
  }

  public void setActiDate(Date actiDate) {
    this.actiDate = actiDate;
  }


  public String getActiHost() {
    return actiHost;
  }

  public void setActiHost(String actiHost) {
    this.actiHost = actiHost;
  }


  public String getActiType() {
    return actiType;
  }

  public void setActiType(String actiType) {
    this.actiType = actiType;
  }


  public String getActiForm() {
    return actiForm;
  }

  public void setActiForm(String actiForm) {
    this.actiForm = actiForm;
  }


  public String getActiDesc() {
    return actiDesc;
  }

  public void setActiDesc(String actiDesc) {
    this.actiDesc = actiDesc;
  }


  public String getActiStatus() {
    return actiStatus;
  }

  public void setActiStatus(String actiStatus) {
    this.actiStatus = actiStatus;
  }

}
