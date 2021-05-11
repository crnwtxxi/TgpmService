package com.example.tgpmsystem.service.impl;

import com.example.tgpmsystem.mapper.*;
import com.example.tgpmsystem.pojo.*;
import com.example.tgpmsystem.response.ResponseResult;
import com.example.tgpmsystem.service.LoginService;
import com.example.tgpmsystem.service.PlanService;
import com.example.tgpmsystem.utils.IdWorker;
import com.example.tgpmsystem.utils.TextUtils;
import com.example.tgpmsystem.vo.UploadPlanVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class PlanServiceImpl implements PlanService {

    @Resource
    StudentMapper studentMapper;

    @Resource
    UserPlanMapper userPlanMapper;

    @Resource
    AdminMapper adminMapper;

    @Resource
    PlanMapper planMapper;

    @Resource
    NoticeMapper noticeMapper;

    @Resource
    LoginService loginService;

    @Autowired
    IdWorker idWorker;

    private HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getRequest();
    }

    private HttpServletResponse getResponse() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getResponse();
    }

    @Override
    public ResponseResult getAllSelfStu() {
        //检查登录状况
        TeaResponse teacher = loginService.judgeTeacher(getRequest(), getResponse());
        if (TextUtils.isNull(teacher)) {
            return ResponseResult.FAILED("账号未登录");
        }
        //登录了
        String currentTeaId = teacher.getTeaId();
        List<StuResponse> selfStuList = studentMapper.selectStuByTeaId(currentTeaId);
        return ResponseResult.SUCCESS("学生获取成功").setData(selfStuList);
    }

    @Override
    public ResponseResult uploadPlan(UploadPlanVo planVo) {
        //检查登录状况
        TeaResponse teacher = loginService.judgeTeacher(getRequest(), getResponse());
        if (TextUtils.isNull(teacher)) {
            //token过期，登录超时，需要重新登录
            return ResponseResult.FAILED("账号未登录");
        }
        //登陆了
        //检查数据
        if (TextUtils.isEmpty(planVo.getPlanTitle())) {
            ResponseResult.FAILED("标题为空，计划上传失败");
        }
        if (TextUtils.isEmpty(planVo.getPlanContent())) {
            ResponseResult.FAILED("内容为空，计划上传失败");
        }
        if (TextUtils.isNull(planVo.getStudentList())) {
            ResponseResult.FAILED("发布对象为空，计划上传失败");
        }
        //数据没问题，存进数据库，状态设为 【0未审核】
        Plan plan = new Plan();
        plan.setPlanId(idWorker.nextId()+"");
        plan.setPlanTitle(planVo.getPlanTitle());
        plan.setPlanContent(planVo.getPlanContent());
        plan.setPlanDate(new Date());
        plan.setPlanStatus("0");//未审核
        //存入数据库plan表
        planMapper.save(plan);
        //通知院级管理员进行审核，只有审核通过了才可以存入数据库
        //先从数据库中找出自己学院的管理员ID（可能有多个管理员）
        List<AdminResponse> adminList = adminMapper.selectByCollege(teacher.getTeaCollege());
        //给这些管理员发送消息通知
        //存一条消息通知到数据库 id send_id recive_id content time status
        for (int j = 0; j < adminList.size(); j++) {
            Notice notice = new Notice();
            notice.setNotiId(idWorker.nextId()+"");
            notice.setNotiSendId(teacher.getTeaId());
            notice.setNotiReciveId(adminList.get(j).getAdminId());
            notice.setNotiContent("有一份计划待审核");
            notice.setNotiTime(new Date());
            notice.setNotiStatus("0");//未读
            noticeMapper.save(notice);
        }
        //获取当前写计划的老师ID
        String currentTeaId = teacher.getTeaId();
        //发布对象，一个学生存一条，user_plan表  id  teaId stuId planId
        List<StuResponse> studentList = planVo.getStudentList();
        for (int i = 0; i < studentList.size(); i++) {
            UserPlan userPlan = new UserPlan();
            userPlan.setUserPlanId(idWorker.nextId()+"");
            userPlan.setTeaId(currentTeaId);
            userPlan.setPlanId(plan.getPlanId());
            userPlan.setStuId(studentList.get(i).getStuId());
            userPlanMapper.save(userPlan);
        }
        return ResponseResult.SUCCESS("计划上传成功");
    }

    @Override
    public ResponseResult getAllPassPlan(Integer pageNum, Integer pageSize) {
        //检查登录状况
        TeaResponse teacher = loginService.judgeTeacher(getRequest(), getResponse());
        if (TextUtils.isNull(teacher)) {
            //token过期，登录超时，需要重新登录
            return ResponseResult.FAILED("账号未登录");
        }
        //登陆了
        //在数据库中查出来当前老师发布过的所有计划的id
        List<String> planIdList = userPlanMapper.selectPlanIdByTeaId(teacher.getTeaId());
        List<Plan> planList = new ArrayList<>();
        for (int i = 0; i < planIdList.size(); i++) {
            Plan plan = planMapper.selectStatus1ById(planIdList.get(i));
            if (!TextUtils.isNull(plan)){
                planList.add(plan);
            }
        }
        //将planList分页
        //创建Page类
        Page page1 = new Page(pageNum, pageSize);
        //为Page类中的total属性赋值
        int total = planList.size();
        page1.setTotal(total);
        //计算数据下标起始值
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, total);
        //截取数据
        page1.addAll(planList.subList(startIndex, endIndex));
        //创建PageInfo
        PageInfo pageInfo = new PageInfo<>(page1);
        return ResponseResult.SUCCESS("计划列表获取成功").setData(pageInfo);
    }

    @Override
    public ResponseResult getAllUnpassPlan(Integer pageNum, Integer pageSize) {
        //检查登录状况
        TeaResponse teacher = loginService.judgeTeacher(getRequest(), getResponse());
        if (TextUtils.isNull(teacher)) {
            //token过期，登录超时，需要重新登录
            return ResponseResult.FAILED("账号未登录");
        }
        //登陆了
        //在数据库中查出来当前老师发布过的所有计划的id
        List<String> planIdList = userPlanMapper.selectPlanIdByTeaId(teacher.getTeaId());
        List<Plan> planList = new ArrayList<>();
        for (int i = 0; i < planIdList.size(); i++) {
            Plan plan = planMapper.selectStatus02ById(planIdList.get(i));
            if (!TextUtils.isNull(plan)){
                planList.add(plan);
            }
        }
        //将planList分页
        //创建Page类
        Page page1 = new Page(pageNum, pageSize);
        //为Page类中的total属性赋值
        int total = planList.size();
        page1.setTotal(total);
        //计算数据下标起始值
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, total);
        //截取数据
        page1.addAll(planList.subList(startIndex, endIndex));
        //创建PageInfo
        PageInfo pageInfo = new PageInfo<>(page1);
        return ResponseResult.SUCCESS("计划列表获取成功").setData(pageInfo);
    }

    @Override
    public ResponseResult getMyPlans(Integer pageNum, Integer pageSize) {
        //检查登录状况
        StuResponse student = loginService.judgeStudent(getRequest(), getResponse());
        if (TextUtils.isNull(student)) {
            //token过期，登录超时，需要重新登录
            return ResponseResult.FAILED("账号未登录");
        }
        //登陆了
        //分页
        PageHelper.startPage(pageNum, pageSize);
        List<Plan> planList = planMapper.selectByStuId(student.getStuId());
        PageInfo<Plan> pageInfo = new PageInfo<>(planList);
        return ResponseResult.SUCCESS("获取老师给自己发布的计划成功").setData(pageInfo);
    }
}
