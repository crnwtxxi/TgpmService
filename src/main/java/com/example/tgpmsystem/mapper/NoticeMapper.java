package com.example.tgpmsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.tgpmsystem.pojo.Notice;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {

    @Insert("insert into notice(noti_id,noti_send_id,noti_recive_id,noti_content,noti_time,noti_status) " +
            "values(#{notiId},#{notiSendId},#{notiReciveId},#{notiContent},#{notiTime},#{notiStatus})")
    int save(Notice notice);

    @Select("select * from notice where noti_recive_id = #{notiReciveId} ORDER BY noti_time DESC")
    List<Notice> selectByReciveId(String notiReciveId);

    @Update("update notice set noti_status = '1' where noti_id = #{noticeId}")
    int updateStatusById(String noticeId);

    @Delete("delete from notice where noti_id = #{noticeId}")
    int deleteById(String noticeId);

    @Select("select * from notice where noti_status = '0' and noti_recive_id = #{notiReciveId}")
    List<Notice> selectStatus0ByReciveId(String notiReciveId);

}
