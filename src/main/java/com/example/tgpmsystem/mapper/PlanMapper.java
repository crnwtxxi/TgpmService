package com.example.tgpmsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.tgpmsystem.pojo.Activity;
import com.example.tgpmsystem.pojo.Plan;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface PlanMapper extends BaseMapper<Plan> {

    @Insert("insert into plan(plan_id,plan_title,plan_content,plan_date,plan_status) " +
            "values(#{planId},#{planTitle},#{planContent},#{planDate},#{planStatus})")
    int save(Plan plan);

    @Select("select * from plan where plan_id in " +
            "(select distinct plan_id from user_plan where tea_id in " +
            "(select tea_id from teacher where tea_college = #{college})) " +
            "and plan_status = '0'")
    List<Plan> selectByCollege(String college);

    @Select("select * from plan where plan_id = #{planId}")
    Plan selectById(String planId);

    @Update("update plan set plan_status = '1' where plan_id = #{planId}")
    int updateStatus1ById(String planId);

    @Update("update plan set plan_status = '2' where plan_id = #{planId}")
    int updateStatus2ById(String planId);

    @Select("select * from plan where plan_id = #{planId} and plan_status = '1'")
    Plan selectStatus1ById(String planId);

    @Select("select * from plan where plan_id = #{planId} and plan_status <> '1'")
    Plan selectStatus02ById(String planId);

    @Select("select * from plan where plan_id in " +
            "(select distinct plan_id from user_plan where stu_id = #{stuId}) and plan_status = '1'")
    List<Plan> selectByStuId(String stuId);

}
