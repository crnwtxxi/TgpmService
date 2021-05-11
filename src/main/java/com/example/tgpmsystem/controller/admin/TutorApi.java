package com.example.tgpmsystem.controller.admin;

import com.example.tgpmsystem.pojo.Allocate;
import com.example.tgpmsystem.response.ResponseResult;
import com.example.tgpmsystem.service.TutorService;
import com.example.tgpmsystem.vo.OpenTutorVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;

@Slf4j
@RestController
@RequestMapping("/tutor")
public class TutorApi {

    @Resource
    private TutorService tutorService;

    //上传开启学院新生年级选导师模块
    @PostMapping("/setOpenTutor")
    public ResponseResult setOpenTutor(@RequestBody OpenTutorVo openTutorVo) {
        return tutorService.setOpenTutor(openTutorVo);
    }

    //获取老师名额+分页
    @GetMapping("/getTeaQuota/{pageNum}/{pageSize}")
    public ResponseResult getTeaQuota(@PathVariable("pageNum") Integer pageNum,
                                      @PathVariable("pageSize") Integer pageSize) {
        return tutorService.getTeaQuota(pageNum, pageSize);
    }

    //更新老师名额
    @GetMapping("/updateTeaQuota/{quotaId}/{amount}")
    public ResponseResult updateTeaQuota(@PathVariable("quotaId") String quotaId,
                                         @PathVariable("amount") Long amount) {
        return tutorService.updateTeaQuota(quotaId, amount);
    }

    //----------------------------------------------------

    //获取今年（新生年级）的选导师模块时间安排
    @GetMapping("/getOpenTutor")
    public ResponseResult getOpenTutor() {
        return tutorService.getOpenTutor();
    }

    //修改新生年级选导师模块开放的时间
    @PostMapping("/updateOpenTutor")
    public ResponseResult updateOpenTutor(@RequestBody Allocate allocate) {
        return tutorService.updateOpenTutor(allocate);
    }

    //学生端
    //学生是否可以显示选导师模块
    @GetMapping("/judgeTutor")
    public ResponseResult judgeTutor() throws ParseException {
        return tutorService.judgeTutor();
    }

    //获取我的选导师记录
    @GetMapping("/getMyTutorRecord")
    public ResponseResult getMyTutorRecord() {
        return tutorService.getMyTutorRecord();
    }


    //获取可选导师列表+分页
    @GetMapping("/getTutors/{pageNum}/{pageSize}")
    public ResponseResult getTutors(@PathVariable("pageNum") Integer pageNum,
                                    @PathVariable("pageSize") Integer pageSize) {
        return tutorService.getTutors(pageNum, pageSize);
    }

    //选择导师
    @GetMapping("/appliTutor/{teaId}")
    public ResponseResult appliTutor(@PathVariable("teaId") String teaId) {
        return tutorService.appliTutor(teaId);
    }

    //老师端
    //通过某个学生
    @GetMapping("/passStu/{stuId}")
    public ResponseResult passStu(@PathVariable("stuId") String stuId) {
        return tutorService.passStu(stuId);
    }

    //不通过某个学生
    @GetMapping("/UnpassStu/{stuId}")
    public ResponseResult UnpassStu(@PathVariable("stuId") String stuId) {
        return tutorService.UnpassStu(stuId);
    }

    //获取待审核学生列表+分页
    @GetMapping("/getUnverifyStu/{pageNum}/{pageSize}")
    public ResponseResult getUnverifyStu(@PathVariable("pageNum") Integer pageNum,
                                    @PathVariable("pageSize") Integer pageSize) {
        return tutorService.getUnverifyStu(pageNum, pageSize);
    }

    //获取结束时间
    @GetMapping("/getTutorEndTime")
    public ResponseResult getTutorEndTime() {
        return tutorService.getTutorEndTime();
    }

}
