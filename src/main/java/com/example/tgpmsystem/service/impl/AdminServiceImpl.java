package com.example.tgpmsystem.service.impl;

import com.example.tgpmsystem.mapper.*;
import com.example.tgpmsystem.pojo.*;
import com.example.tgpmsystem.response.ResponseResult;
import com.example.tgpmsystem.service.AdminService;
import com.example.tgpmsystem.service.LoginService;
import com.example.tgpmsystem.utils.IdWorker;
import com.example.tgpmsystem.utils.TextUtils;
import com.example.tgpmsystem.vo.FailVerifyVo;
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
import java.util.*;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

    @Resource
    private LoginService loginService;

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
    private UserPlanMapper userPlanMapper;

    @Resource
    private NoticeMapper noticeMapper;

    @Resource
    private AnnounceMapper announceMapper;

    @Resource
    private StudentMapper studentMapper;

    @Autowired
    private IdWorker idWorker;

    private HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getRequest();
    }

    private HttpServletResponse getResponse() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getResponse();
    }

    @Override
    public ResponseResult postAnnounce(Announce announce) {
        //检查登录状况
        AdminResponse admin = loginService.judgeAdmin(getRequest(), getResponse());
        if (TextUtils.isNull(admin)) {
            return ResponseResult.FAILED("账号未登录");
        }
        //登录了
        //检查数据
        if (TextUtils.isEmpty(announce.getAnnoTitle()) || TextUtils.isEmpty(announce.getAnnoContent())) {
            return ResponseResult.FAILED("数据存在空值");
        }
        //保存一条公告数据
        Announce anno = new Announce();
        anno.setAnnoId(idWorker.nextId()+"");
        anno.setAnnoTitle(announce.getAnnoTitle());
        anno.setAnnoContent(announce.getAnnoContent());
        anno.setAnnoCollege(admin.getAdminCollege());
        anno.setAnnoSenderId(admin.getAdminId());
        anno.setAnnoDate(new Date());
        announceMapper.save(anno);
        //通知学院的师生有新的公告消息
        //本学院老师
        List<TeaResponse> teacherList = teacherMapper.selectByCollege(admin.getAdminCollege());
        for (int i = 0; i < teacherList.size(); i++) {
            sendNotice(admin.getAdminId(), teacherList.get(i).getTeaId(), "有的新公告待查看");
        }
        //本学院学生
        List<StuResponse> studentList = studentMapper.selectByCollege(admin.getAdminCollege());
        for (int j = 0; j < studentList.size(); j++) {
            sendNotice(admin.getAdminId(), studentList.get(j).getStuId(), "有的新公告待查看");
        }
        return ResponseResult.SUCCESS("公告发布成功");
    }

    @Override
    public ResponseResult getAllUnVerifyPlan(Integer pageNum, Integer pageSize) {
        //检查登录状况
        AdminResponse admin = loginService.judgeAdmin(getRequest(), getResponse());
        if (TextUtils.isNull(admin)) {
            return ResponseResult.FAILED("账号未登录");
        }
        //登录了
        //获取当前管理员所属的学院中未审核的培养计划列表
        List<Plan> planList = planMapper.selectByCollege(admin.getAdminCollege());
        //分别找到每个计划的上传者信息
        List<Map> resultList = new ArrayList<>();
        for (int i = 0; i < planList.size(); i++) {
            TeaResponse teaInfo = teacherMapper.selectByPlanId(planList.get(i).getPlanId());
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("teaInfo",teaInfo);
            resultMap.put("planInfo",planList.get(i));
            resultList.add(resultMap);
        }
        //分页
        //创建Page类
        Page page1 = new Page(pageNum, pageSize);
        //为Page类中的total属性赋值
        int total = resultList.size();
        page1.setTotal(total);
        //计算数据下标起始值
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, total);
        //截取数据
        page1.addAll(resultList.subList(startIndex, endIndex));
        //创建PageInfo
        PageInfo pageInfo = new PageInfo<>(page1);
        return ResponseResult.SUCCESS("获取未审核计划列表成功").setData(pageInfo);
    }

    @Override
    public ResponseResult passPlan(String planId) {
        //检查登录状况
        AdminResponse admin = loginService.judgeAdmin(getRequest(), getResponse());
        if (TextUtils.isNull(admin)) {
            return ResponseResult.FAILED("账号未登录");
        }
        //登录了
        //将计划状态设为1通过
        int updateNum = planMapper.updateStatus1ById(planId);
        if (updateNum == 0) {
            return ResponseResult.FAILED("该计划不存在");
        }
        //发送消息通知计划上传者审核通过
        //获取该计划上传者id
        String teaId = userPlanMapper.selectTeaIdByPlanId(planId);
        //存一条消息
        sendNotice(admin.getAdminId(), teaId, "您有一份培养计划审核通过");
        //通知计划接收者有新的待查看计划
        //获取计划对象ID
        List<String> stuIdList = userPlanMapper.selectStuIdByPlanId(planId);
        for (int i = 0; i < stuIdList.size(); i++) {
            sendNotice(teaId, stuIdList.get(i), "有一份培养计划待查看");
        }
        return ResponseResult.SUCCESS("计划审核通过");
    }

    @Override
    public ResponseResult failPlan(FailVerifyVo failVerifyVo) {
        //检查登录状况
        AdminResponse admin = loginService.judgeAdmin(getRequest(), getResponse());
        if (TextUtils.isNull(admin)) {
            return ResponseResult.FAILED("账号未登录");
        }
        String planId = failVerifyVo.getId();
        String reason = failVerifyVo.getReason();
        //登录了
        //将状态设为2不通过
        int updateNum = planMapper.updateStatus2ById(planId);
        if (updateNum == 0) {
            return ResponseResult.FAILED("该计划不存在");
        }
        //发送消息通知计划上传者审核不通过
        //获取该计划上传者id
        String teaId = userPlanMapper.selectTeaIdByPlanId(planId);
        //存一条消息
        sendNotice(admin.getAdminId(), teaId, "您有一份培养计划审核未通过,理由："+reason);
        return ResponseResult.SUCCESS("计划审核不通过");
    }

    @Override
    public ResponseResult getAllUnVerifyProject(Integer pageNum, Integer pageSize) {
        //检查登录状况
        AdminResponse admin = loginService.judgeAdmin(getRequest(), getResponse());
        if (TextUtils.isNull(admin)) {
            return ResponseResult.FAILED("账号未登录");
        }
        //登录了
        List<Project> projectList = projectMapper.selectStatus0ByCollege(admin.getAdminCollege());
        List<Map> resultList = new ArrayList<>();
        for (int i = 0; i < projectList.size(); i++) {
            TeaResponse teaInfo = teacherMapper.selectByProjId(projectList.get(i).getProjId());
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("teaInfo",teaInfo);
            resultMap.put("projInfo",projectList.get(i));
            resultList.add(resultMap);
        }
        //分页
        //创建Page类
        Page page1 = new Page(pageNum, pageSize);
        //为Page类中的total属性赋值
        int total = resultList.size();
        page1.setTotal(total);
        //计算数据下标起始值
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, total);
        //截取数据
        page1.addAll(resultList.subList(startIndex, endIndex));
        //创建PageInfo
        PageInfo pageInfo = new PageInfo<>(page1);
        return ResponseResult.SUCCESS("获取未审核计划列表成功").setData(pageInfo);
    }

    private void sendNotice(String sendId, String reciveId, String message) {
        Notice notice = new Notice();
        notice.setNotiId(idWorker.nextId()+"");
        notice.setNotiTime(new Date());
        notice.setNotiContent(message);
        notice.setNotiSendId(sendId);
        notice.setNotiReciveId(reciveId);
        notice.setNotiStatus("0");//默认未读
        noticeMapper.save(notice);
    }

}
