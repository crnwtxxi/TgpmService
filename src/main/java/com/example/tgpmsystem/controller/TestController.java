package com.example.tgpmsystem.controller;

import com.example.tgpmsystem.mapper.*;
import com.example.tgpmsystem.pojo.*;
import com.example.tgpmsystem.response.ResponseResult;
import com.example.tgpmsystem.service.LoginService;
import com.example.tgpmsystem.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 对接口
 */

@Slf4j
@RestController
@RequestMapping("/api")
public class TestController {

    @Autowired
    IdWorker idWorker;

    @Resource
    LoginService loginService;

    @Resource
    RoleMapper roleMapper;

    @Resource
    UserRoleMapper userRoleMapper;

    @Resource
    StudentMapper studentMapper;

    @Resource
    TeacherMapper teacherMapper;

    @Resource
    AdminMapper adminMapper;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/hello")
    public ResponseResult helloWorld() {
        log.info("---------hello world----------");
        return ResponseResult.SUCCESS().setData("hello world !");
    }

    @GetMapping("/getId")
    public ResponseResult getId() {
        return ResponseResult.SUCCESS().setData(idWorker.nextId()+"");
    }

    @GetMapping("/createAdmin")
    public ResponseResult createAdmin() {
        Administrator admin = new Administrator();
        admin.setAdminAno("9876543210");
        admin.setAdminId(idWorker.nextId()+"");
        admin.setAdminPwd(bCryptPasswordEncoder.encode("super"));
        adminMapper.save(admin);
        //添加用户角色关系
        UserRole userRole = new UserRole();
        userRole.setUserRoleId(idWorker.nextId()+"");
        userRole.setUserId(admin.getAdminId());
        userRole.setRoleId(roleMapper.selectIdByRoleName("超级管理员"));
        userRoleMapper.save(userRole);
        return ResponseResult.SUCCESS("创建一个管理员成功");
    }


    /**
     * 初始化role表
     * @return
     */
    @GetMapping("/initRole")
    public ResponseResult initRole() {
        for (int i = 1; i <= 4; i++) {
            Role role = new Role();
            role.setRoleId(idWorker.nextId()+"");
            role.setRoleNo(i+"");
            role.setRoleName("学生");
            roleMapper.save(role);
        }
        return ResponseResult.SUCCESS("角色存入成功");
    }


    @PostMapping("/token")
    public ResponseResult testToken(@RequestBody Test test, HttpServletRequest request, HttpServletResponse response) {
        log.info("---------test token----------");
        log.info("test content == > " + test.getContent());
        //对身份进行确定
        String tokenKey = CookieUtils.getCookie(request, Constants.Student.STUDENT_COOKIE_TOKEN_KEY);
        if (tokenKey == null) {
            log.info("---------tokenKey is null----------");
            return ResponseResult.FAILED("账号未登录");
        }

        StuResponse student = loginService.checkStuLogin(request, response);
        if (student == null) {
            log.info("---------student is null----------");
            return ResponseResult.FAILED("账号未登录");
        }

        test.setStuId(student.getStuId());
        test.setStuSno(student.getStuSno());
        test.setStuName(student.getStuName());
        test.setStuSex(student.getStuSex());
        test.setStuCollege(student.getStuCollege());
        test.setStuProfess(student.getStuProfess());
        test.setStuType(student.getStuType());
        test.setStuDirection(student.getStuDirection());
        test.setStuEmail(student.getStuEmail());
        test.setId(idWorker.nextId() + "");
        //将这条数据存数据库里
        log.info("---------token message----------");
        log.info(test.toString());
        return ResponseResult.SUCCESS("测试成功");
    }


}
