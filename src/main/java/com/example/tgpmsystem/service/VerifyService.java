package com.example.tgpmsystem.service;

import com.example.tgpmsystem.response.ResponseResult;
import com.example.tgpmsystem.vo.FailVerifyVo;
import org.springframework.stereotype.Service;

@Service
public interface VerifyService {

    ResponseResult getAllUnVerifyActivity(Integer pageNum, Integer pageSize);

    ResponseResult passActivity(String actiId);
    ResponseResult failActivity(FailVerifyVo failVerifyVo);

    ResponseResult passProject(String projId);
    ResponseResult failProject(FailVerifyVo failVerifyVo);

    ResponseResult getAllUnVerifyThesis(Integer pageNum, Integer pageSize);

    ResponseResult passThesis(String thesisId);
    ResponseResult failThesis(FailVerifyVo failVerifyVo);

}
