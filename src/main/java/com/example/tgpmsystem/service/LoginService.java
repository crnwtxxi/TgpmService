package com.example.tgpmsystem.service;

import com.example.tgpmsystem.pojo.*;
import com.example.tgpmsystem.response.ResponseResult;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 核验数据的接口
 */

@Service
public interface LoginService {

    ResponseResult checkLogined(HttpServletRequest request, HttpServletResponse response);

    ResponseResult logout(HttpServletRequest request, HttpServletResponse response);

    ResponseResult studentLogin(Student student, HttpServletResponse response);
    StuResponse checkStuLogin(HttpServletRequest request, HttpServletResponse response);
    StuResponse judgeStudent(HttpServletRequest request, HttpServletResponse response);

    ResponseResult teacherLogin(Teacher teacher, HttpServletResponse response);
    TeaResponse checkTeaLogin(HttpServletRequest request, HttpServletResponse response);
    TeaResponse judgeTeacher(HttpServletRequest request, HttpServletResponse response);

    ResponseResult adminLogin(Administrator admin, HttpServletResponse response);
    AdminResponse checkadminLogin(HttpServletRequest request, HttpServletResponse response);
    AdminResponse judgeAdmin(HttpServletRequest request, HttpServletResponse response);
}
