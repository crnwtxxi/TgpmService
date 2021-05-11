package com.example.tgpmsystem.pojo;

public class Test {

    private String id;
    private String content;
    private String stuSno;
    private String stuName;
    private String stuSex;
    private String stuCollege;
    private String stuProfess;
    private String stuType;
    private String stuDirection;
    private String teaId;
    private String stuEmail;
    private String stuId;

    @Override
    public String toString() {
        return "Test{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", stuSno='" + stuSno + '\'' +
                ", stuName='" + stuName + '\'' +
                ", stuSex='" + stuSex + '\'' +
                ", stuCollege='" + stuCollege + '\'' +
                ", stuProfess='" + stuProfess + '\'' +
                ", stuType='" + stuType + '\'' +
                ", stuDirection='" + stuDirection + '\'' +
                ", teaId='" + teaId + '\'' +
                ", stuEmail='" + stuEmail + '\'' +
                ", stuId='" + stuId + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStuSno() {
        return stuSno;
    }

    public void setStuSno(String stuSno) {
        this.stuSno = stuSno;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getStuSex() {
        return stuSex;
    }

    public void setStuSex(String stuSex) {
        this.stuSex = stuSex;
    }

    public String getStuCollege() {
        return stuCollege;
    }

    public void setStuCollege(String stuCollege) {
        this.stuCollege = stuCollege;
    }

    public String getStuProfess() {
        return stuProfess;
    }

    public void setStuProfess(String stuProfess) {
        this.stuProfess = stuProfess;
    }

    public String getStuType() {
        return stuType;
    }

    public void setStuType(String stuType) {
        this.stuType = stuType;
    }

    public String getStuDirection() {
        return stuDirection;
    }

    public void setStuDirection(String stuDirection) {
        this.stuDirection = stuDirection;
    }

    public String getTeaId() {
        return teaId;
    }

    public void setTeaId(String teaId) {
        this.teaId = teaId;
    }

    public String getStuEmail() {
        return stuEmail;
    }

    public void setStuEmail(String stuEmail) {
        this.stuEmail = stuEmail;
    }

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }
}
