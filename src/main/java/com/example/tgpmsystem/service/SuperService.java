package com.example.tgpmsystem.service;

import com.example.tgpmsystem.pojo.Administrator;
import com.example.tgpmsystem.response.ResponseResult;
import org.springframework.stereotype.Service;

@Service
public interface SuperService {

    //获取所有管理员列表
    ResponseResult getAdminList(Integer pageNum, Integer pageSize);

    //删除一个管理员
    ResponseResult deleteAdmin(String adminId);
    //新增一个管理员
    ResponseResult addAdmin(Administrator adminVo);

}
