package com.example.tgpmsystem.service;

import com.example.tgpmsystem.response.ResponseResult;
import org.springframework.stereotype.Service;

@Service
public interface ViewTaskService {

    ResponseResult viewPlan(String planId);
    ResponseResult viewActivity(String planId);
    ResponseResult viewProject(String planId);
    ResponseResult viewThesis(String planId);
    ResponseResult viewAnnounce(String annoId);

    ResponseResult viewStuInfo(String stuId);
    ResponseResult viewTeaInfo(String teaId);

    ResponseResult getMyActiList(String userId, Integer pageNum, Integer pageSize);
    ResponseResult getMyProjListS(String stuId, Integer pageNum, Integer pageSize);
    ResponseResult getMyProjListT(String teaId, Integer pageNum, Integer pageSize);
    ResponseResult getMyThesisList(String userId, Integer pageNum, Integer pageSize);
}
