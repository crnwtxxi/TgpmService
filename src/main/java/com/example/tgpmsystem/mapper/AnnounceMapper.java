package com.example.tgpmsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.tgpmsystem.pojo.Announce;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AnnounceMapper extends BaseMapper<Announce> {

    @Insert("insert into announce(anno_id,anno_title,anno_content,anno_college,anno_sender_id,anno_date) " +
            "values(#{annoId},#{annoTitle},#{annoContent},#{annoCollege},#{annoSenderId},#{annoDate})")
    int save(Announce announce);

    @Select("select * from announce where anno_college = #{annoCollege} ORDER BY anno_date DESC")
    List<Announce> selectByCollege(String annoCollege);

    @Select("select * from announce where anno_id = #{annoId}")
    Announce selectById(String annoId);
}
