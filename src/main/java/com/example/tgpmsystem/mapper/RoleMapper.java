package com.example.tgpmsystem.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.tgpmsystem.pojo.Role;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    @Insert("insert into role(role_id,role_no,role_name) " +
            "values(#{roleId},#{roleNo},#{roleName})")
    int save(Role role);

    @Select("select role_id from role where role_name = #{roleName}")
    String selectIdByRoleName(String roleName);

    @Select("select role_name from role where role_id = #{roleId}")
    String selectRoleNameByRoleId(String roleId);

    @Select("select role_name from role,user_role where role.role_id = user_role.role_id and user_id = #{userId}")
    String selectRoleNameByUserId(String userId);

}

