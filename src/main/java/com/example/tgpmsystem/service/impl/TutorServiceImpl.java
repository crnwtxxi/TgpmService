package com.example.tgpmsystem.service.impl;

import com.example.tgpmsystem.mapper.*;
import com.example.tgpmsystem.pojo.*;
import com.example.tgpmsystem.response.ResponseResult;
import com.example.tgpmsystem.service.LoginService;
import com.example.tgpmsystem.service.TutorService;
import com.example.tgpmsystem.utils.IdWorker;
import com.example.tgpmsystem.utils.TextUtils;
import com.example.tgpmsystem.vo.OpenTutorVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.rmi.MarshalledObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

@Slf4j
@Service
public class TutorServiceImpl implements TutorService {

    @Resource
    private LoginService loginService;

    @Resource
    private AllocateMapper allocateMapper;

    @Resource
    private TeacherMapper teacherMapper;

    @Resource
    private QuotaMapper quotaMapper;

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private RecordMapper recordMapper;

    @Resource
    private NoticeMapper noticeMapper;

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
    public ResponseResult setOpenTutor(OpenTutorVo openTutorVo) {
        //检查登录状况
        AdminResponse admin = loginService.judgeAdmin(getRequest(), getResponse());
        if (TextUtils.isNull(admin)) {
            return ResponseResult.FAILED("账号未登录");
        }
        //登录了
        log.info(openTutorVo.toString());
        //检查数据
        if (TextUtils.isNull(openTutorVo.getAlloOndate()) || TextUtils.isNull(openTutorVo.getAlloOffdate()) ||
                TextUtils.isNull(openTutorVo.getQuotaAmount())) {
            return ResponseResult.FAILED("数据存在空值");
        }
        //存表里
        //allocate表
        Allocate allocate = new Allocate();
        allocate.setAlloId(idWorker.nextId()+"");
        allocate.setAlloCollege(admin.getAdminCollege());
        allocate.setAlloGrade(openTutorVo.getAlloGrade());
        allocate.setAlloOndate(openTutorVo.getAlloOndate());
        allocate.setAlloOffdate(openTutorVo.getAlloOffdate());
        allocateMapper.save(allocate);
        //quota表，学院的每一个老师存一条
        List<TeaResponse> teaList = teacherMapper.selectByCollege(admin.getAdminCollege());
        for (int i = 0; i < teaList.size(); i++) {
            Quota quota = new Quota();
            quota.setQuotaId(idWorker.nextId()+"");
            quota.setTeaId(teaList.get(i).getTeaId());
            quota.setQuotaGrade(openTutorVo.getAlloGrade());
            quota.setQuotaAmount(openTutorVo.getQuotaAmount());
            quotaMapper.save(quota);
        }
        return ResponseResult.SUCCESS("设置选导师模块成功，该功能会定时对学院新生开放");
    }

