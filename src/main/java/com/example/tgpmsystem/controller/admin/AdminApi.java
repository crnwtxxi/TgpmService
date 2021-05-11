package com.example.tgpmsystem.controller.admin;


import com.example.tgpmsystem.pojo.Announce;
import com.example.tgpmsystem.response.ResponseResult;
import com.example.tgpmsystem.service.AdminService;
import com.example.tgpmsystem.service.MessageService;
import com.example.tgpmsystem.service.UploadFileService;
import com.example.tgpmsystem.vo.FailVerifyVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminApi {

    @Resource
    private UploadFileService uploadFileService;

    @Resource
    private MessageService messageService;

    @Resource
    private AdminService adminService;

    //公告模块
    //1、发布公告
    @PostMapping("/postAnnounce")
    public ResponseResult postAnnounce(@RequestBody Announce announce) {
        return adminService.postAnnounce(announce);
    }

    //3、删除公告


    //师生信息管理模块
    //1、老师信息上传
    @PostMapping("/teacher-upload")
    public ResponseResult uploadTeaInfo(@RequestBody MultipartFile file) {
        return uploadFileService.teaUpload(file);
    }

    //2、老师信息管理
    //2-1、获取老师信息列表
    @GetMapping("/getAllTeaInfo/{pageNum}/{pageSize}")
    public ResponseResult getAllTeaInfo(@PathVariable("pageNum") Integer pageNum,
                                        @PathVariable("pageSize") Integer pageSize) {
        return messageService.getAllTeaInfo(pageNum, pageSize);
    }

    //2-2、删除一个老师
    @GetMapping("/deleteOneTea/{teaId}")
    public ResponseResult deleteOneTea(@PathVariable("teaId")String teaId) {
        return messageService.deleteOneTea(teaId);
    }

    //2-3、查看该老师的各种详情


    //3、学生信息上传
    @PostMapping("/student-upload")
    public ResponseResult uploadStuInfo(@RequestBody MultipartFile file) {
        return uploadFileService.stuUpload(file);
    }

    //4、学生信息管理
    // 4-1、获取学生信息列表
    @GetMapping("/getAllStuInfo/{pageNum}/{pageSize}")
    public ResponseResult getAllStuInfo(@PathVariable("pageNum") Integer pageNum,
                                        @PathVariable("pageSize") Integer pageSize) {
        return messageService.getAllStuInfo(pageNum, pageSize);
    }

    //4-2、删除一个学生
    @GetMapping("/deleteOneStu/{stuId}")
    public ResponseResult deleteOneStu(@PathVariable("stuId")String  stuId) {
        return messageService.deleteOneStu(stuId);
    }

    //4-3、查看该学生的各种详情

    //事务审核
    //1、培养计划审核
    //1-1、获取需要审核的计划列表
    @GetMapping("/getAllUnVerifyPlan/{pageNum}/{pageSize}")
    public ResponseResult getAllUnVerifyPlan(@PathVariable("pageNum") Integer pageNum,
                                             @PathVariable("pageSize") Integer pageSize) {
        return adminService.getAllUnVerifyPlan(pageNum, pageSize);
    }

    //1-3、通过计划的审核
    @GetMapping("/passPlan/{planId}")
    public ResponseResult passPlan(@PathVariable("planId") String planId) {
        return adminService.passPlan(planId);
    }

    //1-4、不通过计划的审核
    @PostMapping("/failPlan")
    public ResponseResult failPlan(@RequestBody FailVerifyVo failVerifyVo) {
        return adminService.failPlan(failVerifyVo);
    }


    //3、科研项目审核
    //3-1、获取需要审核的科研项目列表
    @GetMapping("/getAllUnVerifyProject/{pageNum}/{pageSize}")
    public ResponseResult getAllUnVerifyProject(@PathVariable("pageNum") Integer pageNum,
                                                 @PathVariable("pageSize") Integer pageSize) {
        return adminService.getAllUnVerifyProject(pageNum, pageSize);
    }

}
