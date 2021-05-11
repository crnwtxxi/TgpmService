package com.example.tgpmsystem.service.impl;

import com.example.tgpmsystem.mapper.TeacherMapper;
import com.example.tgpmsystem.pojo.StuResponse;
import com.example.tgpmsystem.pojo.TeaResponse;
import com.example.tgpmsystem.response.ResponseResult;
import com.example.tgpmsystem.service.LoginService;
import com.example.tgpmsystem.service.StudentService;
import com.example.tgpmsystem.utils.TextUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Resource
    private TeacherMapper teacherMapper;

    @Resource
    private LoginService loginService;


    private HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getRequest();
    }

    private HttpServletResponse getResponse() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getResponse();
    }

    @Override
    public ResponseResult getMyTutor() {
        //检查登录状况
        StuResponse student = loginService.judgeStudent(getRequest(), getResponse());
        if (TextUtils.isNull(student)) {
            //token过期，登录超时，需要重新登录
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        //登陆了
        //查询出自己的导师的信息
        TeaResponse teacher = teacherMapper.selectByStuId(student.getStuId());
        List<TeaResponse> tutorList = new ArrayList<>();
        //如果teacher为空，则代表还未选择导师
        if (!TextUtils.isNull(teacher)) {
            tutorList.add(teacher);
        }
        return ResponseResult.SUCCESS("获取导师信息成功").setData(tutorList);
    }
}
