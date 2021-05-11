package com.example.tgpmsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.tgpmsystem.pojo.UserProj;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserProjMapper extends BaseMapper<UserProj> {

    @Insert("insert into user_proj(user_proj_id,proj_id,stu_id,tea_id) " +
            "values(#{userProjId},#{projId},#{stuId},#{teaId})")
    int save(UserProj userProj);

    @Select("select DISTINCT tea_id from user_proj where proj_id = #{projId}")
    String selectTeaIdByProjId(String projId);


}
