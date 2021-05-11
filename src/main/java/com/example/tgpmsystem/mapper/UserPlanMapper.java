package com.example.tgpmsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.tgpmsystem.pojo.Plan;
import com.example.tgpmsystem.pojo.UserPlan;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserPlanMapper extends BaseMapper<UserPlan> {

    @Insert("insert into user_plan(user_plan_id,tea_id,plan_id,stu_id) " +
            "values(#{userPlanId},#{teaId},#{planId},#{stuId})")
    int save(UserPlan userPlan);

    @Select("select DISTINCT tea_id from user_plan where plan_id = #{planId}")
    String selectTeaIdByPlanId(String planId);

    @Select("select stu_id from user_plan where plan_id = #{planId}")
    List<String> selectStuIdByPlanId(String planId);

    @Select("select DISTINCT plan_id from user_plan where tea_id = #{teaId}")
    List<String> selectPlanIdByTeaId(String teaId);
}
