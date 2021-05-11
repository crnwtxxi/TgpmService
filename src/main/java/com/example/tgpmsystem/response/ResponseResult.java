package com.example.tgpmsystem.response;

public class ResponseResult {

    private boolean success;
    private int code;
    private String message;
    private Object data;
    private String role;

    public ResponseResult(ResponseState responseState) {
        this.success = responseState.isSuccess();
        this.code = responseState.getCode();
        this.message = responseState.getMessage();
    }

    //成功
    public static ResponseResult SUCCESS() {
        return new ResponseResult(ResponseState.SUCCESS);
    }

    //重载SUCCESS
    public static ResponseResult SUCCESS(String message) {
        ResponseResult responseResult = new ResponseResult(ResponseState.SUCCESS);
        responseResult.setMessage(message);
        return responseResult;
    }

    //登陆成功
    public static ResponseResult LOGIN_SUCCESS() {
        return new ResponseResult(ResponseState.LOGIN_SUCCESS);
    }

    //允许登录
    public static ResponseResult PERMIT_LOGIN() {
        return new ResponseResult(ResponseState.PERMIT_LOGIN);
    }

    //失败
    public static ResponseResult FAILED() {
        return new ResponseResult(ResponseState.FAILED);
    }

    //重载FAILED
    public static ResponseResult FAILED(String message) {
        ResponseResult responseResult = new ResponseResult(ResponseState.FAILED);
        responseResult.setMessage(message);
        return responseResult;
    }

    //禁止登录
    public static ResponseResult FORBIDDEN_LOGIN() {
        return new ResponseResult(ResponseState.FORBIDDEN_LOGIN);
    }

    //登录超时
    public static ResponseResult LOGIN_TIMEOUT() {
        return new ResponseResult(ResponseState.LOGIN_TOMEOUT);
    }

    public static ResponseResult ACCOUNT_NOT_LOGIN() {
        return new ResponseResult(ResponseState.ACCOUNT_NOT_LOGIN);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public ResponseResult setData(Object data) {
        this.data = data;
        return this;
        //调用接口的时候直接  return ResponseResult.SUCCESS().setData(返回给前端的数据);
    }

    public String getRole() {
        return role;
    }

    public ResponseResult setRole(String role) {
        this.role = role;
        return this;
    }
}
