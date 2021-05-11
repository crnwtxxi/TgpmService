package com.example.tgpmsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.tgpmsystem.pojo.Quota;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface QuotaMapper extends BaseMapper<Quota> {

    @Insert("insert into quota(quota_id,tea_id,quota_amount,quota_grade) " +
            "values(#{quotaId},#{teaId},#{quotaAmount},#{quotaGrade})")
    int save(Quota quota);

    @Select("select * from quota where tea_id = #{teaId} and quota_grade = #{grade}")
    Quota selectAmountByTeaIdAndGrade(String teaId, String grade);

    @Update("update quota set quota_amount = #{amount} where quota_id = #{quotaId}")
    int updateById(String quotaId, Long amount);

}
