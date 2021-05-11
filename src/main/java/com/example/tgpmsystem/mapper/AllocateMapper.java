package com.example.tgpmsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.tgpmsystem.pojo.Allocate;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;

@Mapper
public interface AllocateMapper extends BaseMapper<Allocate> {

    @Insert("insert into allocate(allo_id,allo_college,allo_grade,allo_ondate,allo_offdate) " +
            "values(#{alloId},#{alloCollege},#{alloGrade},#{alloOndate},#{alloOffdate})")
    int save(Allocate allocate);

    @Select("select * from allocate where allo_college = #{college} and allo_grade = #{grade}")
    Allocate selectByCollegeAndGrade(String college, String grade);

    @Update("update allocate set allo_ondate = #{ondate},allo_offdate = #{offdate} where allo_id = #{alloId}")
    int updateDateById(String alloId, Date ondate, Date offdate);

}
