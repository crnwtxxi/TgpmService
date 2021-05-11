package com.example.tgpmsystem.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.tgpmsystem.mapper.AdminMapper;
import com.example.tgpmsystem.mapper.AnnounceMapper;
import com.example.tgpmsystem.mapper.StudentMapper;
import com.example.tgpmsystem.mapper.TeacherMapper;
import com.example.tgpmsystem.pojo.AdminResponse;
import com.example.tgpmsystem.pojo.Announce;
import com.example.tgpmsystem.pojo.StuResponse;
import com.example.tgpmsystem.pojo.TeaResponse;
import com.example.tgpmsystem.response.ResponseResult;
import com.example.tgpmsystem.service.CommonService;
import com.example.tgpmsystem.service.LoginService;
import com.example.tgpmsystem.utils.TextUtils;
import com.example.tgpmsystem.vo.UpdatePwdVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Service
public class CommonServiceImpl implements CommonService {

    @Resource
    private LoginService loginService;

    @Resource
    private AnnounceMapper announceMapper;

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private TeacherMapper teacherMapper;

    @Resource
    private AdminMapper adminMapper;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    private HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getRequest();
    }

    private HttpServletResponse getResponse() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getResponse();
    }

    @Override
    public ResponseResult getAllAnnounce(Integer pageNum, Integer pageSize) {
        //检查登录状况
        StuResponse student = loginService.judgeStudent(getRequest(), getResponse());
        TeaResponse teacher = loginService.judgeTeacher(getRequest(), getResponse());
        AdminResponse admin = loginService.judgeAdmin(getRequest(), getResponse());
        if (TextUtils.isNull(admin) && TextUtils.isNull(teacher) && TextUtils.isNull(student)) {
            return ResponseResult.FAILED("账号未登录");
        }
        //登录了
        if (!TextUtils.isNull(student)) {
            //学生
            //获取本学院的所有公告
            PageHelper.startPage(pageNum, pageSize);
            List<Announce> announceList = announceMapper.selectByCollege(student.getStuCollege());
            PageInfo<Announce> pageInfo = new PageInfo<>(announceList);
            return ResponseResult.SUCCESS("公告列表获取成功").setData(pageInfo);
        } else if (!TextUtils.isNull(teacher)) {
            //老师
            //获取本学院的所有公告
            PageHelper.startPage(pageNum, pageSize);
            List<Announce> announceList = announceMapper.selectByCollege(teacher.getTeaCollege());
            PageInfo<Announce> pageInfo = new PageInfo<>(announceList);
            return ResponseResult.SUCCESS("公告列表获取成功").setData(pageInfo);
        } else {
            //admin
            //获取本学院的所有公告
            PageHelper.startPage(pageNum, pageSize);
            List<Announce> announceList = announceMapper.selectByCollege(admin.getAdminCollege());
            PageInfo<Announce> pageInfo = new PageInfo<>(announceList);
            return ResponseResult.SUCCESS("公告列表获取成功").setData(pageInfo);
        }
    }

    @Override
    public ResponseResult updatePassword(UpdatePwdVo pwdVo) {
        //检查登录状况
        StuResponse student = loginService.judgeStudent(getRequest(), getResponse());
        TeaResponse teacher = loginService.judgeTeacher(getRequest(), getResponse());
        AdminResponse admin = loginService.judgeAdmin(getRequest(), getResponse());
        if (TextUtils.isNull(admin) && TextUtils.isNull(teacher) && TextUtils.isNull(student)) {
            return ResponseResult.FAILED("账号未登录");
        }
        //检查数据
        if (TextUtils.isEmpty(pwdVo.getOldPwd()) || TextUtils.isEmpty(pwdVo.getNewPwd())) {
            return ResponseResult.FAILED("存在数据空值");
        }
        //登录了
        if (!TextUtils.isNull(student)) {
            //学生
            //看旧密码是否匹配
            String cyptPwd = studentMapper.selectPwdById(student.getStuId());
            boolean matchePwd = bCryptPasswordEncoder.matches(pwdVo.getOldPwd(), cyptPwd);
            if (!matchePwd) {
                return ResponseResult.FAILED("原密码不正确");
            }
            //将新密码加密后存入数据库
            String encode = bCryptPasswordEncoder.encode(pwdVo.getNewPwd());
            studentMapper.updatePwdById(encode, student.getStuId());
            return ResponseResult.SUCCESS("密码修改成功");
        } else if (!TextUtils.isNull(teacher)) {
            //老师
            //看旧密码是否匹配
            String cyptPwd = teacherMapper.selectPwdById(teacher.getTeaId());
            boolean matchePwd = bCryptPasswordEncoder.matches(pwdVo.getOldPwd(), cyptPwd);
            if (!matchePwd) {
                return ResponseResult.FAILED("原密码不正确");
            }
            //将新密码加密后存入数据库
            String encode = bCryptPasswordEncoder.encode(pwdVo.getNewPwd());
            teacherMapper.updatePwdById(encode, teacher.getTeaId());
            return ResponseResult.SUCCESS("密码修改成功");
        } else {
            //admin
            //看旧密码是否匹配
            String cyptPwd = adminMapper.selectPwdById(admin.getAdminId());
            boolean matchePwd = bCryptPasswordEncoder.matches(pwdVo.getOldPwd(), cyptPwd);
            if (!matchePwd) {
                return ResponseResult.FAILED("原密码不正确");
            }
            //将新密码加密后存入数据库
            String encode = bCryptPasswordEncoder.encode(pwdVo.getNewPwd());
            adminMapper.updatePwdById(encode, admin.getAdminId());
            return ResponseResult.SUCCESS("密码修改成功");
        }
    }

    @Override
    public ResponseResult updateEmail(String newEmail) {
        //检查登录状况
        StuResponse student = loginService.judgeStudent(getRequest(), getResponse());
        TeaResponse teacher = loginService.judgeTeacher(getRequest(), getResponse());
        if (TextUtils.isNull(teacher) && TextUtils.isNull(student)) {
            return ResponseResult.FAILED("账号未登录");
        }
        //检查数据
        if (TextUtils.isEmpty(newEmail)) {
            return ResponseResult.FAILED("邮箱为空");
        }
        Map<String ,String> emailMap = JSON.parseObject(newEmail, Map.class);
        String email = emailMap.get("newEmail");
        //登录了
        if (!TextUtils.isNull(student)) {
            //学生
            studentMapper.updateEmailById(email, student.getStuId());
            return ResponseResult.SUCCESS("邮箱修改成功");
        } else {
            //老师
            teacherMapper.updateEmailById(email, teacher.getTeaId());
            return ResponseResult.SUCCESS("邮箱修改成功");
        }
    }
}
