package com.example.tgpmsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.tgpmsystem.pojo.Activity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ActivityMapper extends BaseMapper<Activity> {

    @Insert("insert into activity(acti_id,acti_title,acti_date,acti_host,acti_type,acti_form,acti_desc,acti_status) " +
            "values(#{actiId},#{actiTitle},#{actiDate},#{actiHost},#{actiType},#{actiForm},#{actiDesc},#{actiStatus})")
    int save(Activity activity);

    @Select("select * from activity where acti_id = #{actiId}")
    Activity selectById(String actiId);

    @Select("select * from activity where acti_id in " +
            "(select acti_id from user_acti where user_id in " +
            "(select tea_id from teacher where tea_college = #{college}))" +
            " and acti_status = '0'")
    List<Activity> selectStatus0ByCollege(String college);

    @Select("select * from activity where acti_id in " +
            "(select acti_id from user_acti where user_id in " +
            "(select stu_id from student where tea_id = #{teaId})) " +
            "and acti_status = '0'")
    List<Activity> selectStatus0ByTeaId(String teaId);

    @Update("update activity set acti_status = '1' where acti_id = #{actiId}")
    int updateStatus1ById(String actiId);

    @Update("update activity set acti_status = '2' where acti_id = #{actiId}")
    int updateStatus2ById(String actiId);

    @Select("select * from activity where acti_id in " +
            "(select acti_id from user_acti where user_id = #{userId}) " +
            "and acti_status = '1'")
    List<Activity> selectStatus1ByUserId(String userId);

    @Select("select * from activity where acti_id in " +
            "(select acti_id from user_acti where user_id = #{userId}) " +
            "and acti_status <> '1'")
    List<Activity> selectStatus02ByUserId(String userId);

    @Select("select * from activity where acti_id in " +
            "(select acti_id from user_acti where user_id = #{userId})")
    List<Activity> selectByUserId(String userId);
}
