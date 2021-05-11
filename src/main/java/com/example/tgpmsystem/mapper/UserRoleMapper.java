package com.example.tgpmsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.tgpmsystem.pojo.UserRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    @Insert("insert into user_role(user_role_id,user_id,role_id) " +
            "values(#{userRoleId},#{userId},#{roleId})")
    int save(UserRole userRole);

    @Select("select role_id from user_role where user_id = #{userId}")
    String selectRoleIdByUserId(String userId);

    @Delete("delete from user_role where user_id = #{userId}")
    int deleteByUserId(String userId);


}
