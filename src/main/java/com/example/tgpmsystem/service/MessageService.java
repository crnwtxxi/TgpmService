package com.example.tgpmsystem.service;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.example.tgpmsystem.pojo.TeaResponse;
import com.example.tgpmsystem.response.ResponseResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MessageService {

    //获取所有老师信息列表
    ResponseResult getAllTeaInfo(int pageNum, int pageSize);
    //删除一个老师
    ResponseResult deleteOneTea(String teaId);

    //获取所有学生信息列表
    ResponseResult getAllStuInfo(int pageNum, int pageSize);
    //删除一个学生
    ResponseResult deleteOneStu(String stuId);

}
