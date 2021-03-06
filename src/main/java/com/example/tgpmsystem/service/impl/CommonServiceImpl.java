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
        //??????????????????
        StuResponse student = loginService.judgeStudent(getRequest(), getResponse());
        TeaResponse teacher = loginService.judgeTeacher(getRequest(), getResponse());
        AdminResponse admin = loginService.judgeAdmin(getRequest(), getResponse());
        if (TextUtils.isNull(admin) && TextUtils.isNull(teacher) && TextUtils.isNull(student)) {
            return ResponseResult.FAILED("???????????????");
        }
        //?????????
        if (!TextUtils.isNull(student)) {
            //??????
            //??????????????????????????????
            PageHelper.startPage(pageNum, pageSize);
            List<Announce> announceList = announceMapper.selectByCollege(student.getStuCollege());
            PageInfo<Announce> pageInfo = new PageInfo<>(announceList);
            return ResponseResult.SUCCESS("????????????????????????").setData(pageInfo);
        } else if (!TextUtils.isNull(teacher)) {
            //??????
            //??????????????????????????????
            PageHelper.startPage(pageNum, pageSize);
            List<Announce> announceList = announceMapper.selectByCollege(teacher.getTeaCollege());
            PageInfo<Announce> pageInfo = new PageInfo<>(announceList);
            return ResponseResult.SUCCESS("????????????????????????").setData(pageInfo);
        } else {
            //admin
            //??????????????????????????????
            PageHelper.startPage(pageNum, pageSize);
            List<Announce> announceList = announceMapper.selectByCollege(admin.getAdminCollege());
            PageInfo<Announce> pageInfo = new PageInfo<>(announceList);
            return ResponseResult.SUCCESS("????????????????????????").setData(pageInfo);
        }
    }

    @Override
    public ResponseResult updatePassword(UpdatePwdVo pwdVo) {
        //??????????????????
        StuResponse student = loginService.judgeStudent(getRequest(), getResponse());
        TeaResponse teacher = loginService.judgeTeacher(getRequest(), getResponse());
        AdminResponse admin = loginService.judgeAdmin(getRequest(), getResponse());
        if (TextUtils.isNull(admin) && TextUtils.isNull(teacher) && TextUtils.isNull(student)) {
            return ResponseResult.FAILED("???????????????");
        }
        //????????????
        if (TextUtils.isEmpty(pwdVo.getOldPwd()) || TextUtils.isEmpty(pwdVo.getNewPwd())) {
            return ResponseResult.FAILED("??????????????????");
        }
        //?????????
        if (!TextUtils.isNull(student)) {
            //??????
            //????????????????????????
            String cyptPwd = studentMapper.selectPwdById(student.getStuId());
            boolean matchePwd = bCryptPasswordEncoder.matches(pwdVo.getOldPwd(), cyptPwd);
            if (!matchePwd) {
                return ResponseResult.FAILED("??????????????????");
            }
            //????????????????????????????????????
            String encode = bCryptPasswordEncoder.encode(pwdVo.getNewPwd());
            studentMapper.updatePwdById(encode, student.getStuId());
            return ResponseResult.SUCCESS("??????????????????");
        } else if (!TextUtils.isNull(teacher)) {
            //??????
            //????????????????????????
            String cyptPwd = teacherMapper.selectPwdById(teacher.getTeaId());
            boolean matchePwd = bCryptPasswordEncoder.matches(pwdVo.getOldPwd(), cyptPwd);
            if (!matchePwd) {
                return ResponseResult.FAILED("??????????????????");
            }
            //????????????????????????????????????
            String encode = bCryptPasswordEncoder.encode(pwdVo.getNewPwd());
            teacherMapper.updatePwdById(encode, teacher.getTeaId());
            return ResponseResult.SUCCESS("??????????????????");
        } else {
            //admin
            //????????????????????????
            String cyptPwd = adminMapper.selectPwdById(admin.getAdminId());
            boolean matchePwd = bCryptPasswordEncoder.matches(pwdVo.getOldPwd(), cyptPwd);
            if (!matchePwd) {
                return ResponseResult.FAILED("??????????????????");
            }
            //????????????????????????????????????
            String encode = bCryptPasswordEncoder.encode(pwdVo.getNewPwd());
            adminMapper.updatePwdById(encode, admin.getAdminId());
            return ResponseResult.SUCCESS("??????????????????");
        }
    }

    @Override
    public ResponseResult updateEmail(String newEmail) {
        //??????????????????
        StuResponse student = loginService.judgeStudent(getRequest(), getResponse());
        TeaResponse teacher = loginService.judgeTeacher(getRequest(), getResponse());
        if (TextUtils.isNull(teacher) && TextUtils.isNull(student)) {
            return ResponseResult.FAILED("???????????????");
        }
        //????????????
        if (TextUtils.isEmpty(newEmail)) {
            return ResponseResult.FAILED("????????????");
        }
        Map<String ,String> emailMap = JSON.parseObject(newEmail, Map.class);
        String email = emailMap.get("newEmail");
        //?????????
        if (!TextUtils.isNull(student)) {
            //??????
            studentMapper.updateEmailById(email, student.getStuId());
            return ResponseResult.SUCCESS("??????????????????");
        } else {
            //??????
            teacherMapper.updateEmailById(email, teacher.getTeaId());
            return ResponseResult.SUCCESS("??????????????????");
        }
    }
}
