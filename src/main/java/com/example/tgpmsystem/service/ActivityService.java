package com.example.tgpmsystem.service;

import com.example.tgpmsystem.response.ResponseResult;
import org.springframework.stereotype.Service;

@Service
public interface ActivityService {

    ResponseResult getAllPassActi(Integer pageNum, Integer pageSize);
    ResponseResult getAllUnpassActi(Integer pageNum, Integer pageSize);
}
