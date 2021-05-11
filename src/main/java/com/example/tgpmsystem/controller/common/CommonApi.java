package com.example.tgpmsystem.controller.common;

import com.example.tgpmsystem.pojo.*;
import com.example.tgpmsystem.response.ResponseResult;
import com.example.tgpmsystem.service.*;
import com.example.tgpmsystem.vo.FailVerifyVo;
import com.example.tgpmsystem.vo.UpdatePwdVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonApi {

    @Resource
    private LoginService loginService;

    @Resource
    private NoticeService noticeService;

    @Resource
    private ViewTaskService viewTaskService;

    @Resource
    private ReportService reportService;

    @Resource
    private VerifyService verifyService;

    @Resource
    private ActivityService activityService;

    @Resource
    private ProjectService projectService;

    @Resource
    private ThesisService thesisService;

    @Resource
    private CommonService commonService;

    //检查登录
    @GetMapping("/checkLogined")
    public ResponseResult checkLogined(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return loginService.checkLogined(request, response);
    }

    @PostMapping("/student-login")
    public ResponseResult studentLogin(@RequestBody Student student, HttpServletResponse response) throws Exception {
        return loginService.studentLogin(student, response);
    }

    @PostMapping("/teacher-login")
    public ResponseResult teacherLogin(@RequestBody Teacher teacher, HttpServletResponse response) throws Exception {
        return loginService.teacherLogin(teacher, response);
    }

    @PostMapping("/admin-login")
    public ResponseResult adminLogin(@RequestBody Administrator admin, HttpServletResponse response) throws Exception {
        return loginService.adminLogin(admin, response);
    }

    //退出登录
    @GetMapping("/logout")
    public ResponseResult logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return loginService.logout(request, response);
    }

    //修改密码  未实现
    @PostMapping("/updatePassword")
    public ResponseResult updatePassword(@RequestBody UpdatePwdVo pwdVo) {
        return commonService.updatePassword(pwdVo);
    }

    //修改邮箱  未实现
    @PostMapping("/updateEmail")
    public ResponseResult updateEmail(@RequestBody String newEmail) {
        return commonService.updateEmail(newEmail);
    }

    //获取消息通知列表
    @GetMapping("/getNotices/{pageNum}/{pageSize}")
    public ResponseResult getNotices(@PathVariable("pageNum") Integer pageNum,
                                     @PathVariable("pageSize") Integer pageSize) {
        return noticeService.getNotices(pageNum, pageSize);
    }

    //消息通知已读
    @GetMapping("/readNotice/{noticeId}")
    public ResponseResult readNotice(@PathVariable("noticeId")String  noticeId) {
        return noticeService.readNotice(noticeId);
    }

    //删除一条消息通知
    @GetMapping("/deleteNotice/{noticeId}")
    public ResponseResult deleteNotice(@PathVariable("noticeId")String  noticeId) {
        return noticeService.deleteNotice(noticeId);
    }

    //未读消息条数
    @GetMapping("/unreadNoticesNum")
    public ResponseResult unreadNoticesNum() {
        return noticeService.unreadNoticesNum();
    }

    //查看某一个计划的详情
    @GetMapping("/viewPlan/{planId}")
    public ResponseResult viewPlan(@PathVariable("planId") String planId) {
        return viewTaskService.viewPlan(planId);
    }

    //查看某一个学术活动的详情
    @GetMapping("/viewActivity/{actiId}")
    public ResponseResult viewActivity(@PathVariable("actiId") String actiId) {
        return viewTaskService.viewActivity(actiId);
    }

    //查看某一个科研项目的详情
    @GetMapping("/viewProject/{projId}")
    public ResponseResult viewProject(@PathVariable("projId") String projId) {
        return viewTaskService.viewProject(projId);
    }

    //查看某一个论文的详情
    @GetMapping("/viewThesis/{thesisId}")
    public ResponseResult viewThesis(@PathVariable("thesisId") String thesisId) {
        return viewTaskService.viewThesis(thesisId);
    }

    //填报学术活动，导师和学生共用这个接口
    @PostMapping("/reportActivity")
    public ResponseResult reportActivity(@RequestBody Activity activity) {
        return reportService.reportActivity(activity);
    }

    //登记论文，导师和学生共用这个接口
    @PostMapping("/reportThesis")
    public ResponseResult reportThesis(@RequestBody Thesis thesis) {
        return reportService.reportThesis(thesis);
    }

    //2、学术活动审核
    //2-1、获取需要审核的学术活动列表
    @GetMapping("/getAllUnVerifyActivity/{pageNum}/{pageSize}")
    public ResponseResult getAllUnVerifyActivity(@PathVariable("pageNum") Integer pageNum,
                                                 @PathVariable("pageSize") Integer pageSize) {
        return verifyService.getAllUnVerifyActivity(pageNum, pageSize);
    }

    //通过活动的审核
    @GetMapping("/passActivity/{actiId}")
    public ResponseResult passActivity(@PathVariable("actiId") String actiId) {
        return verifyService.passActivity(actiId);
    }

    //不通过活动的审核
    @PostMapping("/failActivity")
    public ResponseResult failActivity(@RequestBody FailVerifyVo failVerifyVo) {
        return verifyService.failActivity(failVerifyVo);
    }

    //通过科研项目的审核
    @GetMapping("/passProject/{projId}")
    public ResponseResult passProject(@PathVariable("projId") String projId) {
        return verifyService.passProject(projId);
    }

    //不通过科研项目的审核
    @PostMapping("/failProject")
    public ResponseResult failProject(@RequestBody FailVerifyVo failVerifyVo) {
        return verifyService.failProject(failVerifyVo);
    }

    //4、学术论文审核
    //4-1、获取需要审核的论文列表
    @GetMapping("/getAllUnVerifyThesis/{pageNum}/{pageSize}")
    public ResponseResult getAllUnVerifyThesis(@PathVariable("pageNum") Integer pageNum,
                                               @PathVariable("pageSize") Integer pageSize) {
        return verifyService.getAllUnVerifyThesis(pageNum, pageSize);
    }

    //通过论文的审核
    @GetMapping("/passThesis/{thesisId}")
    public ResponseResult passThesis(@PathVariable("thesisId") String thesisId) {
        return verifyService.passThesis(thesisId);
    }

    //不通过论文的审核
    @PostMapping("/failThesis")
    public ResponseResult failThesis(@RequestBody FailVerifyVo failVerifyVo) {
        return verifyService.failThesis(failVerifyVo);
    }

    //查看我的活动记录
    //通过的
    @GetMapping("/getAllPassActi/{pageNum}/{pageSize}")
    public ResponseResult getAllPassActi(@PathVariable("pageNum") Integer pageNum,
                                         @PathVariable("pageSize") Integer pageSize) {
        return activityService.getAllPassActi(pageNum, pageSize);
    }
    //没通过的
    @GetMapping("/getAllUnpassActi/{pageNum}/{pageSize}")
    public ResponseResult getAllUnpassActi(@PathVariable("pageNum") Integer pageNum,
                                           @PathVariable("pageSize") Integer pageSize) {
        return activityService.getAllUnpassActi(pageNum, pageSize);
    }

    //查看我的项目记录
    //通过的
    @GetMapping("/getAllPassProj/{pageNum}/{pageSize}")
    public ResponseResult getAllPassProj(@PathVariable("pageNum") Integer pageNum,
                                         @PathVariable("pageSize") Integer pageSize) {
        return projectService.getAllPassproj(pageNum, pageSize);
    }
    //不通过的
    @GetMapping("/getAllUnpassProj/{pageNum}/{pageSize}")
    public ResponseResult getAllUnpassProj(@PathVariable("pageNum") Integer pageNum,
                                         @PathVariable("pageSize") Integer pageSize) {
        return projectService.getAllUnpassproj(pageNum, pageSize);
    }

    //查看我的项目记录
    //通过的
    @GetMapping("/getAllPassThesis/{pageNum}/{pageSize}")
    public ResponseResult getAllPassThesis(@PathVariable("pageNum") Integer pageNum,
                                         @PathVariable("pageSize") Integer pageSize) {
        return thesisService.getAllPassThesis(pageNum, pageSize);
    }
    //没通过的
    @GetMapping("/getAllUnpassThesis/{pageNum}/{pageSize}")
    public ResponseResult getAllUnpassThesis(@PathVariable("pageNum") Integer pageNum,
                                           @PathVariable("pageSize") Integer pageSize) {
        return thesisService.getAllUnpassThesis(pageNum, pageSize);
    }

    //查看一个公告详情
    @GetMapping("/viewAnnounce/{annoId}")
    public ResponseResult viewAnnounce(@PathVariable("annoId") String annoId) {
        return viewTaskService.viewAnnounce(annoId);
    }

    //2、获取所有公告列表
    @GetMapping("/getAllAnnounce/{pageNum}/{pageSize}")
    public ResponseResult getAllAnnounce(@PathVariable("pageNum") Integer pageNum,
                                         @PathVariable("pageSize") Integer pageSize) {
        return commonService.getAllAnnounce(pageNum, pageSize);
    }

    //查看个人信息详情
    // ------------------------------------------------------
    @GetMapping("/viewStuInfo/{stuId}")
    public ResponseResult viewStuInfo(@PathVariable("stuId") String stuId) {
        return viewTaskService.viewStuInfo(stuId);
    }

    @GetMapping("/viewTeaInfo/{teaId}")
    public ResponseResult viewTeaInfo(@PathVariable("teaId") String teaId) {
        return viewTaskService.viewTeaInfo(teaId);
    }

    @GetMapping("/getMyActiList/{userId}/{pageNum}/{pageSize}")
    public ResponseResult getMyActiList(@PathVariable("userId") String userId,
                                        @PathVariable("pageNum") Integer pageNum,
                                        @PathVariable("pageSize") Integer pageSize) {
        return viewTaskService.getMyActiList(userId, pageNum, pageSize);
    }

    @GetMapping("/getMyProjListS/{stuId}/{pageNum}/{pageSize}")
    public ResponseResult getMyProjListS(@PathVariable("stuId") String stuId,
                                         @PathVariable("pageNum") Integer pageNum,
                                         @PathVariable("pageSize") Integer pageSize) {
        return viewTaskService.getMyProjListS(stuId, pageNum, pageSize);
    }

    @GetMapping("/getMyProjListT/{teaId}/{pageNum}/{pageSize}")
    public ResponseResult getMyProjListT(@PathVariable("teaId") String teaId,
                                         @PathVariable("pageNum") Integer pageNum,
                                         @PathVariable("pageSize") Integer pageSize) {
        return viewTaskService.getMyProjListT(teaId, pageNum, pageSize);
    }

    @GetMapping("/getMyThesisList/{userId}/{pageNum}/{pageSize}")
    public ResponseResult getMyThesisList(@PathVariable("userId") String userId,
                                          @PathVariable("pageNum") Integer pageNum,
                                          @PathVariable("pageSize") Integer pageSize) {
        return viewTaskService.getMyThesisList(userId, pageNum, pageSize);
    }

}
