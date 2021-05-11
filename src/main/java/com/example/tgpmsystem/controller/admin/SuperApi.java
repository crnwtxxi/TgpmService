package com.example.tgpmsystem.controller.admin;

import com.example.tgpmsystem.pojo.Administrator;
import com.example.tgpmsystem.response.ResponseResult;
import com.example.tgpmsystem.service.SuperService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/super")
public class SuperApi {

    @Resource
    private SuperService superService;

    //获取所有管理员列表
    @GetMapping("/getAdminList/{pageNum}/{pageSize}")
    public ResponseResult getAdminList(@PathVariable("pageNum") Integer pageNum,
                                       @PathVariable("pageSize") Integer pageSize) {
        return superService.getAdminList(pageNum, pageSize);
    }

    //删除一个管理员
    @GetMapping("/deleteAdmin/{adminId}")
    public ResponseResult deleteAdmin(@PathVariable("adminId") String adminId) {
        return superService.deleteAdmin(adminId);
    }

    //新增一个管理员
    @PostMapping("/addAdmin")
    public ResponseResult addAdmin(@RequestBody Administrator admin) {
        return superService.addAdmin(admin);
    }

}
