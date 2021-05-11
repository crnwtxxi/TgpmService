package com.example.tgpmsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.tgpmsystem.pojo.Thesis;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ThesisMapper extends BaseMapper<Thesis> {

    @Insert("insert into thesis(thesis_id,thesis_title,thesis_date,thesis_period,thesis_name,thesis_seque,thesis_type,thesis_level,thesis_desc,thesis_status) " +
            "values(#{thesisId},#{thesisTitle},#{thesisDate},#{thesisPeriod},#{thesisName},#{thesisSeque},#{thesisType},#{thesisLevel},#{thesisDesc},#{thesisStatus})")
    int save(Thesis thesis);

    @Select("select * from thesis where thesis_id = #{thesisId}")
    Thesis selectById(String thesisId);

    @Select("select * from thesis where thesis_id in " +
            "(select thesis_id from user_thesis where user_id in " +
            "(select tea_id from teacher where tea_college = #{college})) " +
            "and thesis_status = '0'")
    List<Thesis> selectStatus0ByCollege(String college);

    @Update("update thesis set thesis_status = '1' where thesis_id = #{thesisId}")
    int updateStatus1ById(String thesisId);

    @Update("update thesis set thesis_status = '2' where thesis_id = #{thesisId}")
    int updateStatus2ById(String thesisId);

    @Select("select * from thesis where thesis_id in " +
            "(select thesis_id from user_thesis where user_id in " +
            "(select stu_id from student where tea_id = #{teaId})) " +
            "and thesis_status = '0'")
    List<Thesis> selectStatus0ByTeaId(String teaId);

    @Select("select * from thesis where thesis_id in " +
            "(select thesis_id from user_thesis where user_id = #{userId} " +
            "and thesis_status = '1')")
    List<Thesis> selectStatus1ByUserId(String userId);

    @Select("select * from thesis where thesis_id in " +
            "(select thesis_id from user_thesis where user_id = #{userId} " +
            "and thesis_status <> '1')")
    List<Thesis> selectStatus02ByUserId(String userId);

    @Select("select * from thesis where thesis_id in " +
            "(select thesis_id from user_thesis where user_id = #{userId})")
    List<Thesis> selectByUserId(String userId);

}
