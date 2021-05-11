package com.example.tgpmsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.tgpmsystem.pojo.RefreshToken;
import com.example.tgpmsystem.pojo.Student;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RefreshTokenMapper extends BaseMapper<RefreshToken> {

    @Insert("INSERT INTO refresh_token(id,refresh_token,user_id,token_key,create_time,update_time) " +
            "values(#{id},#{refreshToken},#{userId},#{tokenKey},#{createTime},#{updateTime})")
    int save(RefreshToken refreshToken);

    @Select("select * from refresh_token where token_key = #{tokenKey}")
    RefreshToken findOneByTokenKey(String tokenKey);

    @Select("select * from refresh_token where user_id = #{userId}")
    List<RefreshToken> findOneById(String userId);

    @Delete("delete from refresh_token where user_id = #{userId}")
    int deleteByUserId(String userId);

    @Delete("delete from refresh_token where id = #{id}")
    int deleteById(String id);

}
