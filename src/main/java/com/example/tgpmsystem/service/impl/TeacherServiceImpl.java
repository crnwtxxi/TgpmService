package com.example.tgpmsystem.service.impl;

import com.example.tgpmsystem.mapper.StudentMapper;
import com.example.tgpmsystem.pojo.StuResponse;
import com.example.tgpmsystem.pojo.TeaResponse;
import com.example.tgpmsystem.response.ResponseResult;
import com.example.tgpmsystem.service.LoginService;
import com.example.tgpmsystem.service.TeacherService;
import com.example.tgpmsystem.utils.TextUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


@Service
public class TeacherServiceImpl implements TeacherService {

    @Resource
    private LoginService loginService;

    @Resource
    private StudentMapper studentMapper;

    private HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getRequest();
    }

    private HttpServletResponse getResponse() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getResponse();
    }

    @Override
    public ResponseResult getAllSelfStu(Integer pageNum, Integer pageSize) {
        //检查登录状况
        TeaResponse teacher = loginService.judgeTeacher(getRequest(), getResponse());
        if (TextUtils.isNull(teacher)) {
            return ResponseResult.FAILED("账号未登录");
        }
        //登录了
        String currentTeaId = teacher.getTeaId();
        //分页
        PageHelper.startPage(pageNum, pageSize);
        List<StuResponse> selfStuList = studentMapper.selectStuByTeaId(currentTeaId);
        PageInfo<StuResponse> pageInfo = new PageInfo<>(selfStuList);
        return ResponseResult.SUCCESS("学生获取成功").setData(pageInfo);
    }

    @Override
    public ResponseResult getAllCampusStu() {
        List<StuResponse> allStuList = studentMapper.findAll();
        return ResponseResult.SUCCESS("获取成功").setData(allStuList);
    }

}
