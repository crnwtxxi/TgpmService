package com.example.tgpmsystem.vo;

import java.util.Date;

public class OpenTutorVo {

    private Date alloOndate;
    private Date alloOffdate;
    private String alloGrade;
    private long quotaAmount;


    @Override
    public String toString() {
        return "OpenTutorVo{" +
                "alloOndate=" + alloOndate +
                ", alloOffdate=" + alloOffdate +
                ", alloGrade='" + alloGrade + '\'' +
                ", quotaAmount=" + quotaAmount +
                '}';
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

    public long getQuotaAmount() {
        return quotaAmount;
    }

    public void setQuotaAmount(long quotaAmount) {
        this.quotaAmount = quotaAmount;
    }
}
