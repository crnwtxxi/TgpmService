package com.example.tgpmsystem.controller.teacher;

import com.example.tgpmsystem.pojo.Project;
import com.example.tgpmsystem.response.ResponseResult;
import com.example.tgpmsystem.service.PlanService;
import com.example.tgpmsystem.service.ReportService;
import com.example.tgpmsystem.service.TeacherService;
import com.example.tgpmsystem.vo.UploadPlanVo;
import com.example.tgpmsystem.vo.UploadProjVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/teacher")
public class TeacherApi {

    @Resource
    PlanService planService;

    @Resource
    private TeacherService teacherService;

    @Resource
    private ReportService reportService;

    //获取自己所带学生
    @GetMapping("/getAllSelfStu/{pageNum}/{pageSize}")
    public ResponseResult getAllSelfStu(@PathVariable("pageNum") Integer pageNum,
                                         @PathVariable("pageSize") Integer pageSize) {
        return teacherService.getAllSelfStu(pageNum, pageSize);
    }

    //培养计划模块
    //1、上传培养计划
    //1-1、获取自己所带的所有学生,用于发布范围
    @GetMapping("/getAllSelfStu")
    public ResponseResult getAllSelfStu() {
        return planService.getAllSelfStu();
    }

    //1-2、提交计划
    @PostMapping("/uploadPlan")
    public ResponseResult uploadPlan(@RequestBody UploadPlanVo planVo) {
        return planService.uploadPlan(planVo);
    }

    //2、查看计划
    //2-1、获取全部自己上传的计划被通过了的列表
    @GetMapping("/getAllPassPlan/{pageNum}/{pageSize}")
    public ResponseResult getAllPassPlan(@PathVariable("pageNum") Integer pageNum,
                                         @PathVariable("pageSize") Integer pageSize) {
        return planService.getAllPassPlan(pageNum, pageSize);
    }

    //2-2、获取全部自己上传的计划的列表，不通过的和待审核的
    @GetMapping("/getAllUnpassPlan/{pageNum}/{pageSize}")
    public ResponseResult getAllUnpassPlan(@PathVariable("pageNum") Integer pageNum,
                                         @PathVariable("pageSize") Integer pageSize) {
        return planService.getAllUnpassPlan(pageNum, pageSize);
    }

    //申报科研项目
    @PostMapping("/reportProject")
    public ResponseResult reportProject(@RequestBody UploadProjVo projectVo) {
        return reportService.reportProject(projectVo);
    }

    //获取全校学生信息
    @GetMapping("/getAllCampusStu")
    public ResponseResult getAllCampusStu() {
        return teacherService.getAllCampusStu();
    }

}
