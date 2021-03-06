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
        //????????????
        AdminResponse admin = loginService.judgeAdmin(getRequest(), getResponse());
        if (TextUtils.isNull(admin)) {
            return ResponseResult.FAILED("???????????????");
        }
        //?????????
        List<Map> resultList = new ArrayList<>();
        List<AdminResponse> adminList = adminMapper.getAll();
        for (int i = 0; i < adminList.size(); i++) {
            Map<String,Object> resultMap = new HashMap<>();
            resultMap.put("adminInfo",adminList.get(i));
            resultMap.put("role",roleMapper.selectRoleNameByUserId(adminList.get(i).getAdminId()));
            resultList.add(resultMap);
        }
//??????
        //??????Page???
        Page page1 = new Page(pageNum, pageSize);
        //???Page?????????total????????????
        int total = resultList.size();
        page1.setTotal(total);
        //???????????????????????????
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, total);
        //????????????
        page1.addAll(resultList.subList(startIndex, endIndex));
        //??????PageInfo
        PageInfo pageInfo = new PageInfo<>(page1);
        return ResponseResult.SUCCESS("????????????").setData(pageInfo);
    }

    @Override
    public ResponseResult deleteAdmin(String adminId) {
        //????????????
        AdminResponse admin = loginService.judgeAdmin(getRequest(), getResponse());
        if (TextUtils.isNull(admin)) {
            return ResponseResult.FAILED("???????????????");
        }
        //?????????
        adminMapper.deleteByAdminId(adminId);
        //????????????
        userRoleMapper.deleteByUserId(adminId);
        return ResponseResult.SUCCESS("????????????");
    }

    @Override
    public ResponseResult addAdmin(Administrator adminVo) {
        //????????????
        AdminResponse admin = loginService.judgeAdmin(getRequest(), getResponse());
        if (TextUtils.isNull(admin)) {
            return ResponseResult.FAILED("???????????????");
        }
        //?????????
        //????????????
        log.info(adminVo.toString());
        if (TextUtils.isEmpty(adminVo.getAdminAno())) {
            return ResponseResult.FAILED("???????????????????????????");
        }
        //????????????????????????
        AdminResponse adminFormDb = adminMapper.selectByAno(adminVo.getAdminAno());
        if (!TextUtils.isNull(adminFormDb)) {
            return ResponseResult.FAILED("???????????????");
        }
        Administrator a = new Administrator();
        a.setAdminId(idWorker.nextId()+"");
        a.setAdminAno(adminVo.getAdminAno());
        a.setAdminCollege(adminVo.getAdminCollege());
        //???????????????
        String encode = bCryptPasswordEncoder.encode("123456");
        a.setAdminPwd(encode);
        adminMapper.save(a);
        //????????????????????????
        UserRole userRole = new UserRole();
        userRole.setUserRoleId(idWorker.nextId()+"");
        userRole.setUserId(a.getAdminId());
        if (TextUtils.isEmpty(a.getAdminCollege())) {
            userRole.setRoleId(roleMapper.selectIdByRoleName("???????????????"));
        } else {
            userRole.setRoleId(roleMapper.selectIdByRoleName("???????????????"));
        }
        userRoleMapper.save(userRole);
        return ResponseResult.SUCCESS("?????????????????????");
    }
}
