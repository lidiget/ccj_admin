package com.example.democommon.unitedreturn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统一json返回
 */
public class HjControllerHelper {
    /**
     * RestControllerHelper的toJson常量
     */
    private static final String RESULT_DATA = "data";//结果
    private static final String RESULT_RESULT = "result";//失败返回
    private static final String RESULT_MSG = "msg";//原因
    private static final String RESULT_STATUS = "status";//状态
    private static final String RESULT_TOTAL = "total";//总条数
    /**
     * 200: 成功。
     * 401: 当前请求需要用户验证。
     * 403：权限错误。
     * 404: 请求的资源未找到。
     * 408：请求超时。
     */
    public static final int SUCCESS = 200;
    public static final int UNLOGIN = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int TIMEOUT = 408;

    /**
     *  data: 结果
     *  sues: 成功
     *  eors: 失败
     *  msg: 原因
     *  status:状态码
     */
    private Object data;
    private boolean result;
    private String msg;
    private int status;
    private int total;

    /**
     *
     */
    public HjControllerHelper() {
        this.status = SUCCESS;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * toJsonMap
     * @return 返回数据
     */
    public  Map<?,?> result(Object data,boolean result,String msg,int status){
        Map<Object,Object> map = new HashMap<>();
        map.put(RESULT_DATA,data);
        map.put(RESULT_RESULT,result);
        map.put(RESULT_MSG,msg);
        map.put(RESULT_STATUS,status);
        return map;
    }
    // 用于捕捉全局异常返回数据用
    public  Map<?,?> results(Throwable abc,boolean result,String msg,int status){
        Map<Object,Object> map = new HashMap<>();
        map.put(RESULT_DATA,abc);
        map.put(RESULT_RESULT,result);
        map.put(RESULT_MSG,msg);
        map.put(RESULT_STATUS,status);
        return map;
    }
    // 用于捕捉全局异常返回数据用
    public  Map<?,?> result(List abc,boolean result,String msg,int status,int total){
        Map<Object,Object> map = new HashMap<>();
        map.put(RESULT_TOTAL,total);
        map.put(RESULT_DATA,abc);
        map.put(RESULT_RESULT,result);
        map.put(RESULT_MSG,msg);
        map.put(RESULT_STATUS,status);
        return map;
    }
}
