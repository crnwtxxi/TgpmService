package com.example.tgpmsystem.pojo;

import java.util.Date;
public class Project {

  private String projId;
  private String projTitle;
  private Date projOndate;
  private Date projOffdate;
  private String projType;
  private String projForm;
  private String projDesc;
  private String projStatus;


  public String getProjId() {
    return projId;
  }

  public void setProjId(String projId) {
    this.projId = projId;
  }


  public String getProjTitle() {
    return projTitle;
  }

  public void setProjTitle(String projTitle) {
    this.projTitle = projTitle;
  }


  public Date getProjOndate() {
    return projOndate;
  }

  public void setProjOndate(Date projOndate) {
    this.projOndate = projOndate;
  }


  public Date getProjOffdate() {
    return projOffdate;
  }

  public void setProjOffdate(Date projOffdate) {
    this.projOffdate = projOffdate;
  }


  public String getProjType() {
    return projType;
  }

  public void setProjType(String projType) {
    this.projType = projType;
  }


  public String getProjForm() {
    return projForm;
  }

  public void setProjForm(String projForm) {
    this.projForm = projForm;
  }


  public String getProjDesc() {
    return projDesc;
  }

  public void setProjDesc(String projDesc) {
    this.projDesc = projDesc;
  }


  public String getProjStatus() {
    return projStatus;
  }

  public void setProjStatus(String projStatus) {
    this.projStatus = projStatus;
  }

}
