package com.example.tgpmsystem.service.impl;

import com.baomidou.mybatisplus.core.toolkit.support.BiIntFunction;
import com.example.tgpmsystem.mapper.NoticeMapper;
import com.example.tgpmsystem.pojo.AdminResponse;
import com.example.tgpmsystem.pojo.Notice;
import com.example.tgpmsystem.pojo.StuResponse;
import com.example.tgpmsystem.pojo.TeaResponse;
import com.example.tgpmsystem.response.ResponseResult;
import com.example.tgpmsystem.service.LoginService;
import com.example.tgpmsystem.service.NoticeService;
import com.example.tgpmsystem.utils.TextUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private LoginService loginService;

    @Resource
    private NoticeMapper noticeMapper;

    private HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getRequest();
    }

    private HttpServletResponse getResponse() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getResponse();
    }

    @Override
    public ResponseResult getNotices(Integer pageNum, Integer pageSize) {
        //检查当前登录用户
        StuResponse student =  loginService.judgeStudent(getRequest(), getResponse());
        TeaResponse teacher = loginService.judgeTeacher(getRequest(), getResponse());
        AdminResponse admin = loginService.judgeAdmin(getRequest(), getResponse());
        //如果三者为空则没用用户登录
        if (TextUtils.isNull(student) && TextUtils.isNull(teacher) && TextUtils.isNull(admin)) {
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        //有一方登录了
        //到数据库中查notice表，取出里面所有noti_recive_id为当前用户的消息通知
        if (!TextUtils.isNull(student)) {
            PageHelper.startPage(pageNum, pageSize);
            List<Notice> notices = noticeMapper.selectByReciveId(student.getStuId());
            PageInfo<Notice> pageInfo = new PageInfo<>(notices);
            return ResponseResult.SUCCESS("消息通知列表获取成功").setData(pageInfo);
        } else if (!TextUtils.isNull(teacher)) {
            PageHelper.startPage(pageNum, pageSize);
            List<Notice> notices = noticeMapper.selectByReciveId(teacher.getTeaId());
            PageInfo<Notice> pageInfo = new PageInfo<>(notices);
            return ResponseResult.SUCCESS("消息通知列表获取成功").setData(pageInfo);
        } else {
            PageHelper.startPage(pageNum, pageSize);
            List<Notice> notices = noticeMapper.selectByReciveId(admin.getAdminId());
            PageInfo<Notice> pageInfo = new PageInfo<>(notices);
            return ResponseResult.SUCCESS("消息通知列表获取成功").setData(pageInfo);
        }
    }

    @Override
    public ResponseResult readNotice(String  noticeId) {
        //检查当前登录用户
        StuResponse student =  loginService.judgeStudent(getRequest(), getResponse());
        TeaResponse teacher = loginService.judgeTeacher(getRequest(), getResponse());
        AdminResponse admin = loginService.judgeAdmin(getRequest(), getResponse());
        //如果三者为空则没用用户登录
        if (TextUtils.isNull(student) && TextUtils.isNull(teacher) && TextUtils.isNull(admin)) {
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        //有一方登录了
        //检查数据
        if (TextUtils.isEmpty(noticeId)) {
            return ResponseResult.FAILED("数据为空");
        }
        //通过传进来的noticeId,将该条消息状态置为已读（0未读/1已读）
        int update = noticeMapper.updateStatusById(noticeId);
        if (update == 0) {
            return ResponseResult.FAILED("该消息不存在");
        }
        return ResponseResult.SUCCESS("消息已读");
    }

    @Override
    public ResponseResult deleteNotice(String  noticeId) {
        //检查当前登录用户
        StuResponse student =  loginService.judgeStudent(getRequest(), getResponse());
        TeaResponse teacher = loginService.judgeTeacher(getRequest(), getResponse());
        AdminResponse admin = loginService.judgeAdmin(getRequest(), getResponse());
        //如果三者为空则没用用户登录
        if (TextUtils.isNull(student) && TextUtils.isNull(teacher) && TextUtils.isNull(admin)) {
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        //有一方登录了
        //检查数据
        if (TextUtils.isEmpty(noticeId)) {
            return ResponseResult.FAILED("数据为空");
        }
        //通过传进来的noticeId,将该条消息删除
        int delete = noticeMapper.deleteById(noticeId);
        if (delete == 0) {
            return ResponseResult.FAILED("该消息不存在");
        }
        return ResponseResult.SUCCESS("消息已删除");
    }

    @Override
    public ResponseResult unreadNoticesNum() {
        //检查当前登录用户
        StuResponse student =  loginService.judgeStudent(getRequest(), getResponse());
        TeaResponse teacher = loginService.judgeTeacher(getRequest(), getResponse());
        AdminResponse admin = loginService.judgeAdmin(getRequest(), getResponse());
        //如果三者为空则没用用户登录
        if (TextUtils.isNull(student) && TextUtils.isNull(teacher) && TextUtils.isNull(admin)) {
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        //有一方登录了
        //到数据库查询status为0的当前用户Id消息通知的条数
        if (!TextUtils.isNull(student)) {
            List<Notice> notices = noticeMapper.selectStatus0ByReciveId(student.getStuId());
            return ResponseResult.SUCCESS("未读消息通知数量获取成功").setData(notices.size());
        } else if (!TextUtils.isNull(teacher)) {
            List<Notice> notices = noticeMapper.selectStatus0ByReciveId(teacher.getTeaId());
            return ResponseResult.SUCCESS("未读消息通知数量获取成功").setData(notices.size());
        } else {
            List<Notice> notices = noticeMapper.selectStatus0ByReciveId(admin.getAdminId());
            return ResponseResult.SUCCESS("未读消息通知数量获取成功").setData(notices.size());
        }
    }
}
