package com.example.tgpmsystem.service.impl;

import com.example.tgpmsystem.mapper.ThesisMapper;
import com.example.tgpmsystem.pojo.Project;
import com.example.tgpmsystem.pojo.StuResponse;
import com.example.tgpmsystem.pojo.TeaResponse;
import com.example.tgpmsystem.pojo.Thesis;
import com.example.tgpmsystem.response.ResponseResult;
import com.example.tgpmsystem.service.LoginService;
import com.example.tgpmsystem.service.ThesisService;
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
public class ThesisServiceImpl implements ThesisService {

    @Resource
    private LoginService loginService;

    @Resource
    private ThesisMapper thesisMapper;

    private HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getRequest();
    }

    private HttpServletResponse getResponse() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getResponse();
    }

    @Override
    public ResponseResult getAllPassThesis(Integer pageNum, Integer pageSize) {
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
            List<Thesis> thesisList = thesisMapper.selectStatus1ByUserId(teacher.getTeaId());
            PageInfo<Thesis> pageInfo = new PageInfo<>(thesisList);
            return ResponseResult.SUCCESS("论文审核通过列表获取成功").setData(pageInfo);
        } else {
            //学生
            //在数据库中找到自己填报的所有活动中状态为1的
            PageHelper.startPage(pageNum, pageSize);
            List<Thesis> thesisList = thesisMapper.selectStatus1ByUserId(student.getStuId());
            PageInfo<Thesis> pageInfo = new PageInfo<>(thesisList);
            return ResponseResult.SUCCESS("论文审核通过列表获取成功").setData(pageInfo);
        }
    }

    @Override
    public ResponseResult getAllUnpassThesis(Integer pageNum, Integer pageSize) {
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
            List<Thesis> thesisList = thesisMapper.selectStatus02ByUserId(teacher.getTeaId());
            PageInfo<Thesis> pageInfo = new PageInfo<>(thesisList);
            return ResponseResult.SUCCESS("论文审核不通过列表获取成功").setData(pageInfo);
        } else {
            //学生
            //在数据库中找到自己填报的所有活动中状态为1的
            PageHelper.startPage(pageNum, pageSize);
            List<Thesis> thesisList = thesisMapper.selectStatus02ByUserId(student.getStuId());
            PageInfo<Thesis> pageInfo = new PageInfo<>(thesisList);
            return ResponseResult.SUCCESS("论文审核不通过列表获取成功").setData(pageInfo);
        }
    }
}
