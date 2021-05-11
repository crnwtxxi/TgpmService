package com.example.tgpmsystem.service.impl;

import com.example.tgpmsystem.mapper.*;
import com.example.tgpmsystem.pojo.*;
import com.example.tgpmsystem.response.ResponseResult;
import com.example.tgpmsystem.service.LoginService;
import com.example.tgpmsystem.service.VerifyService;
import com.example.tgpmsystem.utils.IdWorker;
import com.example.tgpmsystem.utils.TextUtils;
import com.example.tgpmsystem.vo.FailVerifyVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
public class VerifyServiceImpl implements VerifyService {

    @Resource
    private LoginService loginService;

    @Resource
    private NoticeMapper noticeMapper;

    @Resource
    private ActivityMapper activityMapper;

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private ThesisMapper thesisMapper;

    @Resource
    private UserActiMapper userActiMapper;

    @Resource
    private UserProjMapper userProjMapper;

    @Resource
    private UserThesisMapper userThesisMapper;

    @Resource
    private TeacherMapper teacherMapper;

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
    public ResponseResult getAllUnVerifyActivity(Integer pageNum, Integer pageSize) {
        //检查登录状况
        TeaResponse teacher = loginService.judgeTeacher(getRequest(), getResponse());
        AdminResponse admin = loginService.judgeAdmin(getRequest(), getResponse());
        if (TextUtils.isNull(admin) && TextUtils.isNull(teacher)) {
            return ResponseResult.FAILED("账号未登录");
        }
        //登录了
        if (!TextUtils.isNull(teacher)) {
            //老师
            //获取当前老师所带学生中填报的未审核的学术活动列表
            List<Activity> activityList = activityMapper.selectStatus0ByTeaId(teacher.getTeaId());
            //获取每个活动填报者的信息
            List<Map> resultList = new ArrayList<>();
            for (int i = 0; i < activityList.size(); i++) {
                StuResponse stuInfo = studentMapper.selectByActiId(activityList.get(i).getActiId());
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("userInfo",stuInfo);
                resultMap.put("actiInfo",activityList.get(i));
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
        } else {
            //admin
            //获取当前管理员所属的学院中未审核的学术活动列表
            List<Activity> activityList = activityMapper.selectStatus0ByCollege(admin.getAdminCollege());
            //获取每条活动上传者信息
            List<Map> resultList = new ArrayList<>();
            for (int i = 0; i < activityList.size(); i++) {
                TeaResponse teaInfo = teacherMapper.selectByActiId(activityList.get(i).getActiId());
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("userInfo",teaInfo);
                resultMap.put("actiInfo",activityList.get(i));
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
    }

    @Override
    public ResponseResult passActivity(String actiId) {
        //检查登录状况
        TeaResponse teacher = loginService.judgeTeacher(getRequest(), getResponse());
        AdminResponse admin = loginService.judgeAdmin(getRequest(), getResponse());
        if (TextUtils.isNull(admin) && TextUtils.isNull(teacher)) {
            return ResponseResult.FAILED("账号未登录");
        }
        //登录了
        if (!TextUtils.isNull(teacher)) {
            //老师
            //将活动状态置为1，表示通过
            int updateNum = activityMapper.updateStatus1ById(actiId);
            if (updateNum == 0) {
                return ResponseResult.FAILED("该计划不存在");
            }
            //发送消息通知学生审核通过
            //获取该上传者id
            String userId = userActiMapper.selectUserIdByActiId(actiId);
            //发消息通知活动审核通过
            sendNotice(teacher.getTeaId(), userId, "填报的活动审核通过");
        } else {
            //admin
            //将活动状态置为1，表示通过
            int updateNum = activityMapper.updateStatus1ById(actiId);
            if (updateNum == 0) {
                return ResponseResult.FAILED("该计划不存在");
            }
            //发送消息通知老师审核通过
            //获取该上传者id
            String userId = userActiMapper.selectUserIdByActiId(actiId);
            //发消息通知活动审核通过
            sendNotice(admin.getAdminId(), userId, "填报的活动审核通过");
        }
        return ResponseResult.SUCCESS("审核通过");
    }

    @Override
    public ResponseResult failActivity(FailVerifyVo failVerifyVo) {
        //检查登录状况
        TeaResponse teacher = loginService.judgeTeacher(getRequest(), getResponse());
        AdminResponse admin = loginService.judgeAdmin(getRequest(), getResponse());
        if (TextUtils.isNull(admin) && TextUtils.isNull(teacher)) {
            return ResponseResult.FAILED("账号未登录");
        }
        //登录了
        String actiId = failVerifyVo.getId();
        String reason = failVerifyVo.getReason();
        if (!TextUtils.isNull(teacher)) {
            //老师
            //将活动状态置为2，表示不通过
            int updateNum = activityMapper.updateStatus2ById(actiId);
            if (updateNum == 0) {
                return ResponseResult.FAILED("该计划不存在");
            }
            //发送消息通知学生审核不通过
            //获取该上传者id
            String userId = userActiMapper.selectUserIdByActiId(actiId);
            //发消息通知活动审核通过
            sendNotice(teacher.getTeaId(), userId, "填报的活动审核不通过，理由："+reason);
        } else {
            //admin
            //将活动状态置为2，表示不通过
            int updateNum = activityMapper.updateStatus2ById(actiId);
            if (updateNum == 0) {
                return ResponseResult.FAILED("该计划不存在");
            }
            //发送消息通知老师审核不通过
            //获取该上传者id
            String userId = userActiMapper.selectUserIdByActiId(actiId);
            //发消息通知活动审核不通过
            sendNotice(admin.getAdminId(), userId, "填报的活动审核不通过，理由："+reason);
        }
        return ResponseResult.SUCCESS("审核不通过");
    }

    @Override
    public ResponseResult passProject(String projId) {
        //检查登录状况
        AdminResponse admin = loginService.judgeAdmin(getRequest(), getResponse());
        if (TextUtils.isNull(admin)) {
            return ResponseResult.FAILED("账号未登录");
        }
        //admin
        //将活动状态置为1，表示通过
        int updateNum = projectMapper.updateStatus1ById(projId);
        if (updateNum == 0) {
            return ResponseResult.FAILED("该项目不存在");
        }
        //发送消息通知老师项目审核通过
        //获取该上传者老师的id
        String teaId = userProjMapper.selectTeaIdByProjId(projId);
        sendNotice(admin.getAdminId(), teaId, "申报的项目审核通过");
        //发消息告诉项目成员有申报成功的项目
        List<StuResponse> stuList = studentMapper.selectByProjId(projId);
        for (int i = 0; i < stuList.size(); i++) {
            sendNotice(admin.getAdminId(), stuList.get(i).getStuId(), "有一项项目申报通过");
        }
        return ResponseResult.SUCCESS("审核通过");
    }

    @Override
    public ResponseResult failProject(FailVerifyVo failVerifyVo) {
        //检查登录状况
        AdminResponse admin = loginService.judgeAdmin(getRequest(), getResponse());
        if (TextUtils.isNull(admin)) {
            return ResponseResult.FAILED("账号未登录");
        }
        //登录了
        String projId = failVerifyVo.getId();
        String reason = failVerifyVo.getReason();
        //admin
        //将活动状态置为2，表示不通过
        int updateNum = projectMapper.updateStatus2ById(projId);
        if (updateNum == 0) {
            return ResponseResult.FAILED("该项目不存在");
        }
        //发送消息通知老师审核不通过
        //获取该上传者id
        String teaId = userProjMapper.selectTeaIdByProjId(projId);
        sendNotice(admin.getAdminId(), teaId, "申报的项目审核不通过，理由："+reason);
        //发消息告诉项目成员申报审核不通过
        List<StuResponse> stuList = studentMapper.selectByProjId(projId);
        for (int i = 0; i < stuList.size(); i++) {
            sendNotice(admin.getAdminId(), stuList.get(i).getStuId(), "申报的项目审核不通过，理由："+reason);
        }
        return ResponseResult.SUCCESS("审核不通过");
    }

    @Override
    public ResponseResult getAllUnVerifyThesis(Integer pageNum, Integer pageSize) {
        //检查登录状况
        TeaResponse teacher = loginService.judgeTeacher(getRequest(), getResponse());
        AdminResponse admin = loginService.judgeAdmin(getRequest(), getResponse());
        if (TextUtils.isNull(admin) && TextUtils.isNull(teacher)) {
            return ResponseResult.FAILED("账号未登录");
        }
        //登录了
        if (!TextUtils.isNull(teacher)) {
            //老师
            //获取当前老师所带学生中提交的未审核的学术论文列表
            List<Thesis> thesisList = thesisMapper.selectStatus0ByTeaId(teacher.getTeaId());
            //获取每篇论文登记者信息
            List<Map> resultList = new ArrayList<>();
            for (int i = 0; i < thesisList.size(); i++) {
                StuResponse stuInfo = studentMapper.selectByThesisId(thesisList.get(i).getThesisId());
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("userInfo",stuInfo);
                resultMap.put("thesisInfo",thesisList.get(i));
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
            return ResponseResult.SUCCESS("获取未审核论文登记列表成功").setData(pageInfo);
        } else {
            //admin
            //获取当前管理员所属的学院中未审核的学术活动列表
            List<Thesis> thesisList = thesisMapper.selectStatus0ByCollege(admin.getAdminCollege());
            //获取每条活动上传者信息
            List<Map> resultList = new ArrayList<>();
            for (int i = 0; i < thesisList.size(); i++) {
                TeaResponse teaInfo = teacherMapper.selectByThesisId(thesisList.get(i).getThesisId());
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("userInfo",teaInfo);
                resultMap.put("thesisInfo",thesisList.get(i));
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
            return ResponseResult.SUCCESS("获取未审核论文登记列表成功").setData(pageInfo);
        }
    }

    @Override
    public ResponseResult passThesis(String thesisId) {
        //检查登录状况
        TeaResponse teacher = loginService.judgeTeacher(getRequest(), getResponse());
        AdminResponse admin = loginService.judgeAdmin(getRequest(), getResponse());
        if (TextUtils.isNull(admin) && TextUtils.isNull(teacher)) {
            return ResponseResult.FAILED("账号未登录");
        }
        //登录了
        if (!TextUtils.isNull(teacher)) {
            //老师
            //将论文状态置为1，表示通过
            int updateNum = thesisMapper.updateStatus1ById(thesisId);
            if (updateNum == 0) {
                return ResponseResult.FAILED("该论文不存在");
            }
            //发送消息通知学生审核通过
            //获取该上传者id
            String userId = userThesisMapper.selectUserIdByThesisId(thesisId);
            //发消息通知活动审核通过
            sendNotice(teacher.getTeaId(), userId, "登记的论文审核通过");
        } else {
            //admin
            //将活动状态置为1，表示通过
            int updateNum = thesisMapper.updateStatus1ById(thesisId);
            if (updateNum == 0) {
                return ResponseResult.FAILED("该计划不存在");
            }
            //发送消息通知老师审核通过
            //获取该上传者id
            String userId = userThesisMapper.selectUserIdByThesisId(thesisId);
            //发消息通知活动审核通过
            sendNotice(admin.getAdminId(), userId, "登记的论文审核通过");
        }
        return ResponseResult.SUCCESS("审核通过");
    }

    @Override
    public ResponseResult failThesis(FailVerifyVo failVerifyVo) {
        //检查登录状况
        TeaResponse teacher = loginService.judgeTeacher(getRequest(), getResponse());
        AdminResponse admin = loginService.judgeAdmin(getRequest(), getResponse());
        if (TextUtils.isNull(admin) && TextUtils.isNull(teacher)) {
            return ResponseResult.FAILED("账号未登录");
        }
        //登录了
        String thesisId = failVerifyVo.getId();
        String reason = failVerifyVo.getReason();
        if (!TextUtils.isNull(teacher)) {
            //老师
            //将活动状态置为2，表示不通过
            int updateNum = thesisMapper.updateStatus2ById(thesisId);
            if (updateNum == 0) {
                return ResponseResult.FAILED("该论文不存在");
            }
            //发送消息通知学生审核不通过
            //获取该上传者id
            String userId = userThesisMapper.selectUserIdByThesisId(thesisId);
            //发消息通知活动审核通过
            sendNotice(teacher.getTeaId(), userId, "登记的论文审核不通过，理由："+reason);
        } else {
            //admin
            //将活动状态置为2，表示不通过
            int updateNum = thesisMapper.updateStatus2ById(thesisId);
            if (updateNum == 0) {
                return ResponseResult.FAILED("该计划不存在");
            }
            //发送消息通知老师审核不通过
            //获取该上传者id
            String userId = userThesisMapper.selectUserIdByThesisId(thesisId);
            //发消息通知活动审核不通过
            sendNotice(admin.getAdminId(), userId, "登记的论文审核不通过，理由："+reason);
        }
        return ResponseResult.SUCCESS("审核不通过");
    }

    private void sendNotice(String senderId, String reciveId, String message) {
        Notice notice = new Notice();
        notice.setNotiId(idWorker.nextId()+"");
        notice.setNotiSendId(senderId);
        notice.setNotiReciveId(reciveId);
        notice.setNotiTime(new Date());
        notice.setNotiContent(message);
        notice.setNotiStatus("0");
        noticeMapper.save(notice);
    }
}
