package com.example.tgpmsystem.vo;

import com.example.tgpmsystem.pojo.StuResponse;

import java.util.List;

public class UploadPlanVo {

    private String planTitle;
    private String planContent;
    private List<StuResponse> studentList;

    @Override
    public String toString() {
        return "PlanVo{" +
                "planTitle='" + planTitle + '\'' +
                ", planContent='" + planContent + '\'' +
                ", studentList=" + studentList +
                '}';
    }

    public String getPlanTitle() {
        return planTitle;
    }

    public void setPlanTitle(String planTitle) {
        this.planTitle = planTitle;
    }

    public String getPlanContent() {
        return planContent;
    }

    public void setPlanContent(String planContent) {
        this.planContent = planContent;
    }

    public List<StuResponse> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<StuResponse> studentList) {
        this.studentList = studentList;
    }
}
