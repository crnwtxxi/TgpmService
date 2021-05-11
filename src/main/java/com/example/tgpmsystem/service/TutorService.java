package com.example.tgpmsystem.service;

import com.example.tgpmsystem.pojo.Allocate;
import com.example.tgpmsystem.response.ResponseResult;
import com.example.tgpmsystem.vo.OpenTutorVo;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.ParseException;

@Service
public interface TutorService {

    //上传开启学院新生年级选导师模块
    ResponseResult setOpenTutor(OpenTutorVo openTutorVo) ;
    //获取老师名额+分页
    ResponseResult getTeaQuota(Integer pageNum, Integer pageSize);
    //更新老师名额
    ResponseResult updateTeaQuota(String quotaId, Long amount);

    //获取今年（新生年级）的选导师模块时间安排
    ResponseResult getOpenTutor();

    //修改新生年级选导师模块开放的时间
    ResponseResult updateOpenTutor(Allocate allocate);

    //学生是否可以显示选导师模块
    ResponseResult judgeTutor() throws ParseException;

    //获取可选导师列表+分页
    ResponseResult getTutors(Integer pageNum, Integer pageSize);

    //选择导师
    ResponseResult appliTutor(String teaId);

    //通过某个学生
    ResponseResult passStu(String stuId);

    //不通过某个学生
    ResponseResult UnpassStu(String stuId);

    //获取待审核学生列表+分页
    ResponseResult getUnverifyStu(Integer pageNum, Integer pageSize);

    //获取选导师模块结束时间
    ResponseResult getTutorEndTime();

    //获取我的选导师记录
    ResponseResult getMyTutorRecord();
}
