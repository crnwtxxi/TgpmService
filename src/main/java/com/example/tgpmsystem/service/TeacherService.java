package com.example.tgpmsystem.service;

import com.example.tgpmsystem.response.ResponseResult;
import org.springframework.stereotype.Service;

@Service
public interface TeacherService {

    //获取自己所带的所有学生
    ResponseResult getAllSelfStu(Integer pageNum, Integer pageSize);

    //获取全校学生
    ResponseResult getAllCampusStu();

}
