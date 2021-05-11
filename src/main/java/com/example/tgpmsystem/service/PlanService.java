package com.example.tgpmsystem.service;

import com.example.tgpmsystem.response.ResponseResult;
import com.example.tgpmsystem.vo.UploadPlanVo;
import org.springframework.stereotype.Service;

@Service
public interface PlanService {

    //获取自己所带的所有学生的姓名,用于发布范围
    ResponseResult getAllSelfStu();
    //提交计划
    ResponseResult uploadPlan(UploadPlanVo planVo);
    //获取全部自己上传的计划的列表，包括未审核、通过和不通过所有状态，分两组展示
    ResponseResult getAllPassPlan(Integer pageNum, Integer pageSize);
    ResponseResult getAllUnpassPlan(Integer pageNum, Integer pageSize);

    //学生查看属于自己的计划
    ResponseResult getMyPlans(Integer pageNum, Integer pageSize);
}
