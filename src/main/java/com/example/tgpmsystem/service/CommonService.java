package com.example.tgpmsystem.service;

import com.example.tgpmsystem.response.ResponseResult;
import com.example.tgpmsystem.vo.UpdatePwdVo;
import org.springframework.stereotype.Service;

@Service
public interface CommonService {

    ResponseResult getAllAnnounce(Integer pageNum, Integer pageSize);

    ResponseResult updatePassword(UpdatePwdVo pwdVo);
    ResponseResult updateEmail(String newEmail);
}
