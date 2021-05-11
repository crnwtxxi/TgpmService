package com.example.tgpmsystem.service.impl;

import com.example.tgpmsystem.mapper.RoleMapper;
import com.example.tgpmsystem.mapper.StudentMapper;
import com.example.tgpmsystem.mapper.TeacherMapper;
import com.example.tgpmsystem.mapper.UserRoleMapper;
import com.example.tgpmsystem.pojo.*;
import com.example.tgpmsystem.response.ResponseResult;
import com.example.tgpmsystem.service.LoginService;
import com.example.tgpmsystem.service.UploadFileService;
import com.example.tgpmsystem.utils.ExcelUtils;
import com.example.tgpmsystem.utils.IdWorker;
import com.example.tgpmsystem.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UploadFileServiceImpl implements UploadFileService {

    @Resource
    ExcelUtils excelUtils;

    @Autowired
    IdWorker idWorker;

    @Resource
    TeacherMapper teacherMapper;

    @Resource
    StudentMapper studentMapper;

    @Resource
    RoleMapper roleMapper;

    @Resource
    UserRoleMapper userRoleMapper;

    @Resource
    LoginService loginService;

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
    public ResponseResult teaUpload(MultipartFile file) {
        //检查登录
        AdminResponse admin = loginService.judgeAdmin(getRequest(), getResponse());
        if (TextUtils.isNull(admin)) {
            return ResponseResult.FAILED("账号未登录");
        }
        //登陆了
        //解析excel文件
        List<ArrayList<String>> row = excelUtils.analysisExcel(file);
        ArrayList<TeaResponse> teaResponseList = new ArrayList<>();
        //打印信息
        for (int i = 0; i < row.size(); i++){
            List<String> cell = row.get(i);
            //每一行new一个对象
            Teacher teacher = new Teacher();

            //设置雪花ID
            teacher.setTeaId(idWorker.nextId()+"");
            //             0    1   2     3       4     5
            //信息数据顺序 tno name sex direction rank email
            teacher.setTeaTno(cell.get(0)+"");
            teacher.setTeaName(cell.get(1)+"");
            teacher.setTeaSex(cell.get(2)+"");
            teacher.setTeaDirection(cell.get(3)+"");
            teacher.setTeaRank(cell.get(4)+"");
            teacher.setTeaEmail(cell.get(5)+"");
            //设置学院，默认为管理员当前所属学院
            teacher.setTeaCollege(admin.getAdminCollege());
            //设置状态
            teacher.setTeaStatus("1");
            //设置默认密码123456
            //给密码加密
            String encode = bCryptPasswordEncoder.encode("123456");
            teacher.setTeaPwd(encode);
            //保存到数据库
            teacherMapper.save(teacher);
            //赋予角色
            UserRole userRole = new UserRole();
            userRole.setUserRoleId(idWorker.nextId()+"");
            userRole.setUserId(teacher.getTeaId());
            userRole.setRoleId(roleMapper.selectIdByRoleName("老师"));
            userRoleMapper.save(userRole);
            //添加到教师信息列表中用于返回数据给前端(不能有密码)
            TeaResponse teaResponse = getTeaResponse(teacher);
            teaResponseList.add(teaResponse);
        }
        return ResponseResult.SUCCESS("文件上传成功").setData(teaResponseList);
    }

    private TeaResponse getTeaResponse(Teacher teacher) {
        TeaResponse teaResponse = new TeaResponse();
        teaResponse.setTeaId(teacher.getTeaId());
        teaResponse.setTeaTno(teacher.getTeaTno());
        teaResponse.setTeaName(teacher.getTeaName());
        teaResponse.setTeaSex(teacher.getTeaSex());
        teaResponse.setTeaCollege(teacher.getTeaCollege());
        teaResponse.setTeaDirection(teacher.getTeaDirection());
        teaResponse.setTeaRank(teacher.getTeaRank());
        teaResponse.setTeaEmail(teacher.getTeaEmail());
        teaResponse.setTeaStatus(teacher.getTeaStatus());
        return teaResponse;
    }

    @Override
    public ResponseResult stuUpload(MultipartFile file) {
        //检查登录
        AdminResponse admin = loginService.judgeAdmin(getRequest(), getResponse());
        if (TextUtils.isNull(admin)) {
            return ResponseResult.FAILED("账号未登录");
        }
        //登陆了
        //解析excel文件
        List<ArrayList<String>> row = excelUtils.analysisExcel(file);
        ArrayList<StuResponse> stuResponseList = new ArrayList<>();
        //打印信息
        for (int i = 0; i < row.size(); i++){
            List<String> cell = row.get(i);
            //每一行new一个对象
            Student student = new Student();
            //设置雪花ID
            student.setStuId(idWorker.nextId()+"");
            //             0    1   2     3      4      5       6     7
            //信息数据顺序 sno name sex profess type direction email admiss
            student.setStuSno(cell.get(0)+"");
            student.setStuName(cell.get(1)+"");
            student.setStuSex(cell.get(2)+"");
            student.setStuProfess(cell.get(3)+"");
            student.setStuType(cell.get(4)+"");
            student.setStuDirection(cell.get(5)+"");
            student.setStuEmail(cell.get(6)+"");
            student.setStuAdmiss(cell.get(7)+"");
            //设置学院，默认为管理员所属学院
            student.setStuCollege(admin.getAdminCollege());
            //设置状态，默认在读为1
            student.setStuStatus("1");
            //设置默认密码123456
            //密码加密
            String encode = bCryptPasswordEncoder.encode("123456");
            student.setStuPwd(encode);
            //保存到数据库
            studentMapper.save(student);
            //赋予角色
            UserRole userRole = new UserRole();
            userRole.setUserRoleId(idWorker.nextId()+"");
            userRole.setUserId(student.getStuId());
            userRole.setRoleId(roleMapper.selectIdByRoleName("学生"));
            userRoleMapper.save(userRole);
            //添加到学生信息列表中用于返回数据给前端(不能有密码)
            StuResponse stuResponse = getStuResponse(student);
            stuResponseList.add(stuResponse);
        }
        return ResponseResult.SUCCESS("文件上传成功").setData(stuResponseList);
    }

    private StuResponse getStuResponse(Student student) {
        StuResponse stuResponse = new StuResponse();
        stuResponse.setStuId(student.getStuId());
        stuResponse.setStuSno(student.getStuSno());
        stuResponse.setStuName(student.getStuName());
        stuResponse.setStuSex(student.getStuSex());
        stuResponse.setStuCollege(student.getStuCollege());
        stuResponse.setStuProfess(student.getStuProfess());
        stuResponse.setStuType(student.getStuType());
        stuResponse.setStuDirection(student.getStuDirection());
        stuResponse.setStuEmail(student.getStuEmail());
        return stuResponse;
    }
}