    @Override
    public ResponseResult getTeaQuota(Integer pageNum, Integer pageSize) {
        //检查登录状况
        AdminResponse admin = loginService.judgeAdmin(getRequest(), getResponse());
        if (TextUtils.isNull(admin)) {
            return ResponseResult.FAILED("账号未登录");
        }
        //登录了
        List<TeaResponse> teaList = teacherMapper.selectByCollege(admin.getAdminCollege());
        List<Map> resultList = new ArrayList<>();
        for (int i = 0; i < teaList.size(); i++) {
            //去数据库查，当前年级老师的名额
            Calendar date = Calendar.getInstance();
            String year = String.valueOf(date.get(Calendar.YEAR));//当前年级
            Quota quota = quotaMapper.selectAmountByTeaIdAndGrade(teaList.get(i).getTeaId(), year);
            Map<String,Object> map = new HashMap<>();
            map.put("teaId", teaList.get(i).getTeaId());
            map.put("teaName", teaList.get(i).getTeaName());
            map.put("teaTno", teaList.get(i).getTeaTno());
            map.put("quotaId", quota.getQuotaId());
            map.put("amount", quota.getQuotaAmount());
            resultList.add(map);
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
        return ResponseResult.SUCCESS("获取成功").setData(pageInfo);
    }

    @Override
    public ResponseResult updateTeaQuota(String quotaId, Long amount) {
        //检查登录状况
        AdminResponse admin = loginService.judgeAdmin(getRequest(), getResponse());
        if (TextUtils.isNull(admin)) {
            return ResponseResult.FAILED("账号未登录");
        }
        //登陆了
        quotaMapper.updateById(quotaId, amount);
        return ResponseResult.SUCCESS("修改成功");
    }

    @Override
    public ResponseResult getOpenTutor() {
        //检查登录状况
        AdminResponse admin = loginService.judgeAdmin(getRequest(), getResponse());
        if (TextUtils.isNull(admin)) {
            return ResponseResult.FAILED("账号未登录");
        }
        //登陆了
        Calendar date = Calendar.getInstance();
        String year = String.valueOf(date.get(Calendar.YEAR));//当前年级
        Allocate allocate = allocateMapper.selectByCollegeAndGrade(admin.getAdminCollege(), year);
//        log.info(allocate.toString());
        return ResponseResult.SUCCESS("获取成功").setData(allocate);
    }

    @Override
    public ResponseResult updateOpenTutor(Allocate allocate) {
        //检查登录状况
        AdminResponse admin = loginService.judgeAdmin(getRequest(), getResponse());
        if (TextUtils.isNull(admin)) {
            return ResponseResult.FAILED("账号未登录");
        }
        //登陆了
        log.info(allocate.toString());
        //检查数据
        if (TextUtils.isNull(allocate.getAlloOndate()) || TextUtils.isNull(allocate.getAlloOffdate())) {
            return ResponseResult.FAILED("数据存在空值");
        }
        allocateMapper.updateDateById(allocate.getAlloId(), allocate.getAlloOndate(), allocate.getAlloOffdate());
        return ResponseResult.SUCCESS("设置修改成功");
    }

    @Override
    public ResponseResult judgeTutor() throws ParseException {
        //检查登录状况
        StuResponse student = loginService.judgeStudent(getRequest(), getResponse());
        if (TextUtils.isNull(student)) {
            return ResponseResult.FAILED("账号未登录");
        }
        //登陆了
        Calendar date = Calendar.getInstance();
        String year = String.valueOf(date.get(Calendar.YEAR));//当前年级
        //判断学生的年级是否属于新生年级
        if (!student.getStuAdmiss().equals(year)) {
            return ResponseResult.SUCCESS().setData(false);
        }
        //判断学生所属学院的选导师板块是否开启
        Allocate allocate = allocateMapper.selectByCollegeAndGrade(student.getStuCollege(), student.getStuAdmiss());

        //日期格式
        Date date1 = parse(new Date()+"", "EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        String now_time = format(date1, "yyyy-MM-dd HH:mm:ss", Locale.CHINA);

        Date alloOndate = parse(allocate.getAlloOndate()+"", "EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        String on_time = format(alloOndate, "yyyy-MM-dd HH:mm:ss", Locale.CHINA);

        Date alloOffdate = parse(allocate.getAlloOffdate()+"", "EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        String off_time = format(alloOffdate, "yyyy-MM-dd HH:mm:ss", Locale.CHINA);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date now_date = dateFormat.parse(now_time);
            Date on_date = dateFormat.parse(on_time);
            Date off_date = dateFormat.parse(off_time);

            if (now_date.compareTo(on_date) == 1 && now_date.compareTo(off_date) == -1) {
                //在时间范围内
                return ResponseResult.SUCCESS().setData(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //不在时间范围内
        return ResponseResult.SUCCESS().setData(false);
    }

    private Date parse(String str, String pattern, Locale locale) {
        if(str == null || pattern == null) {
            return null;
        }
        try {
            return new SimpleDateFormat(pattern, locale).parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String format(Date date, String pattern, Locale locale) {
        if(date == null || pattern == null) {
            return null;
        }
        return new SimpleDateFormat(pattern, locale).format(date);
    }


    @Override
    public ResponseResult getTutors(Integer pageNum, Integer pageSize) {
        //检查登录状况
        StuResponse student = loginService.judgeStudent(getRequest(), getResponse());
        if (TextUtils.isNull(student)) {
            return ResponseResult.FAILED("账号未登录");
        }
        //登陆了
        //当前学生所属学院的全部导师
        List<Map> resultList = new ArrayList<>();
        //筛选出学生所在学院的导师
        List<TeaResponse> teaList = teacherMapper.selectByCollege(student.getStuCollege());
        //找出每个老师已选人数，总名额 - 已选人数 = 剩余名额
        for (int i = 0; i < teaList.size(); i++) {
            Map<String, Object> infoMap = new HashMap<>();
            infoMap.put("teaInfo", teaList.get(i));
            //老师总名额
            long totalQuota = quotaMapper.selectAmountByTeaIdAndGrade(teaList.get(i).getTeaId(), student.getStuAdmiss()).getQuotaAmount();
            //已确定人数
            int ensureQuota = studentMapper.countByTeaIdAndGrade(teaList.get(i).getTeaId(), student.getStuAdmiss());
            //int ensureQuota = recordMapper.countStatus1ByTeaIdAndGrade(teaList.get(i).getTeaId(), student.getStuAdmiss());
            //剩余名额
            int surplusQuota = (int)totalQuota - ensureQuota;
            //申请人数
            int appliNum = recordMapper.countStatus0ByTeaIdAndGrade(teaList.get(i).getTeaId(), student.getStuAdmiss());
            infoMap.put("surplusQuota", surplusQuota);
            infoMap.put("appliNum", appliNum);
            //申请人数>=总名额数的老师不显示，即不加到list中
            //剩余名额为0的不显示
            if (appliNum < totalQuota && surplusQuota != 0) {
                resultList.add(infoMap);
            }
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
        return ResponseResult.SUCCESS("获取成功").setData(pageInfo);
    }

    @Override
    public ResponseResult appliTutor(String teaId) {
        //检查登录状况
        StuResponse student = loginService.judgeStudent(getRequest(), getResponse());
        if (TextUtils.isNull(student)) {
            return ResponseResult.FAILED("账号未登录");
        }
        //登陆了
        //往record表存一条记录
        Record record = new Record();
        record.setRecordId(idWorker.nextId()+"");
        record.setRecordStatus("0");
        record.setStuId(student.getStuId());
        record.setTeaId(teaId);
        record.setRecordGrade(student.getStuAdmiss());
        recordMapper.save(record);
        //给老师发消息
        sendNotice(student.getStuId(), teaId, student.getStuName()+"同学正在申请您作为导师，请审核");
        return ResponseResult.SUCCESS("已提交申请，等待导师审核");
    }

    @Override
    public ResponseResult passStu(String stuId) {
        //检查登录状况
        TeaResponse teacher = loginService.judgeTeacher(getRequest(), getResponse());
        if (TextUtils.isNull(teacher)) {
            return ResponseResult.FAILED("账号未登录");
        }
        //登陆了
        //将record表中的状态置1
        recordMapper.updateStatus1ByStuIdAndTeaId(stuId, teacher.getTeaId());
        //绑定导师学生关系
        studentMapper.updateTeaIdByStuId(stuId, teacher.getTeaId());
        //导师姓名
        String teaName = teacherMapper.selectTeaNameByTeaId(teacher.getTeaId());
        //发消息通知学生通过
        sendNotice(teacher.getTeaId(), stuId, "恭喜你与"+teaName+"老师互选成功");
        return ResponseResult.SUCCESS("通过成功");
    }

    @Override
    public ResponseResult UnpassStu(String stuId) {
        //检查登录状况
        TeaResponse teacher = loginService.judgeTeacher(getRequest(), getResponse());
        if (TextUtils.isNull(teacher)) {
            return ResponseResult.FAILED("账号未登录");
        }
        //登陆了
        //将record表中的状态置2
        recordMapper.updateStatus2ByStuIdAndTeaId(stuId, teacher.getTeaId());
        //导师姓名
        String teaName = teacherMapper.selectTeaNameByTeaId(teacher.getTeaId());
        //通知学生
        sendNotice(teacher.getTeaId(), stuId, teaName+"老师没有通过你的申请，请尽快在结束时间前选择其他的导师");
        return ResponseResult.SUCCESS("退回成功");
    }

    @Override
    public ResponseResult getUnverifyStu(Integer pageNum, Integer pageSize) {
        //检查登录状况
        TeaResponse teacher = loginService.judgeTeacher(getRequest(), getResponse());
        if (TextUtils.isNull(teacher)) {
            return ResponseResult.FAILED("账号未登录");
        }
        //登陆了
        Calendar date = Calendar.getInstance();
        String year = String.valueOf(date.get(Calendar.YEAR));//当前年级
        //找出申请了该老师的但未被审核的学生列表
        PageHelper.startPage(pageNum, pageSize);
        List<StuResponse> studentList = studentMapper.selectByRecord(teacher.getTeaId(), year);
        PageInfo<StuResponse> pageInfo = new PageInfo<>(studentList);
        return ResponseResult.SUCCESS("获取成功").setData(pageInfo);
    }

    @Override
    public ResponseResult getTutorEndTime() {
        //检查登录状况
        StuResponse student = loginService.judgeStudent(getRequest(), getResponse());
        if (TextUtils.isNull(student)) {
            return ResponseResult.FAILED("账号未登录");
        }
        //登陆了
        //判断学生所属学院的选导师板块是否开启
        Allocate allocate = allocateMapper.selectByCollegeAndGrade(student.getStuCollege(), student.getStuAdmiss());
        Date alloOffdate = parse(allocate.getAlloOffdate()+"", "EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        String off_time = format(alloOffdate, "yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return ResponseResult.SUCCESS("获取成功").setData(off_time);
    }

    @Override
    public ResponseResult getMyTutorRecord() {
        //检查登录状况
        StuResponse student = loginService.judgeStudent(getRequest(), getResponse());
        if (TextUtils.isNull(student)) {
            return ResponseResult.FAILED("账号未登录");
        }
        //登陆了
        List<Map> resultList = new ArrayList<>();
        //去record
        List<Record> recordList = recordMapper.selectByStuId(student.getStuId());
        for (int i = 0; i < recordList.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("teaInfo", teacherMapper.selectById(recordList.get(i).getTeaId()));
            map.put("recordStatus", recordList.get(i).getRecordStatus());
            resultList.add(map);
        }
        return ResponseResult.SUCCESS("获取成功").setData(resultList);
    }


    private void sendNotice(String sendId, String reciveId, String message) {
        Notice notice = new Notice();
        notice.setNotiId(idWorker.nextId()+"");
        notice.setNotiStatus("0");
        notice.setNotiContent(message);
        notice.setNotiTime(new Date());
        notice.setNotiSendId(sendId);
        notice.setNotiReciveId(reciveId);
        noticeMapper.save(notice);
    }
}
