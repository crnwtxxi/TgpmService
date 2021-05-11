package com.example.tgpmsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.tgpmsystem.pojo.Project;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ProjectMapper extends BaseMapper<Project> {

    @Insert("insert into project(proj_id,proj_title,proj_ondate,proj_offdate,proj_type,proj_form,proj_desc,proj_status) " +
            "values(#{projId},#{projTitle},#{projOndate},#{projOffdate},#{projType},#{projForm},#{projDesc},#{projStatus})")
    int save(Project project);

    @Select("select * from project where proj_id = #{projId}")
    Project selectById(String projId);

    @Select("select * from project where proj_id in " +
            "(select DISTINCT proj_id from user_proj where tea_id in " +
            "(select tea_id from teacher where tea_college = #{college})) " +
            "and proj_status = '0'")
    List<Project> selectStatus0ByCollege(String college);

    @Update("update project set proj_status = '1' where proj_id = #{projId}")
    int updateStatus1ById(String projId);

    @Update("update project set proj_status = '2' where proj_id = #{projId}")
    int updateStatus2ById(String projId);

    @Select("select * from project where proj_id in " +
            "(select DISTINCT proj_id from user_proj where tea_id = #{teaId}) " +
            "and proj_status = '1'")
    List<Project> selectStatus1ByTeaId(String teaId);

    @Select("select * from project where proj_id in " +
            "(select DISTINCT proj_id from user_proj where tea_id = #{teaId}) " +
            "and proj_status <> '1'")
    List<Project> selectStatus02ByTeaId(String teaId);

    @Select("select * from project where proj_id in " +
            "(select proj_id from user_proj where stu_id = #{stuId}) " +
            "and proj_status = '1'")
    List<Project> selectStatus1ByStuId(String stuId);

    @Select("select * from project where proj_id in " +
            "(select proj_id from user_proj where stu_id = #{stuId}) " +
            "and proj_status <> '1'")
    List<Project> selectStatus02ByStuId(String stuId);

    @Select("select * from project where proj_id in " +
            "(select proj_id from user_proj where tea_id = #{teaId})")
    List<Project> selectByTeaId(String teaId);

    @Select("select * from project where proj_id in " +
            "(select proj_id from user_proj where stu_id = #{stuId})")
    List<Project> selectByStuId(String stuId);

}
