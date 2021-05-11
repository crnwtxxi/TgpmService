package com.example.tgpmsystem.service;

import com.example.tgpmsystem.response.ResponseResult;
import org.springframework.stereotype.Service;

@Service
public interface ThesisService {

    ResponseResult getAllPassThesis(Integer pageNum, Integer pageSize);
    ResponseResult getAllUnpassThesis(Integer pageNum, Integer pageSize);
}
