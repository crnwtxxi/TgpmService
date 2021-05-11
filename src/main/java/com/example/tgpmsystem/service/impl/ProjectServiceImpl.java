package com.example.tgpmsystem.service.impl;

import com.example.tgpmsystem.mapper.ProjectMapper;
import com.example.tgpmsystem.pojo.Activity;
import com.example.tgpmsystem.pojo.Project;
import com.example.tgpmsystem.pojo.StuResponse;
import com.example.tgpmsystem.pojo.TeaResponse;
import com.example.tgpmsystem.response.ResponseResult;
import com.example.tgpmsystem.service.LoginService;
import com.example.tgpmsystem.service.ProjectService;
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
public class ProjectServiceImpl implements ProjectService {

    @Resource
    private LoginService loginService;

    @Resource
    private ProjectMapper projectMapper;

    private HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getRequest();
    }

    private HttpServletResponse getResponse() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getResponse();
    }

    @Override
    public ResponseResult getAllPassproj(Integer pageNum, Integer pageSize) {
        //检查登陆状况
        TeaResponse teacher = loginService.judgeTeacher(getRequest(), getResponse());
        StuResponse student = loginService.judgeStudent(getRequest(), getResponse());
        if (TextUtils.isNull(student) && TextUtils.isNull(teacher)) {
            return ResponseResult.FAILED("账号未登录");
        }
        //登录了
        if (!TextUtils.isNull(teacher)) {
            //老师
            //在数据库中找到自己填报的所有活动中状态为1的
            PageHelper.startPage(pageNum, pageSize);
            List<Project> projectList = projectMapper.selectStatus1ByTeaId(teacher.getTeaId());
            PageInfo<Project> pageInfo = new PageInfo<>(projectList);
            return ResponseResult.SUCCESS("项目审核通过列表获取成功").setData(pageInfo);
        } else {
            //学生
            //在数据库中找到自己填报的所有活动中状态为1的
            PageHelper.startPage(pageNum, pageSize);
            List<Project> projectList = projectMapper.selectStatus1ByStuId(student.getStuId());
            PageInfo<Project> pageInfo = new PageInfo<>(projectList);
            return ResponseResult.SUCCESS("项目审核通过列表获取成功").setData(pageInfo);
        }
    }

    @Override
    public ResponseResult getAllUnpassproj(Integer pageNum, Integer pageSize) {
        //检查登陆状况
        TeaResponse teacher = loginService.judgeTeacher(getRequest(), getResponse());
        StuResponse student = loginService.judgeStudent(getRequest(), getResponse());
        if (TextUtils.isNull(student) && TextUtils.isNull(teacher)) {
            return ResponseResult.FAILED("账号未登录");
        }
        //登录了
        if (!TextUtils.isNull(teacher)) {
            //老师
            //在数据库中找到自己填报的所有活动中状态为1的
            PageHelper.startPage(pageNum, pageSize);
            List<Project> projectList = projectMapper.selectStatus02ByTeaId(teacher.getTeaId());
            PageInfo<Project> pageInfo = new PageInfo<>(projectList);
            return ResponseResult.SUCCESS("项目审核不通过列表获取成功").setData(pageInfo);
        } else {
            //学生
            //在数据库中找到自己填报的所有活动中状态为1的
            PageHelper.startPage(pageNum, pageSize);
            List<Project> projectList = projectMapper.selectStatus02ByStuId(student.getStuId());
            PageInfo<Project> pageInfo = new PageInfo<>(projectList);
            return ResponseResult.SUCCESS("项目审核不通过列表获取成功").setData(pageInfo);
        }
    }
}
