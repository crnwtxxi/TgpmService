package com.example.tgpmsystem.service;

import com.example.tgpmsystem.pojo.Announce;
import com.example.tgpmsystem.response.ResponseResult;
import com.example.tgpmsystem.vo.FailVerifyVo;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public interface AdminService {

    ResponseResult postAnnounce(Announce announce);

    ResponseResult getAllUnVerifyPlan(Integer pageNum, Integer pageSize);
    ResponseResult passPlan(String actiId);
    ResponseResult failPlan(FailVerifyVo failVerifyVo);

    ResponseResult getAllUnVerifyProject(Integer pageNum, Integer pageSize);

}

