package com.example.tgpmsystem.vo;

import com.example.tgpmsystem.pojo.Project;
import com.example.tgpmsystem.pojo.StuResponse;

import java.util.Date;
import java.util.List;

public class UploadProjVo {

    private String projTitle;
    private Date projOndate;
    private Date projOffdate;
    private String projType;
    private String projForm;
    private String projDesc;
    private List<String> studentList;

    @Override
    public String toString() {
        return "UploadProjVo{" +
                "projTitle='" + projTitle + '\'' +
                ", projOndate=" + projOndate +
                ", projOffdate=" + projOffdate +
                ", projType='" + projType + '\'' +
                ", projForm='" + projForm + '\'' +
                ", projDesc='" + projDesc + '\'' +
                ", studentList=" + studentList +
                '}';
    }

    public List<String> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<String> studentList) {
        this.studentList = studentList;
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


}
