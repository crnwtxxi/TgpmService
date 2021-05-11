package com.example.tgpmsystem.service;

import com.example.tgpmsystem.response.ResponseResult;
import org.springframework.stereotype.Service;

@Service
public interface StudentService {

    ResponseResult getMyTutor();

}
