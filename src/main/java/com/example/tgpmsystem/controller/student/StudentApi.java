package com.example.tgpmsystem.controller.student;

import com.example.tgpmsystem.response.ResponseResult;
import com.example.tgpmsystem.service.ActivityService;
import com.example.tgpmsystem.service.PlanService;
import com.example.tgpmsystem.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/student")
public class StudentApi {

    @Resource
    private PlanService planService;

    @Resource
    private StudentService studentService;

    @Resource
    private ActivityService activityService;

    //查看导师
    @GetMapping("/getMyTutor")
    public ResponseResult getMyTutor() {
        return studentService.getMyTutor();
    }

    //查看计划
    @GetMapping("/getMyPlans/{pageNum}/{pageSize}")
    public ResponseResult getMyPlans(@PathVariable("pageNum") Integer pageNum,
                                     @PathVariable("pageSize") Integer pageSize) {
        return planService.getMyPlans(pageNum ,pageSize);
    }

}
