package com.example.tgpmsystem.service.impl;

import com.baomidou.mybatisplus.extension.api.R;
import com.example.tgpmsystem.mapper.*;
import com.example.tgpmsystem.pojo.*;
import com.example.tgpmsystem.response.ResponseResult;
import com.example.tgpmsystem.service.LoginService;
import com.example.tgpmsystem.utils.*;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 查数据库
 */

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    StudentMapper studentMapper;

    @Resource
    TeacherMapper teacherMapper;

    @Resource
    AdminMapper adminMapper;

    @Resource
    RefreshTokenMapper refreshTokenMapper;

    @Resource
    UserRoleMapper userRoleMapper;

    @Resource
    RoleMapper roleMapper;

    @Resource
    RedisUtils redisUtils;

    @Autowired
    IdWorker idWorker;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    /**
     * 检查是否有用户已经登录，只允许登录一个账户，不能同时登录多个
     * 先判断是不是学生，然后判断老师，最后则是管理员
     * @param request
     * @param response
     * @return
     */
    @Override
    public ResponseResult checkLogined(HttpServletRequest request, HttpServletResponse response) {
        //检查登录状况
        StuResponse student = judgeStudent(request, response);
        TeaResponse teacher = judgeTeacher(request, response);
        AdminResponse admin = judgeAdmin(request, response);
        //如果三者任一不为空则表示有用户登录信息，则默认自动跳转到用户界面
        //必须手动退出登录才能跳转到登陆界面，登录别的用户
        //判断三者是否都为空
        if (TextUtils.isNull(student) && TextUtils.isNull(teacher) && TextUtils.isNull(admin)) {
            //允许登录
            return ResponseResult.PERMIT_LOGIN();
        }
        //有一方登录了，找出不为空的
        if (!TextUtils.isNull(student)) {
            return ResponseResult.FORBIDDEN_LOGIN().setData(student).setRole(roleMapper.selectRoleNameByUserId(student.getStuId()));
        } else if (!TextUtils.isNull(teacher)) {
            return ResponseResult.FORBIDDEN_LOGIN().setData(teacher).setRole(roleMapper.selectRoleNameByUserId(teacher.getTeaId()));
        } else {
            return ResponseResult.FORBIDDEN_LOGIN().setData(admin).setRole(roleMapper.selectRoleNameByUserId(admin.getAdminAno()));
        }
    }


    /**
     * 退出登录
     * 拿到token_key
     * 删除redis里对应的token
     * 删除MySQL里对应的refreshToken
     * 删除cookie里的token_key
     * @return
     */
    @Override
    public ResponseResult logout(HttpServletRequest request, HttpServletResponse response) {
        //判断当前要退出的用户是什么身份  学生？老师？管理员？
        StuResponse student = judgeStudent(request, response);
        TeaResponse teacher = judgeTeacher(request, response);
        AdminResponse admin = judgeAdmin(request, response);
        //如果三者都为空,则用户未登录
        if (TextUtils.isNull(student) && TextUtils.isNull(teacher) && TextUtils.isNull(admin)) {
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        if (!TextUtils.isNull(student)) {
            //拿到tokenKey
            String tokenKey = CookieUtils.getCookie(request, Constants.Student.STUDENT_COOKIE_TOKEN_KEY);
            //删除redis里对应的token
            redisUtils.del(Constants.Student.STUDENT_TOKEN_KEY+tokenKey);
            //删除MySQL里对应的refreshToken
            refreshTokenMapper.deleteByUserId(student.getStuId());
            //删除cookie里的token_key
            CookieUtils.deleteCookie(response, Constants.Student.STUDENT_COOKIE_TOKEN_KEY);
            return ResponseResult.SUCCESS("退出登录成功");
        } else if (!TextUtils.isNull(teacher)) {
            //拿到tokenKey
            String tokenKey = CookieUtils.getCookie(request, Constants.Teacher.TEACHER_COOKIE_TOKEN_KEY);
            //删除redis里对应的token
            redisUtils.del(Constants.Teacher.TEACHER_TOKEN_KEY+tokenKey);
            //删除MySQL里对应的refreshToken
            refreshTokenMapper.deleteByUserId(teacher.getTeaId());
            //删除cookie里的token_key
            CookieUtils.deleteCookie(response, Constants.Teacher.TEACHER_COOKIE_TOKEN_KEY);
            return ResponseResult.SUCCESS("退出登录成功");
        } else {
            //拿到tokenKey
            String tokenKey = CookieUtils.getCookie(request, Constants.Admin.ADMIN_COOKIE_TOKEN_KEY);
            //删除redis里对应的token
            redisUtils.del(Constants.Admin.ADMIN_TOKEN_KEY+tokenKey);
            //删除MySQL里对应的refreshToken
            refreshTokenMapper.deleteByUserId(admin.getAdminId());
            //删除cookie里的token_key
            CookieUtils.deleteCookie(response, Constants.Admin.ADMIN_COOKIE_TOKEN_KEY);
            return ResponseResult.SUCCESS("退出登录成功");
        }
    }


    ////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////
    //--------------------------------------------学生登录

    @Override
    public ResponseResult studentLogin(Student student, HttpServletResponse response) {
        //检查数据
        if (TextUtils.isEmpty(student.getStuSno())) {
            return ResponseResult.FAILED("登录账号不能为空");
        }
        if (TextUtils.isEmpty(student.getStuPwd())) {
            return ResponseResult.FAILED("登录密码不能为空");
        }

        //查询数据库账号和密码是否对的
        //那账号去查有没有这个用户，有的话将用户返回
        StuResponse stuFromDb = studentMapper.selectBySno(student.getStuSno());
        if (TextUtils.isNull(stuFromDb)) {
            return ResponseResult.FAILED("用户不存在或密码错误");
        }
        String cyptPwd = studentMapper.selectPwdBySno(stuFromDb.getStuSno());
        boolean matchePwd = bCryptPasswordEncoder.matches(student.getStuPwd(), cyptPwd);
        if (!matchePwd) {
            return ResponseResult.FAILED("用户不存在或密码错误");
        }

        //账号密码正确
        //生成token
        createStuToken(response, stuFromDb);

        return ResponseResult.SUCCESS("登录成功").setData(stuFromDb).setRole(roleMapper.selectRoleNameByUserId(stuFromDb.getStuId()));
    }

    private String createStuToken(HttpServletResponse response, StuResponse stuFromDb) {
        //生成token
        Map<String,Object> cliams = ClaimsUtils.student2Claims(stuFromDb);
        //token默认有效为2个小时
        String token = JwtUtil.createToken(cliams);
        //返回token的md5值，token会保存在redis里
        //前端访问的时候，携带token的md5key，从redis中获取即可
        String tokenKey = DigestUtils.md5DigestAsHex(token.getBytes());
        //保存token到redis里，有效期为2个小时，key是tokenKey(有效期为1年)
        redisUtils.set(Constants.Student.STUDENT_TOKEN_KEY+tokenKey, token,Constants.TimeValueInSecond.HOUR_2);
        //把tokenKey写到cookies里
        CookieUtils.setUpCookie(response, Constants.Student.STUDENT_COOKIE_TOKEN_KEY, tokenKey);

        //生成refreshToken
        String refreshTokenValue = JwtUtil.createRefreshToken(stuFromDb.getStuId(), Constants.TimeValueInSecond.MONTH);
        //保存到数据库
        //先到数据库查看有没有该用户的refreshToken，如果该用户之前登录过，是指redis的token过期了，数据库中的还是会有的
        List<RefreshToken> tokenList = refreshTokenMapper.findOneById(stuFromDb.getTeaId());
        //如果数据库有的话，将原有的删掉
        if (tokenList.size() != 0) {
            refreshTokenMapper.deleteByUserId(stuFromDb.getTeaId());
        }
        //数据库没有建一个新的存进去
        //refreshToken, tokenKey, 用户ID, 创建时间, 更新时间
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(idWorker.nextId() + "");
        refreshToken.setRefreshToken(refreshTokenValue);
        refreshToken.setUserId(stuFromDb.getStuId());
        refreshToken.setTokenKey(tokenKey);
        refreshToken.setCreateTime(new Date());
        refreshToken.setUpdateTime(new Date());
        refreshTokenMapper.save(refreshToken);
        return tokenKey;
    }

    /**
     * 本质：通过携带的token_key检查用户是否有登录，如果登录了，就返回用户信息
     * @param request
     * @param response
     * @return
     */
    @Override
    public StuResponse checkStuLogin(HttpServletRequest request, HttpServletResponse response) {
        //拿到token_key
        String tokenKey = CookieUtils.getCookie(request, Constants.Student.STUDENT_COOKIE_TOKEN_KEY);
//        log.info("checkStuLogin tokenKey ==> " + tokenKey);
        StuResponse stuResponse = parseStuByTokenKey(tokenKey);
        if (stuResponse == null) {
            //说明解析出错了，过期了
            //1、去MySQL数据库查询refreshToken
            RefreshToken refreshToken = refreshTokenMapper.findOneByTokenKey(tokenKey);
            //2、如果不存在，就是当前访问没有登陆
            if (refreshToken == null) {
//                log.info("---------refreshToken is null----------");
                return null;
            }
            //3、如果存在，就解析refreshToken
            try {
                JwtUtil.parseJWT(refreshToken.getRefreshToken());
                //4、如果refreshToken有效，创建新的token，和新的refreshToken
                //拿到用户id，去数据库查询，再在redis里面生成新的token
                String userId = refreshToken.getUserId();
                StuResponse stuFromDb = studentMapper.findOneById(userId);
                //在redis里面创建新的token,因为到这里的时候，redis里面久的token已经过期自动删除了，所以这里不必再手动删除了
                int deleteResult = refreshTokenMapper.deleteById(refreshToken.getId());
//                log.info("删除了"+deleteResult+"条refreshToken数据");
                //生成新的token和refreshToken保存到数据库
                String newTokenKey = createStuToken(response, stuFromDb);
                return parseStuByTokenKey(newTokenKey);
            } catch (Exception e1) {
                //5、如果refreshToken过期了，就当前访问没有登录，提示用户登录
                log.info("---------refreshToken 过期----------");
                return null;
            }
        }
        return stuResponse;
    }


    //学生端
    //如果学生没登陆返回null
    //登陆了就返回该学生ID
    @Override
    public StuResponse judgeStudent(HttpServletRequest request, HttpServletResponse response) {
        String tokenKey = CookieUtils.getCookie(request, Constants.Student.STUDENT_COOKIE_TOKEN_KEY);
        if (tokenKey == null) {
            return null;
        }
        //获取当前用户信息
        StuResponse student = checkStuLogin(request, response);
        if (student == null) {
            return null;
        }
        //获取当前老师ID返回
        return student;
    }

    private StuResponse parseStuByTokenKey(String tokenKey) {
        //记得加前缀，通过前面保存的(tokenKey,token)拿到token
        String token = (String) redisUtils.get(Constants.Student.STUDENT_TOKEN_KEY + tokenKey);
//        log.info("parseByTokenKey token ==> " + token);
        if (token != null) {
//            log.info("----------token is not null-----------");
            try {
                //说明有token，解析token
                Claims claims = JwtUtil.parseJWT(token);
//                log.info((String)claims.get("sno"));
//                log.info("----------解析token-----------");
                StuResponse stuResponse = ClaimsUtils.claims2Student(claims);
                return stuResponse;
            } catch (Exception e) {
                //过期了
                log.info("parseByTokenKey ==> " + tokenKey + " ========== 过期了");
                return null;
            }
        }
        return null;
    }

    //--------------------------------------------老师登录

    @Override
    public ResponseResult teacherLogin(Teacher teacher, HttpServletResponse response) {
        //检查数据
        if (TextUtils.isEmpty(teacher.getTeaTno())) {
            return ResponseResult.FAILED("登录账号不能为空");
        }
        if (TextUtils.isEmpty(teacher.getTeaPwd())) {
            return ResponseResult.FAILED("登录密码不能为空");
        }

        //查询数据库账号和密码是否对的
        TeaResponse teaFromDb = teacherMapper.selectByTno(teacher.getTeaTno());
        if (TextUtils.isNull(teaFromDb)) {
            return ResponseResult.FAILED("用户不存在或密码错误");
        }
        String cyptPwd = teacherMapper.selectPwdByTno(teaFromDb.getTeaTno());
        boolean matchePwd = bCryptPasswordEncoder.matches(teacher.getTeaPwd(), cyptPwd);
        if (!matchePwd) {
            return ResponseResult.FAILED("用户不存在或密码错误");
        }

        //账号密码正确
        //生成token
        String tokenKey = createTeaToken(response, teaFromDb);

        return ResponseResult.SUCCESS("登陆成功").setData(teaFromDb).setRole(roleMapper.selectRoleNameByUserId(teaFromDb.getTeaId()));
    }

    private String createTeaToken(HttpServletResponse response, TeaResponse teaFromDb) {
        //生成token
        Map<String,Object> cliams = ClaimsUtils.teacher2Claims(teaFromDb);
        //token默认有效为2个小时
        String token = JwtUtil.createToken(cliams);
        //返回token的md5值，token会保存在redis里
        //前端访问的时候，携带token的md5key，从redis中获取即可
        String tokenKey = DigestUtils.md5DigestAsHex(token.getBytes());
        //保存token到redis里，有效期为2个小时，key是tokenKey(有效期为1年)
        redisUtils.set(Constants.Teacher.TEACHER_TOKEN_KEY+tokenKey, token,Constants.TimeValueInSecond.HOUR_2);
        //把tokenKey写到cookies里
        CookieUtils.setUpCookie(response, Constants.Teacher.TEACHER_COOKIE_TOKEN_KEY, tokenKey);

        //生成refreshToken
        String refreshTokenValue = JwtUtil.createRefreshToken(teaFromDb.getTeaId(), Constants.TimeValueInSecond.MONTH);
        //保存到数据库
        //先到数据库查看有没有该用户的refreshToken，如果该用户之前登录过，是指redis的token过期了，数据库中的还是会有的
        List<RefreshToken> tokenList = refreshTokenMapper.findOneById(teaFromDb.getTeaId());
        //如果数据库有的话，将原有的删掉
        if (tokenList.size() != 0) {
            refreshTokenMapper.deleteByUserId(teaFromDb.getTeaId());
        }
        //数据库没有建一个新的存进去
        //refreshToken, tokenKey, 用户ID, 创建时间, 更新时间
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(idWorker.nextId() + "");
        refreshToken.setRefreshToken(refreshTokenValue);
        refreshToken.setUserId(teaFromDb.getTeaId());
        refreshToken.setTokenKey(tokenKey);
        refreshToken.setCreateTime(new Date());
        refreshToken.setUpdateTime(new Date());
        refreshTokenMapper.save(refreshToken);
        return tokenKey;
    }

    @Override
    public TeaResponse checkTeaLogin(HttpServletRequest request, HttpServletResponse response) {
        //拿到token_key
        String tokenKey = CookieUtils.getCookie(request, Constants.Teacher.TEACHER_COOKIE_TOKEN_KEY);
//        log.info("checkTeaLogin tokenKey ==> " + tokenKey);
        TeaResponse teaResponse = parseTeaByTokenKey(tokenKey);
        if (teaResponse == null) {
            //说明解析出错了，过期了
            //1、去MySQL数据库查询refreshToken
            RefreshToken refreshToken = refreshTokenMapper.findOneByTokenKey(tokenKey);
            //2、如果不存在，就是当前访问没有登陆
            if (refreshToken == null) {
//                log.info("---------refreshToken is null----------");
                return null;
            }
            //3、如果存在，就解析refreshToken
            try {
                JwtUtil.parseJWT(refreshToken.getRefreshToken());
                //4、如果refreshToken有效，创建新的token，和新的refreshToken
                //拿到用户id，去数据库查询，再在redis里面生成新的token
                String userId = refreshToken.getUserId();
                TeaResponse teaFromDb = teacherMapper.findOneById(userId);
                //在redis里面创建新的token,因为到这里的时候，redis里面久的token已经过期自动删除了，所以这里不必再手动删除了
                int deleteResult = refreshTokenMapper.deleteById(refreshToken.getId());
//                log.info("删除了"+deleteResult+"条refreshToken数据");
                //生成新的token和refreshToken保存到数据库
                String newTokenKey = createTeaToken(response, teaFromDb);
                return parseTeaByTokenKey(newTokenKey);
            } catch (Exception e1) {
                //5、如果refreshToken过期了，就当前访问没有登录，提示用户登录
                log.info("---------refreshToken 过期----------");
                return null;
            }
        }
        return teaResponse;
    }

    //老师端
    //如果老师没登陆返回null
    //登陆了就返回该老师信息
    public TeaResponse judgeTeacher(HttpServletRequest request, HttpServletResponse response) {
        String tokenKey = CookieUtils.getCookie(request, Constants.Teacher.TEACHER_COOKIE_TOKEN_KEY);
        //log.info("judgeTeacher tokenKey ==> " + tokenKey);
        if (tokenKey == null) {
//            log.info("tokenKey ==> is null");
            //账号未登录
            return null;
        }
        //获取当前用户信息
        TeaResponse teacher = checkTeaLogin(request, response);
        if (TextUtils.isNull(teacher)) {
//            log.info("teacher ==> is null");
            //账号未登录
            return null;
        }
        //获取当前老师返回
        return teacher;
    }

    private TeaResponse parseTeaByTokenKey(String tokenKey) {
        //记得加前缀，通过前面保存的(tokenKey,token)拿到token
        String token = (String) redisUtils.get(Constants.Teacher.TEACHER_TOKEN_KEY + tokenKey);
//        log.info("parseByTokenKey token ==> " + token);
        if (token != null) {
//            log.info("----------token is not null-----------");
            try {
                //说明有token，解析token
                Claims claims = JwtUtil.parseJWT(token);
//                log.info((String)claims.get("sno"));
//                log.info("----------解析token-----------");
                TeaResponse teaResponse = ClaimsUtils.claims2Teacher(claims);
                return teaResponse;
            } catch (Exception e) {
                //过期了
                log.info("parseByTokenKey ==> " + tokenKey + " ========== 过期了");
                return null;
            }
        }
        return null;
    }

    //--------------------------------------------管理员登录

    @Override
    public ResponseResult adminLogin(Administrator admin, HttpServletResponse response) {
        //检查数据
        if (TextUtils.isEmpty(admin.getAdminAno())) {
            return ResponseResult.FAILED("登录账号不能为空");
        }
        if (TextUtils.isEmpty(admin.getAdminPwd())) {
            return ResponseResult.FAILED("登录密码不能为空");
        }

        //查询数据库账号和密码是否对的
        AdminResponse adminFromDb = adminMapper.selectByAno(admin.getAdminAno());
        if (TextUtils.isNull(adminFromDb)) {
            return ResponseResult.FAILED("用户不存在或密码错误");
        }
        String cyptPwd = adminMapper.selectPwdByAno(admin.getAdminAno());
        boolean matchePwd = bCryptPasswordEncoder.matches(admin.getAdminPwd(), cyptPwd);
        if (!matchePwd) {
            return ResponseResult.FAILED("用户不存在或密码错误");
        }

        //账号密码正确
        //生成token
        createAdminToken(response, adminFromDb);

        return ResponseResult.SUCCESS("登录成功").setData(adminFromDb).setRole(roleMapper.selectRoleNameByUserId(adminFromDb.getAdminId()));
    }

    private String createAdminToken(HttpServletResponse response, AdminResponse adminFromDb) {
        //生成token
        Map<String,Object> cliams = ClaimsUtils.admin2Claims(adminFromDb);
        //token默认有效为2个小时
        String token = JwtUtil.createToken(cliams);
        //返回token的md5值，token会保存在redis里
        //前端访问的时候，携带token的md5key，从redis中获取即可
        String tokenKey = DigestUtils.md5DigestAsHex(token.getBytes());
        //保存token到redis里，有效期为2个小时，key是tokenKey(有效期为1年)
        redisUtils.set(Constants.Admin.ADMIN_TOKEN_KEY+tokenKey, token,Constants.TimeValueInSecond.HOUR_2);
        //把tokenKey写到cookies里
        CookieUtils.setUpCookie(response, Constants.Admin.ADMIN_COOKIE_TOKEN_KEY, tokenKey);

        //生成refreshToken
        String refreshTokenValue = JwtUtil.createRefreshToken(adminFromDb.getAdminId(), Constants.TimeValueInSecond.MONTH);
        //保存到数据库
        //先到数据库查看有没有该用户的refreshToken，如果该用户之前登录过，是指redis的token过期了，数据库中的还是会有的
        List<RefreshToken> tokenList = refreshTokenMapper.findOneById(adminFromDb.getAdminId());
        //如果数据库有的话，将原有的删掉
        if (tokenList.size() != 0) {
            refreshTokenMapper.deleteByUserId(adminFromDb.getAdminId());
        }
        //数据库没有建一个新的存进去
        //refreshToken, tokenKey, 用户ID, 创建时间, 更新时间
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(idWorker.nextId() + "");
        refreshToken.setRefreshToken(refreshTokenValue);
        refreshToken.setUserId(adminFromDb.getAdminId());
        refreshToken.setTokenKey(tokenKey);
        refreshToken.setCreateTime(new Date());
        refreshToken.setUpdateTime(new Date());
        refreshTokenMapper.save(refreshToken);
        return tokenKey;
    }

    @Override
    public AdminResponse checkadminLogin(HttpServletRequest request, HttpServletResponse response) {
        //拿到token_key
        String tokenKey = CookieUtils.getCookie(request, Constants.Admin.ADMIN_COOKIE_TOKEN_KEY);
//        log.info("checkAdminLogin tokenKey ==> " + tokenKey);
        AdminResponse adminResponse = parseAdminByTokenKey(tokenKey);
        if (adminResponse == null) {
            //说明解析出错了，过期了
            //1、去MySQL数据库查询refreshToken
            RefreshToken refreshToken = refreshTokenMapper.findOneByTokenKey(tokenKey);
            //2、如果不存在，就是当前访问没有登陆
            if (refreshToken == null) {
//                log.info("---------refreshToken is null----------");
                return null;
            }
            //3、如果存在，就解析refreshToken
            try {
                JwtUtil.parseJWT(refreshToken.getRefreshToken());
                //4、如果refreshToken有效，创建新的token，和新的refreshToken
                //拿到用户id，去数据库查询，再在redis里面生成新的token
                String userId = refreshToken.getUserId();
                AdminResponse adminFromDb = adminMapper.findOneById(userId);
                //在redis里面创建新的token,因为到这里的时候，redis里面久的token已经过期自动删除了，所以这里不必再手动删除了
                int deleteResult = refreshTokenMapper.deleteById(refreshToken.getId());
//                log.info("删除了"+deleteResult+"条refreshToken数据");
                //生成新的token和refreshToken保存到数据库
                String newTokenKey = createAdminToken(response, adminFromDb);
                return parseAdminByTokenKey(newTokenKey);
            } catch (Exception e1) {
                //5、如果refreshToken过期了，就当前访问没有登录，提示用户登录
                log.info("---------refreshToken 过期----------");
                return null;
            }
        }
        return adminResponse;
    }

    @Override
    public AdminResponse judgeAdmin(HttpServletRequest request, HttpServletResponse response) {
        String tokenKey = CookieUtils.getCookie(request, Constants.Admin.ADMIN_COOKIE_TOKEN_KEY);
//        log.info("judgeAdmin tokenKey ==> " + tokenKey);
        if (tokenKey == null) {
//            log.info("tokenKey ==> is null");
            return null;
        }
        //获取当前用户信息
        AdminResponse admin = checkadminLogin(request, response);
        if (TextUtils.isNull(admin)) {
//            log.info("teacher ==> is null");
            return null;
        }
        //获取当前老师返回
        return admin;
    }

    private AdminResponse parseAdminByTokenKey(String tokenKey) {
        //记得加前缀，通过前面保存的(tokenKey,token)拿到token
        String token = (String) redisUtils.get(Constants.Admin.ADMIN_TOKEN_KEY + tokenKey);
//        log.info("parseByTokenKey token ==> " + token);
        if (token != null) {
//            log.info("----------token is not null-----------");
            try {
                //说明有token，解析token
                Claims claims = JwtUtil.parseJWT(token);
//                log.info("----------解析token-----------");
                AdminResponse adminResponse = ClaimsUtils.claims2Admin(claims);
                return adminResponse;
            } catch (Exception e) {
                //过期了
                log.info("parseByTokenKey ==> " + tokenKey + " ========== 过期了");
                return null;
            }
        }
        //redis里的到两个小时了，自动删除了
        return null;
    }


}
