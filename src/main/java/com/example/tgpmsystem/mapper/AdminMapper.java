package com.example.tgpmsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.tgpmsystem.pojo.Administrator;
import com.example.tgpmsystem.pojo.AdminResponse;
import com.example.tgpmsystem.pojo.StuResponse;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AdminMapper extends BaseMapper<Administrator> {

    @Select("select * from administrator where admin_college = #{adminCollege}")
    List<AdminResponse> selectByCollege(String adminCollege);

    @Select("select * from administrator")
    List<AdminResponse> getAll();

    @Select("select * from administrator where admin_college is null")
    List<AdminResponse> getAllAdmin();

    @Select("select * from administrator where admin_college is not null")
    List<AdminResponse> getAllSuper();

    @Select("select * from administrator where admin_ano = #{adminAno}")
    AdminResponse selectByAno(String adminAno);

    @Select("select admin_pwd from administrator where admin_ano = #{adminAno}")
    String selectPwdByAno(String adminAno);

    @Select("select admin_pwd from administrator where admin_id = #{adminId}")
    String selectPwdById(String adminId);

    @Update("update administrator set admin_pwd = #{adminPwd} where admin_id = #{adminId}")
    int updatePwdById(String adminPwd, String adminId);

    @Select("select * from administrator where admin_id = #{adminId}")
    AdminResponse findOneById(String adminId);

    @Insert("insert into administrator(admin_id,admin_ano,admin_pwd,admin_college) " +
            "values(#{adminId},#{adminAno},#{adminPwd},#{adminCollege})")
    int save(Administrator admin);

    @Delete("delete from administrator where admin_id = #{adminId}")
    int deleteByAdminId(String adminId);


}