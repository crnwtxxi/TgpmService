package com.example.tgpmsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.tgpmsystem.pojo.Record;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface RecordMapper extends BaseMapper<Record> {

    @Insert("insert into record(record_id,stu_id,tea_id,record_status,record_grade) " +
            "values(#{recordId},#{stuId},#{teaId},#{recordStatus},#{recordGrade})")
    int save(Record record);

    //未审核个数
    @Select("select COUNT(*) from record where tea_id = #{teaId} and record_grade = #{grade} and record_status = '0'")
    int countStatus0ByTeaIdAndGrade(String teaId, String grade);

    //通过审核个数
    @Select("select COUNT(*) from record where tea_id = #{teaId} and record_grade = #{grade} and record_status = '1'")
    int countStatus1ByTeaIdAndGrade(String teaId, String grade);

    @Update("update record set record_status = '1' where stu_id = #{stuId} and tea_id = #{teaId}")
    int updateStatus1ByStuIdAndTeaId(String stuId, String teaId);

    @Update("update record set record_status = '2' where stu_id = #{stuId} and tea_id = #{teaId}")
    int updateStatus2ByStuIdAndTeaId(String stuId, String teaId);

    @Select("select stu_id from record where tea_id = #{teaId} and record_grade = #{grade} and record_status = '0'")
    List<String> selectStuIdByTeaIdAndGrade(String teaId, String grade);

    @Select("select * from record where stu_id = #{stuId}")
    List<Record> selectByStuId(String stuId);
}
