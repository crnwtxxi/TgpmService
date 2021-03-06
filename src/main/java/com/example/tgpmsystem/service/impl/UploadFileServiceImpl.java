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
        //????????????
        AdminResponse admin = loginService.judgeAdmin(getRequest(), getResponse());
        if (TextUtils.isNull(admin)) {
            return ResponseResult.FAILED("???????????????");
        }
        //?????????
        //??????excel??????
        List<ArrayList<String>> row = excelUtils.analysisExcel(file);
        ArrayList<TeaResponse> teaResponseList = new ArrayList<>();
        //????????????
        for (int i = 0; i < row.size(); i++){
            List<String> cell = row.get(i);
            //?????????new????????????
            Teacher teacher = new Teacher();

            //????????????ID
            teacher.setTeaId(idWorker.nextId()+"");
            //             0    1   2     3       4     5
            //?????????????????? tno name sex direction rank email
            teacher.setTeaTno(cell.get(0)+"");
            teacher.setTeaName(cell.get(1)+"");
            teacher.setTeaSex(cell.get(2)+"");
            teacher.setTeaDirection(cell.get(3)+"");
            teacher.setTeaRank(cell.get(4)+"");
            teacher.setTeaEmail(cell.get(5)+"");
            //???????????????????????????????????????????????????
            teacher.setTeaCollege(admin.getAdminCollege());
            //????????????
            teacher.setTeaStatus("1");
            //??????????????????123456
            //???????????????
            String encode = bCryptPasswordEncoder.encode("123456");
            teacher.setTeaPwd(encode);
            //??????????????????
            teacherMapper.save(teacher);
            //????????????
            UserRole userRole = new UserRole();
            userRole.setUserRoleId(idWorker.nextId()+"");
            userRole.setUserId(teacher.getTeaId());
            userRole.setRoleId(roleMapper.selectIdByRoleName("??????"));
            userRoleMapper.save(userRole);
            //?????????????????????????????????????????????????????????(???????????????)
            TeaResponse teaResponse = getTeaResponse(teacher);
            teaResponseList.add(teaResponse);
        }
        return ResponseResult.SUCCESS("??????????????????").setData(teaResponseList);
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
        //????????????
        AdminResponse admin = loginService.judgeAdmin(getRequest(), getResponse());
        if (TextUtils.isNull(admin)) {
            return ResponseResult.FAILED("???????????????");
        }
        //?????????
        //??????excel??????
        List<ArrayList<String>> row = excelUtils.analysisExcel(file);
        ArrayList<StuResponse> stuResponseList = new ArrayList<>();
        //????????????
        for (int i = 0; i < row.size(); i++){
            List<String> cell = row.get(i);
            //?????????new????????????
            Student student = new Student();
            //????????????ID
            student.setStuId(idWorker.nextId()+"");
            //             0    1   2     3      4      5       6     7
            //?????????????????? sno name sex profess type direction email admiss
            student.setStuSno(cell.get(0)+"");
            student.setStuName(cell.get(1)+"");
            student.setStuSex(cell.get(2)+"");
            student.setStuProfess(cell.get(3)+"");
            student.setStuType(cell.get(4)+"");
            student.setStuDirection(cell.get(5)+"");
            student.setStuEmail(cell.get(6)+"");
            student.setStuAdmiss(cell.get(7)+"");
            //?????????????????????????????????????????????
            student.setStuCollege(admin.getAdminCollege());
            //??????????????????????????????1
            student.setStuStatus("1");
            //??????????????????123456
            //????????????
            String encode = bCryptPasswordEncoder.encode("123456");
            student.setStuPwd(encode);
            //??????????????????
            studentMapper.save(student);
            //????????????
            UserRole userRole = new UserRole();
            userRole.setUserRoleId(idWorker.nextId()+"");
            userRole.setUserId(student.getStuId());
            userRole.setRoleId(roleMapper.selectIdByRoleName("??????"));
            userRoleMapper.save(userRole);
            //?????????????????????????????????????????????????????????(???????????????)
            StuResponse stuResponse = getStuResponse(student);
            stuResponseList.add(stuResponse);
        }
        return ResponseResult.SUCCESS("??????????????????").setData(stuResponseList);
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
