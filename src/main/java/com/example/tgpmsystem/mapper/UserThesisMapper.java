package com.example.tgpmsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.tgpmsystem.pojo.UserThesis;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserThesisMapper extends BaseMapper<UserThesis> {

    @Insert("insert into user_thesis(user_thesis_id,user_id,thesis_id) " +
            "values(#{userThesisId},#{userId},#{thesisId})")
    int save(UserThesis userThesis);

    @Select("select user_id from user_thesis where thesis_id = #{thesisId}")
    String selectUserIdByThesisId(String thesisId);

}
