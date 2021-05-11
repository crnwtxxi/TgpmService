package com.example.tgpmsystem.pojo;


public class Quota {

  private String quotaId;
  private String teaId;
  private long quotaAmount;
  private String quotaGrade;


  public String getQuotaId() {
    return quotaId;
  }

  public void setQuotaId(String quotaId) {
    this.quotaId = quotaId;
  }


  public String getTeaId() {
    return teaId;
  }

  public void setTeaId(String teaId) {
    this.teaId = teaId;
  }


  public long getQuotaAmount() {
    return quotaAmount;
  }

  public void setQuotaAmount(long quotaAmount) {
    this.quotaAmount = quotaAmount;
  }


  public String getQuotaGrade() {
    return quotaGrade;
  }

  public void setQuotaGrade(String quotaGrade) {
    this.quotaGrade = quotaGrade;
  }

}
