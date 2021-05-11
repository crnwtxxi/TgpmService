package com.example.tgpmsystem.pojo;

public class TeaResponse {

    private String teaTno;
    private String teaName;
    private String teaSex;
    private String teaCollege;
    private String teaDirection;
    private String teaRank;
    private String teaEmail;
    private String teaId;
    private String teaStatus;

    @Override
    public String toString() {
        return "TeaResponse{" +
                "teaTno='" + teaTno + '\'' +
                ", teaName='" + teaName + '\'' +
                ", teaSex='" + teaSex + '\'' +
                ", teaCollege='" + teaCollege + '\'' +
                ", teaDirection='" + teaDirection + '\'' +
                ", teaRank='" + teaRank + '\'' +
                ", teaEmail='" + teaEmail + '\'' +
                ", teaId='" + teaId + '\'' +
                ", teaStatus='" + teaStatus + '\'' +
                '}';
    }

    public String getTeaStatus() {
        return teaStatus;
    }

    public void setTeaStatus(String teaStatus) {
        this.teaStatus = teaStatus;
    }

    public String getTeaTno() {
        return teaTno;
    }

    public void setTeaTno(String teaTno) {
        this.teaTno = teaTno;
    }

    public String getTeaName() {
        return teaName;
    }

    public void setTeaName(String teaName) {
        this.teaName = teaName;
    }

    public String getTeaSex() {
        return teaSex;
    }

    public void setTeaSex(String teaSex) {
        this.teaSex = teaSex;
    }

    public String getTeaCollege() {
        return teaCollege;
    }

    public void setTeaCollege(String teaCollege) {
        this.teaCollege = teaCollege;
    }

    public String getTeaDirection() {
        return teaDirection;
    }

    public void setTeaDirection(String teaDirection) {
        this.teaDirection = teaDirection;
    }

    public String getTeaRank() {
        return teaRank;
    }

    public void setTeaRank(String teaRank) {
        this.teaRank = teaRank;
    }

    public String getTeaEmail() {
        return teaEmail;
    }

    public void setTeaEmail(String teaEmail) {
        this.teaEmail = teaEmail;
    }

    public String getTeaId() {
        return teaId;
    }

    public void setTeaId(String teaId) {
        this.teaId = teaId;
    }
}
