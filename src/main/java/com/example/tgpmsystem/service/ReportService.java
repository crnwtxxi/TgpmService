package com.example.tgpmsystem.service;

import com.example.tgpmsystem.pojo.Activity;
import com.example.tgpmsystem.pojo.Project;
import com.example.tgpmsystem.pojo.Thesis;
import com.example.tgpmsystem.response.ResponseResult;
import com.example.tgpmsystem.vo.UploadProjVo;
import org.springframework.stereotype.Service;

@Service
public interface ReportService {

    ResponseResult reportActivity(Activity activity);
    ResponseResult reportProject(UploadProjVo projectVo);
    ResponseResult reportThesis(Thesis thesis);
}
