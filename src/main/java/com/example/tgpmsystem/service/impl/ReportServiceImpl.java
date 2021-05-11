package com.example.tgpmsystem.service.impl;

import com.example.tgpmsystem.mapper.*;
import com.example.tgpmsystem.pojo.*;
import com.example.tgpmsystem.response.ResponseResult;
import com.example.tgpmsystem.service.LoginService;
import com.example.tgpmsystem.service.ReportService;
import com.example.tgpmsystem.utils.IdWorker;
import com.example.tgpmsystem.utils.TextUtils;
import com.example.tgpmsystem.vo.UploadProjVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ReportServiceImpl implements ReportService {

    @Resource
    LoginService loginService;

    @Resource
    private AdminMapper adminMapper;

    @Resource
    private ActivityMapper activityMapper;

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private ThesisMapper thesisMapper;

    @Resource
    private NoticeMapper noticeMapper;

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private UserActiMapper userActiMapper;

    @Resource
    private UserProjMapper userProjMapper;

    @Resource
    private UserThesisMapper userThesisMapper;

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
    public ResponseResult reportActivity(Activity activity) {
        //检查登陆状况，判断当前用户是老师还是学生
        TeaResponse teacher = loginService.judgeTeacher(getRequest(), getResponse());
        StuResponse student = loginService.judgeStudent(getRequest(), getResponse());
        //如果两者都为空则没登陆
        if (TextUtils.isNull(teacher) && TextUtils.isNull(student)) {
            return ResponseResult.FAILED("账号未登录");
        }
        //登录了
        //检查数据
        if (TextUtils.isEmpty(activity.getActiTitle()) || TextUtils.isEmpty(activity.getActiHost()) ||
                TextUtils.isEmpty(activity.getActiType()) || TextUtils.isEmpty(activity.getActiForm()) ||
                TextUtils.isNull(activity.getActiDate()) || TextUtils.isEmpty(activity.getActiDesc())) {
            return ResponseResult.FAILED("数据存在空值");
        }
        //存一条activity数据到表中，将状态设置为0未审核
        //发送消息通知给审核者，老师则发给学院的管理员，学生则发给自己的导师，默认设置消息的状态为0未读
        if (!TextUtils.isNull(teacher)) {
            //登录的是老师
            //存到数据库activity中,在user_acti表中也存入关系
            saveActivity(activity, teacher.getTeaId());
            //接收者Id为老师所在学院的管理员
            List<AdminResponse> adminList = adminMapper.selectByCollege(teacher.getTeaCollege());
            for (int i = 0; i < adminList.size(); i++) {
                sendNotice(teacher.getTeaId(), adminList.get(i).getAdminId(), "有一份活动待审核");
            }
        } else {
            //登录的是学生
            saveActivity(activity, student.getStuId());
            //接收者是自己的导师
            String teaId = studentMapper.selectTeaIdByStuId(student.getStuId());
            sendNotice(student.getStuId(), teaId, "有一份活动待审核");
        }
        return ResponseResult.SUCCESS("学术活动填报成功，已提交审核。");
    }

    @Override
    public ResponseResult reportProject(UploadProjVo projectVo) {
        //检查登陆状况
        TeaResponse teacher = loginService.judgeTeacher(getRequest(), getResponse());
        if (TextUtils.isNull(teacher)) {
            return ResponseResult.FAILED("账号未登录");
        }
        //登陆了
        //检查数据
        if (TextUtils.isEmpty(projectVo.getProjTitle()) || TextUtils.isEmpty(projectVo.getProjType()) ||
                TextUtils.isEmpty(projectVo.getProjForm()) || TextUtils.isNull(projectVo.getProjOndate()) ||
                TextUtils.isNull(projectVo.getProjOffdate()) || TextUtils.isEmpty(projectVo.getProjDesc())) {
            return ResponseResult.FAILED("数据存在空值");
        }
        //存数据到project表中，同时user_proj关系表也需要存
        //user_proj表中  项目成员是学生Id  需要每个学生id存一条数据
        saveProject(projectVo, teacher.getTeaId(), projectVo.getStudentList());
        //发消息通知审核
        //接收者Id为老师所在学院的管理员
        List<AdminResponse> adminList = adminMapper.selectByCollege(teacher.getTeaCollege());
        for (int i = 0; i < adminList.size(); i++) {
            sendNotice(teacher.getTeaId(), adminList.get(i).getAdminId(), "有一份科研项目申报待审核");
        }
        //通知项目成员，有关于ta的项目正在在申报待审核
        List<String> studentList = projectVo.getStudentList();
        for (int j = 0; j < studentList.size(); j++) {
            sendNotice(teacher.getTeaId(), studentList.get(j), "有一份关于你的项正在申报待审核");
        }
        return ResponseResult.SUCCESS("科研项目填报成功，已提交审核。");
    }

    @Override
    public ResponseResult reportThesis(Thesis thesis) {
        //检查登陆状况，判断当前用户是老师还是学生
        TeaResponse teacher = loginService.judgeTeacher(getRequest(), getResponse());
        StuResponse student = loginService.judgeStudent(getRequest(), getResponse());
        //如果两者都为空则没登陆
        if (TextUtils.isNull(teacher) && TextUtils.isNull(student)) {
            return ResponseResult.FAILED("账号未登录");
        }
        //登录了
        //检查数据
        if (TextUtils.isEmpty(thesis.getThesisTitle()) || TextUtils.isEmpty(thesis.getThesisPeriod()) ||
                TextUtils.isEmpty(thesis.getThesisName()) || TextUtils.isEmpty(thesis.getThesisSeque()) ||
                TextUtils.isEmpty(thesis.getThesisType()) || TextUtils.isEmpty(thesis.getThesisLevel()) ||
                TextUtils.isNull(thesis.getThesisDate()) || TextUtils.isEmpty(thesis.getThesisDesc())) {
            return ResponseResult.FAILED("数据存在空值");
        }
        //存一条activity数据到表中，将状态设置为0未审核，同时存入user_thesis关系表
        //发送消息通知给审核者，老师则发给学院的管理员，学生则发给自己的导师，默认设置消息的状态为0未读
        if (!TextUtils.isNull(teacher)) {
            //登录的是老师
            saveThesis(thesis, teacher.getTeaId());
            //接收者Id为老师所在学院的管理员
            List<AdminResponse> adminList = adminMapper.selectByCollege(teacher.getTeaCollege());
            for (int i = 0; i < adminList.size(); i++) {
                sendNotice(teacher.getTeaId(), adminList.get(i).getAdminId(), "有一份学术论文待审核");
            }
        } else {
            //登录的是学生
            saveThesis(thesis, student.getStuId());
            //接收者是自己的导师
            String teaId = studentMapper.selectTeaIdByStuId(student.getStuId());
            sendNotice(student.getStuId(), teaId ,"有一份学术论文待审核");
        }
        return ResponseResult.SUCCESS("学术论文登记成功，已提交审核。");
    }

    private void saveActivity(Activity activity, String userId) {
        //存活动
        Activity activity1 = new Activity();
        activity1.setActiId(idWorker.nextId()+"");
        activity1.setActiTitle(activity.getActiTitle());
        activity1.setActiDate(activity.getActiDate());
        activity1.setActiHost(activity.getActiHost());
        activity1.setActiType(activity.getActiType());
        activity1.setActiForm(activity.getActiForm());
        activity1.setActiDesc(activity.getActiDesc());
        activity1.setActiStatus("0");
        activityMapper.save(activity1);
        //存用户活动关系
        UserActi userActi = new UserActi();
        userActi.setUserActiId(idWorker.nextId()+"");
        userActi.setUserId(userId);
        userActi.setActiId(activity1.getActiId());
        userActiMapper.save(userActi);
    }

    private void saveProject(UploadProjVo project, String teaId, List<String> stuIdList) {
        //存项目
        Project project1 = new Project();
        project1.setProjId(idWorker.nextId()+"");
        project1.setProjTitle(project.getProjTitle());
        project1.setProjType(project.getProjType());
        project1.setProjForm(project.getProjForm());
        project1.setProjOndate(project.getProjOndate());
        project1.setProjOffdate(project.getProjOffdate());
        project1.setProjDesc(project.getProjDesc());
        project1.setProjStatus("0");
        projectMapper.save(project1);
        //存项目和用户关系
        for (int i = 0; i < stuIdList.size(); i++) {
            UserProj userProj = new UserProj();
            userProj.setUserProjId(idWorker.nextId()+"");
            userProj.setProjId(project1.getProjId());
            userProj.setTeaId(teaId);
            userProj.setStuId(stuIdList.get(i));
            userProjMapper.save(userProj);
        }
    }

    private void saveThesis(Thesis thesis, String userId) {
        //存论文发表信息
        Thesis thesis1 = new Thesis();
        thesis1.setThesisId(idWorker.nextId()+"");
        thesis1.setThesisTitle(thesis.getThesisTitle());
        thesis1.setThesisDate(thesis.getThesisDate());
        thesis1.setThesisPeriod(thesis.getThesisPeriod());
        thesis1.setThesisName(thesis.getThesisName());
        thesis1.setThesisSeque(thesis.getThesisSeque());
        thesis1.setThesisType(thesis.getThesisType());
        thesis1.setThesisLevel(thesis.getThesisLevel());
        thesis1.setThesisDesc(thesis.getThesisDesc());
        thesis1.setThesisStatus("0");
        thesisMapper.save(thesis1);
        //存用户论文关系
        UserThesis userThesis = new UserThesis();
        userThesis.setUserThesisId(idWorker.nextId()+"");
        userThesis.setThesisId(thesis1.getThesisId());
        userThesis.setUserId(userId);
        userThesisMapper.save(userThesis);
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
