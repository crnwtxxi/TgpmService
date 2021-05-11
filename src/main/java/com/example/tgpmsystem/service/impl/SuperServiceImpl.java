package com.example.tgpmsystem.service.impl;

import com.example.tgpmsystem.mapper.AdminMapper;
import com.example.tgpmsystem.mapper.RoleMapper;
import com.example.tgpmsystem.mapper.UserRoleMapper;
import com.example.tgpmsystem.pojo.AdminResponse;
import com.example.tgpmsystem.pojo.Administrator;
import com.example.tgpmsystem.pojo.UserRole;
import com.example.tgpmsystem.response.ResponseResult;
import com.example.tgpmsystem.service.LoginService;
import com.example.tgpmsystem.service.SuperService;
import com.example.tgpmsystem.utils.IdWorker;
import com.example.tgpmsystem.utils.TextUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SuperServiceImpl implements SuperService {

    @Resource
    private LoginService loginService;

    @Resource
    private AdminMapper adminMapper;

    @Autowired
    private IdWorker idWorker;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private RoleMapper roleMapper;

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
    public ResponseResult getAdminList(Integer pageNum, Integer pageSize) {
        //检查登录
        AdminResponse admin = loginService.judgeAdmin(getRequest(), getResponse());
        if (TextUtils.isNull(admin)) {
            return ResponseResult.FAILED("账号未登录");
        }
        //登录了
        List<Map> resultList = new ArrayList<>();
        List<AdminResponse> adminList = adminMapper.getAll();
        for (int i = 0; i < adminList.size(); i++) {
            Map<String,Object> resultMap = new HashMap<>();
            resultMap.put("adminInfo",adminList.get(i));
            resultMap.put("role",roleMapper.selectRoleNameByUserId(adminList.get(i).getAdminId()));
            resultList.add(resultMap);
        }
//分页
        //创建Page类
        Page page1 = new Page(pageNum, pageSize);
        //为Page类中的total属性赋值
        int total = resultList.size();
        page1.setTotal(total);
        //计算数据下标起始值
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, total);
        //截取数据
        page1.addAll(resultList.subList(startIndex, endIndex));
        //创建PageInfo
        PageInfo pageInfo = new PageInfo<>(page1);
        return ResponseResult.SUCCESS("获取成功").setData(pageInfo);
    }

    @Override
    public ResponseResult deleteAdmin(String adminId) {
        //检查登录
        AdminResponse admin = loginService.judgeAdmin(getRequest(), getResponse());
        if (TextUtils.isNull(admin)) {
            return ResponseResult.FAILED("账号未登录");
        }
        //登录了
        adminMapper.deleteByAdminId(adminId);
        //删除关系
        userRoleMapper.deleteByUserId(adminId);
        return ResponseResult.SUCCESS("删除成功");
    }

    @Override
    public ResponseResult addAdmin(Administrator adminVo) {
        //检查登录
        AdminResponse admin = loginService.judgeAdmin(getRequest(), getResponse());
        if (TextUtils.isNull(admin)) {
            return ResponseResult.FAILED("账号未登录");
        }
        //登录了
        //检查数据
        log.info(adminVo.toString());
        if (TextUtils.isEmpty(adminVo.getAdminAno())) {
            return ResponseResult.FAILED("管理员账号不能为空");
        }
        //检查账号的唯一性
        AdminResponse adminFormDb = adminMapper.selectByAno(adminVo.getAdminAno());
        if (!TextUtils.isNull(adminFormDb)) {
            return ResponseResult.FAILED("账号已存在");
        }
        Administrator a = new Administrator();
        a.setAdminId(idWorker.nextId()+"");
        a.setAdminAno(adminVo.getAdminAno());
        a.setAdminCollege(adminVo.getAdminCollege());
        //给密码加密
        String encode = bCryptPasswordEncoder.encode("123456");
        a.setAdminPwd(encode);
        adminMapper.save(a);
        //添加用户角色关系
        UserRole userRole = new UserRole();
        userRole.setUserRoleId(idWorker.nextId()+"");
        userRole.setUserId(a.getAdminId());
        if (TextUtils.isEmpty(a.getAdminCollege())) {
            userRole.setRoleId(roleMapper.selectIdByRoleName("超级管理员"));
        } else {
            userRole.setRoleId(roleMapper.selectIdByRoleName("院级管理员"));
        }
        userRoleMapper.save(userRole);
        return ResponseResult.SUCCESS("添加管理员成功");
    }
}
