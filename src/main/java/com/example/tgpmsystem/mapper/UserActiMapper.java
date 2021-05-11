package com.example.tgpmsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.tgpmsystem.pojo.UserActi;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserActiMapper extends BaseMapper<UserActi> {

    @Insert("insert into user_acti(user_acti_id,user_id,acti_id) " +
            "values(#{userActiId},#{userId},#{actiId})")
    int save(UserActi userActi);

    @Select("select user_id from user_acti where acti_id = #{actiId}")
    String selectUserIdByActiId(String actiId);

}
