package com.example.tgpmsystem.service;

import com.example.tgpmsystem.response.ResponseResult;
import org.springframework.stereotype.Service;

@Service
public interface ProjectService {

    ResponseResult getAllPassproj(Integer pageNum, Integer pageSize);
    ResponseResult getAllUnpassproj(Integer pageNum, Integer pageSize);

}
