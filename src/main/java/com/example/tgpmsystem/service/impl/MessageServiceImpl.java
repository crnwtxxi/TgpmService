package com.example.tgpmsystem.service.impl;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.example.tgpmsystem.mapper.StudentMapper;
import com.example.tgpmsystem.mapper.TeacherMapper;
import com.example.tgpmsystem.pojo.AdminResponse;
import com.example.tgpmsystem.pojo.StuResponse;
import com.example.tgpmsystem.pojo.TeaResponse;
import com.example.tgpmsystem.response.ResponseResult;
import com.example.tgpmsystem.service.LoginService;
import com.example.tgpmsystem.service.MessageService;
import com.example.tgpmsystem.utils.TextUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class MessageServiceImpl implements MessageService {

    @Resource
    TeacherMapper teacherMapper;

    @Resource
    StudentMapper studentMapper;

    @Autowired
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
    public ResponseResult getAllTeaInfo(int pageNum, int pageSize) {
        //检查登录情况
        AdminResponse admin = loginService.judgeAdmin(getRequest(), getResponse());
        if (TextUtils.isNull(admin)) {
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        //登录了
        //检查数据是否为空
        if (TextUtils.isNull(pageNum) || TextUtils.isNull(pageSize)) {
            return ResponseResult.FAILED("数据为空，删除失败");
        }
        //去数据库中查出当前管理员所属学院的所有老师信息
        String college = admin.getAdminCollege();
        PageHelper.startPage(pageNum, pageSize);
        List<TeaResponse> allTeaList = teacherMapper.selectByCollege(college);
        PageInfo<TeaResponse> pageInfo = new PageInfo<>(allTeaList);
        return ResponseResult.SUCCESS("获取老师信息成功").setData(pageInfo);
    }

    @Override
    public ResponseResult deleteOneTea(String teaId) {
        //检查登录情况
        AdminResponse admin = loginService.judgeAdmin(getRequest(), getResponse());
        if (TextUtils.isNull(admin)) {
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        //登录了
        //检查数据是否为空
        if (TextUtils.isEmpty(teaId)) {
            return ResponseResult.FAILED("数据为空，删除失败");
        }
        //去数据库删除
        int deleteNum = teacherMapper.deleteById(teaId);
        if (deleteNum == 0) {
            return ResponseResult.FAILED("该用户不存在，删除失败");
        }
        return ResponseResult.SUCCESS("删除成功");
    }

    @Override
    public ResponseResult getAllStuInfo(int pageNum, int pageSize) {
        //检查登录情况
        AdminResponse admin = loginService.judgeAdmin(getRequest(), getResponse());
        if (TextUtils.isNull(admin)) {
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        //登录了
        //检查数据是否为空
        if (TextUtils.isNull(pageNum) || TextUtils.isNull(pageSize)) {
            return ResponseResult.FAILED("数据为空，删除失败");
        }
        //去数据库中查出当前管理员所属学院的所有老师信息
        String college = admin.getAdminCollege();
        PageHelper.startPage(pageNum, pageSize);
        List<StuResponse> allStuList = studentMapper.selectByCollege(college);
        PageInfo<StuResponse> pageInfo = new PageInfo<>(allStuList);
        return ResponseResult.SUCCESS("获取老师信息成功").setData(pageInfo);
    }

    @Override
    public ResponseResult deleteOneStu(String stuId) {
        //检查登录情况
        AdminResponse admin = loginService.judgeAdmin(getRequest(), getResponse());
        if (TextUtils.isNull(admin)) {
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        //登录了
        //检查数据是否为空
        if (TextUtils.isEmpty(stuId)) {
            return ResponseResult.FAILED("数据为空，删除失败");
        }
        //去数据库删除
        int deleteNum = studentMapper.deleteById(stuId);
        if (deleteNum == 0) {
            return ResponseResult.FAILED("该用户不存在，删除失败");
        }
        return ResponseResult.SUCCESS("删除成功");
    }


}
