package com.example.tgpmsystem.service;

import com.example.tgpmsystem.response.ResponseResult;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public interface NoticeService {

    ResponseResult getNotices(Integer pageNum, Integer pageSize);
    ResponseResult readNotice(String  noticeId);
    ResponseResult deleteNotice(String  noticeId);
    ResponseResult unreadNoticesNum();

}
