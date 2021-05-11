package com.example.tgpmsystem.utils;

import com.example.tgpmsystem.pojo.AdminResponse;
import com.example.tgpmsystem.pojo.StuResponse;
import com.example.tgpmsystem.pojo.Student;
import com.example.tgpmsystem.pojo.TeaResponse;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ClaimsUtils {

    //学生老师共有的,管理员
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String SEX = "sex";
    public static final String COLLEGE = "college";
    public static final String DIRECTION = "direction";
    public static final String EMAIL = "email";
    public static final String STATUS = "status";

    //学生特有
    public static final String SNO = "sno";
    public static final String PROFESS = "profess";//专业
    public static final String TYPE = "type";//研究生类别
    public static final String ADMISS = "admiss";//入学年份

    //老师特有
    public static final String TNO = "tno";
    public static final String RANK = "rank";//职称

    //管理员特有
    public static final String ANO = "ano";

    public static Map<String, Object> student2Claims(StuResponse student) {
        Map<String,Object> cliams = new HashMap<>();
        cliams.put(ID,student.getStuId());
        cliams.put(SNO,student.getStuSno());
        cliams.put(NAME,student.getStuName());
        cliams.put(SEX,student.getStuSex());
        cliams.put(COLLEGE,student.getStuCollege());
        cliams.put(PROFESS,student.getStuProfess());
        cliams.put(TYPE,student.getStuType());
        cliams.put(DIRECTION,student.getStuDirection());
        cliams.put(EMAIL,student.getStuEmail());
        cliams.put(ADMISS,student.getStuAdmiss());
        cliams.put(STATUS,student.getStuStatus());
        return cliams;
    }

    public static StuResponse claims2Student(Claims claims) {
        StuResponse student = new StuResponse();
        String id = (String) claims.get(ID);
        student.setStuId(id);
        String sno = (String) claims.get(SNO);
        student.setStuSno(sno);
        String name = (String) claims.get(NAME);
        student.setStuName(name);
        String sex = (String) claims.get(SEX);
        student.setStuSex(sex);
        String college = (String) claims.get(COLLEGE);
        student.setStuCollege(college);
        String profess = (String) claims.get(PROFESS);
        student.setStuProfess(profess);
        String type = (String) claims.get(TYPE);
        student.setStuType(type);
        String direction = (String) claims.get(DIRECTION);
        student.setStuDirection(direction);
        String email = (String) claims.get(EMAIL);
        student.setStuEmail(email);
        String admiss = (String) claims.get(ADMISS);
        student.setStuAdmiss(admiss);
        String status = (String) claims.get(STATUS);
        student.setStuStatus(status);
        return student;
    }

    public static Map<String, Object> teacher2Claims(TeaResponse teacher) {
        Map<String,Object> cliams = new HashMap<>();
        cliams.put(ID,teacher.getTeaId());
        cliams.put(TNO,teacher.getTeaTno());
        cliams.put(NAME,teacher.getTeaName());
        cliams.put(SEX,teacher.getTeaSex());
        cliams.put(COLLEGE,teacher.getTeaCollege());
        cliams.put(DIRECTION,teacher.getTeaDirection());
        cliams.put(EMAIL,teacher.getTeaEmail());
        cliams.put(RANK,teacher.getTeaRank());
        cliams.put(STATUS,teacher.getTeaStatus());
        return cliams;
    }

    public static TeaResponse claims2Teacher(Claims claims) {
        TeaResponse teacher = new TeaResponse();
        String id = (String) claims.get(ID);
        teacher.setTeaId(id);
        String tno = (String) claims.get(TNO);
        teacher.setTeaTno(tno);
        String name = (String) claims.get(NAME);
        teacher.setTeaName(name);
        String sex = (String) claims.get(SEX);
        teacher.setTeaSex(sex);
        String college = (String) claims.get(COLLEGE);
        teacher.setTeaCollege(college);
        String direction = (String) claims.get(DIRECTION);
        teacher.setTeaDirection(direction);
        String email = (String) claims.get(EMAIL);
        teacher.setTeaEmail(email);
        String rank = (String) claims.get(RANK);
        teacher.setTeaRank(rank);
        String status = (String) claims.get(STATUS);
        teacher.setTeaStatus(status);
        return teacher;
    }

    public static Map<String, Object> admin2Claims(AdminResponse admin) {
        Map<String,Object> cliams = new HashMap<>();
        cliams.put(ID,admin.getAdminId());
        cliams.put(ANO,admin.getAdminAno());
        if (TextUtils.isNull(admin.getAdminCollege())) {
            //学院为空，为超管，就不需要存学院字段
            //直接返回
            return cliams;
        }
        cliams.put(COLLEGE,admin.getAdminCollege());
        return cliams;
    }

    public static AdminResponse claims2Admin(Claims claims) {
        AdminResponse admin = new AdminResponse();
        String id = (String) claims.get(ID);
        admin.setAdminId(id);
        String ano = (String) claims.get(ANO);
        admin.setAdminAno(ano);
        if (TextUtils.isNull(claims.get(COLLEGE))) {
            return admin;
        }
        String college = (String) claims.get(COLLEGE);
        admin.setAdminCollege(college);
        return admin;
    }

}
