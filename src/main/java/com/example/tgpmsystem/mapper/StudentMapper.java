package com.example.tgpmsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.tgpmsystem.pojo.StuResponse;
import com.example.tgpmsystem.pojo.Student;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据库查询
 */

@Mapper
public interface StudentMapper extends BaseMapper<Student> {

    @Select("select * from student where stu_sno = #{stuSno}")
    StuResponse selectBySno(String stuSno);

    @Select("select stu_pwd from student where stu_sno = #{stuSno}")
    String selectPwdBySno(String stuSno);

    @Select("select stu_pwd from student where stu_id = #{stuId}")
    String selectPwdById(String stuId);

//    update Student set Sclass = '5' where Sdept = 'IS'
    @Update("update student set stu_pwd = #{stuPwd} where stu_id = #{stuId}")
    int updatePwdById(String stuPwd, String stuId);

    @Select("select * from student where stu_id = #{stuId}")
    StuResponse findOneById(String stuId);

    @Insert("insert into student(stu_id,stu_sno,stu_name,stu_sex,stu_college,stu_profess,stu_type,stu_direction,stu_email,stu_pwd,stu_admiss,stu_status) " +
            "values(#{stuId},#{stuSno},#{stuName},#{stuSex},#{stuCollege},#{stuProfess},#{stuType},#{stuDirection},#{stuEmail},#{stuPwd},#{stuAdmiss},#{stuStatus})")
    int save(Student student);

    @Delete("delete from student where stu_id = #{stuId}")
    int deleteById(String stuId);

    @Select("select * from student where stu_college = #{college}")
    List<StuResponse> selectByCollege(String college);

    @Select("select * from student where tea_id = #{teaId}")
    List<StuResponse> selectStuByTeaId(String teaId);

    @Select("select tea_id from student where stu_id = #{stuId}")
    String selectTeaIdByStuId(String stuId);

    @Select("select * from student where stu_id = " +
            "(select user_id from user_acti where acti_id = #{actiId})")
    StuResponse selectByActiId(String actiId);

    @Select("select * from student")
    List<StuResponse> findAll();

    @Select("select * from student where stu_id in " +
            "(select stu_id from user_proj where proj_id = #{projId})")
    List<StuResponse> selectByProjId(String projId);

    @Select("select * from student where stu_id = " +
            "(select user_id from user_thesis where thesis_id = #{thesisId})")
    StuResponse selectByThesisId(String thesisId);

    @Update("update student set stu_email = #{stuEmail} where stu_id = #{stuId}")
    int updateEmailById(String stuEmail, String stuId);

    @Update("update student set tea_id = #{teaId} where stu_id = #{stuId}")
    int updateTeaIdByStuId(String stuId, String teaId);

    @Select("select * from student where stu_id in " +
            "(select stu_id from record where tea_id = #{teaId} and record_grade = #{grade} and record_status = '0')")
    List<StuResponse> selectByRecord(String teaId, String grade);

    @Select("select count(*) from student where tea_id = #{teaId} and stu_admiss = #{grade}")
    int countByTeaIdAndGrade(String teaId, String grade);

    @Select("select * from student where stu_id = #{stuId}")
    StuResponse selectById(String stuId);
}
