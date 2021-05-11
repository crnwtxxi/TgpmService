package com.example.tgpmsystem.service.impl;

import com.example.tgpmsystem.mapper.*;
import com.example.tgpmsystem.pojo.*;
import com.example.tgpmsystem.response.ResponseResult;
import com.example.tgpmsystem.service.ViewTaskService;
import com.example.tgpmsystem.utils.TextUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ViewTaskServiceImpl implements ViewTaskService {

    @Resource
    private PlanMapper planMapper;

    @Resource
    private ActivityMapper activityMapper;

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private ThesisMapper thesisMapper;

    @Resource
    private TeacherMapper teacherMapper;

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private AnnounceMapper announceMapper;

    @Override
    public ResponseResult viewPlan(String planId) {
        Plan plan = planMapper.selectById(planId);
        return ResponseResult.SUCCESS("计划内容获取成功").setData(plan);
    }

    @Override
    public ResponseResult viewActivity(String actiId) {
        Activity activity = activityMapper.selectById(actiId);
        return ResponseResult.SUCCESS("学术活动内容获取成功").setData(activity);
    }

    @Override
    public ResponseResult viewProject(String projId) {
        Project project = projectMapper.selectById(projId);
        //查到项目的负责导师信息
        TeaResponse teaResponse = teacherMapper.selectByProjId(projId);
        //查到项目的成员
        List<StuResponse> stuResponses = studentMapper.selectByProjId(projId);
        Map<String, Object> result = new HashMap<>();
        result.put("projInfo", project);
        result.put("teaInfo", teaResponse);
        result.put("stuInfo", stuResponses);
        return ResponseResult.SUCCESS("科研项目内容获取成功").setData(result);
    }

    @Override
    public ResponseResult viewThesis(String thesisId) {
        Thesis thesis = thesisMapper.selectById(thesisId);
        return ResponseResult.SUCCESS("学术论文内容获取成功").setData(thesis);
    }

    @Override
    public ResponseResult viewAnnounce(String annoId) {
        Announce announce = announceMapper.selectById(annoId);
        return ResponseResult.SUCCESS("公告内容获取成功").setData(announce);
    }

    @Override
    public ResponseResult viewStuInfo(String stuId) {
        StuResponse student = studentMapper.selectById(stuId);
        return ResponseResult.SUCCESS("学生信息获取成功").setData(student);
    }

    @Override
    public ResponseResult viewTeaInfo(String teaId) {
        TeaResponse teacher = teacherMapper.selectById(teaId);
        return ResponseResult.SUCCESS("老师信息获取成功").setData(teacher);
    }

    @Override
    public ResponseResult getMyActiList(String userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Activity> activityList = activityMapper.selectStatus1ByUserId(userId);
        PageInfo<Activity> pageInfo = new PageInfo<>(activityList);
        return ResponseResult.SUCCESS("获取成功").setData(pageInfo);
    }

    @Override
    public ResponseResult getMyProjListS(String stuId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Project> projectList = projectMapper.selectStatus1ByStuId(stuId);
        PageInfo<Project> pageInfo = new PageInfo<>(projectList);
        return ResponseResult.SUCCESS("获取成功").setData(pageInfo);
    }

    @Override
    public ResponseResult getMyProjListT(String teaId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Project> projectList = projectMapper.selectStatus1ByTeaId(teaId);
        PageInfo<Project> pageInfo = new PageInfo<>(projectList);
        return ResponseResult.SUCCESS("获取成功").setData(pageInfo);
    }

    @Override
    public ResponseResult getMyThesisList(String userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Thesis> thesisList = thesisMapper.selectStatus1ByUserId(userId);
        PageInfo<Thesis> pageInfo = new PageInfo<>(thesisList);
        return ResponseResult.SUCCESS("获取成功").setData(pageInfo);
    }

}
