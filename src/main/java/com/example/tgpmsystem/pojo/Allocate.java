package com.example.tgpmsystem.pojo;


import java.util.Date;

public class Allocate {

  private String alloId;
  private String alloCollege;
  private String alloGrade;
  private Date alloOndate;
  private Date alloOffdate;

  @Override
  public String toString() {
    return "Allocate{" +
            "alloId='" + alloId + '\'' +
            ", alloCollege='" + alloCollege + '\'' +
            ", alloGrade='" + alloGrade + '\'' +
            ", alloOndate=" + alloOndate +
            ", alloOffdate=" + alloOffdate +
            '}';
  }

  public String getAlloId() {
    return alloId;
  }

  public void setAlloId(String alloId) {
    this.alloId = alloId;
  }


  public String getAlloCollege() {
    return alloCollege;
  }

  public void setAlloCollege(String alloCollege) {
    this.alloCollege = alloCollege;
  }


  public String getAlloGrade() {
    return alloGrade;
  }

  public void setAlloGrade(String alloGrade) {
    this.alloGrade = alloGrade;
  }


  public Date getAlloOndate() {
    return alloOndate;
  }

  public void setAlloOndate(Date alloOndate) {
    this.alloOndate = alloOndate;
  }


  public Date getAlloOffdate() {
    return alloOffdate;
  }

  public void setAlloOffdate(Date alloOffdate) {
    this.alloOffdate = alloOffdate;
  }

}
