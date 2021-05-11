package com.example.tgpmsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.tgpmsystem.pojo.TeaResponse;
import com.example.tgpmsystem.pojo.Teacher;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface TeacherMapper extends BaseMapper<Teacher> {

    @Select("select * from teacher where tea_id = #{teaId}")
    TeaResponse findOneById(String teaId);

    @Select("select * from teacher where tea_tno = #{teaTno}")
    TeaResponse selectByTno(String teaTno);

    @Select("select tea_pwd from teacher where tea_tno = #{teaTno}")
    String selectPwdByTno(String teaTno);

    @Select("select tea_pwd from teacher where tea_id = #{teaId}")
    String selectPwdById(String teaId);

    @Update("update teacher set tea_pwd = #{teaPwd} where tea_id = #{teaId}")
    int updatePwdById(String teaPwd, String teaId);

    @Insert("insert into teacher(tea_id,tea_tno,tea_name,tea_sex,tea_college,tea_direction,tea_rank,tea_email,tea_pwd,tea_status) " +
            "values(#{teaId},#{teaTno},#{teaName},#{teaSex},#{teaCollege},#{teaDirection},#{teaRank},#{teaEmail},#{teaPwd},#{teaStatus})")
    int save(Teacher teacher);

    @Delete("delete from teacher where tea_id = #{teaId}")
    int deleteById(String teaId);

    @Select("select * from teacher where tea_college = #{college}")
    List<TeaResponse> selectByCollege(String college);

    @Select("select * from teacher where tea_id = " +
            "(select DISTINCT tea_id from user_plan where plan_id = #{planId})")
    TeaResponse selectByPlanId(String planId);

    @Select("select * from teacher where tea_id = " +
            "(select user_id from user_acti where acti_id = #{actiId})")
    TeaResponse selectByActiId(String actiId);

    @Select("select * from teacher where tea_id = " +
            "(select DISTINCT tea_id from user_proj where proj_id = #{projId})")
    TeaResponse selectByProjId(String projId);

    @Select("select * from teacher where tea_id = " +
            "(select user_id from user_thesis where thesis_id = #{thesisId})")
    TeaResponse selectByThesisId(String thesisId);

    @Select("select * from teacher where tea_id = (select tea_id from student where stu_id = #{stuId})")
    TeaResponse selectByStuId(String stuId);

    @Update("update teacher set tea_email = #{teaEmail} where tea_id = #{teaId}")
    int updateEmailById(String teaEmail, String teaId);

    @Select("select tea_name from teacher where tea_id = #{teaId}")
    String selectTeaNameByTeaId(String teaId);

    @Select("select * from teacher where tea_id = #{teaId}")
    TeaResponse selectById(String teaId);
}
