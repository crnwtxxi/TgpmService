package com.example.tgpmsystem.pojo;

import java.util.Date;
public class Plan {

  private String planId;
  private String planTitle;
  private Date planDate;
  private String planContent;
  private String planStatus;


  public String getPlanId() {
    return planId;
  }

  public void setPlanId(String planId) {
    this.planId = planId;
  }


  public String getPlanTitle() {
    return planTitle;
  }

  public void setPlanTitle(String planTitle) {
    this.planTitle = planTitle;
  }


  public Date getPlanDate() {
    return planDate;
  }

  public void setPlanDate(Date planDate) {
    this.planDate = planDate;
  }


  public String getPlanContent() {
    return planContent;
  }

  public void setPlanContent(String planContent) {
    this.planContent = planContent;
  }


  public String getPlanStatus() {
    return planStatus;
  }

  public void setPlanStatus(String planStatus) {
    this.planStatus = planStatus;
  }

}
