package com.example.democommon.exceptionive;

import com.example.democommon.unitedreturn.HjControllerHelper;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author admin
 */
//@ControllerAdvice
public class GlobalExceptionHandler {
//    HjControllerHelper hjControllerHelper = new HjControllerHelper();
//    /**
//     * 处理自定义的业务异常
//     * @param req
//     * @param e
//     * @return
//     */
//    @ExceptionHandler(value =BizException.class)
//    @ResponseBody
//    public Map<?,?> bizExceptionHandler(HttpServletRequest req, BizException e){
//        System.out.println("发生业务异常！原因是：{}"+e.getErrorMsg());
//        return hjControllerHelper.results(e.getCause(),false,"发生业务异常!原因是:{}"+e.getErrorMsg(),413);
//    }
//    /**
//     * 处理空指针的异常
//     * @param req
//     * @param e
//     * @return
//     */
//    @ExceptionHandler(value =NullPointerException.class)
//    @ResponseBody
//    public Map exceptionHandler(HttpServletRequest req, NullPointerException e){
//        System.out.println("发生空指针异常！原因是:"+e);
//        List list = new ArrayList<>();
//        return hjControllerHelper.result(list,false,"发生异常!原因是某个值为:"+e.getCause(),400);
//    }
//    /**
//     * 处理SQL异常
//     * @param e
//     * @return
//     */
//    /*@ExceptionHandler(value = TransientDataAccessResourceException.class)
//    @ResponseBody
//    public Map exceptionHandler(TransientDataAccessResourceException e){
//        System.out.println("执行sql语句是实体类和映射文件字段类型不一致！原因是:"+e);
//        List list = new ArrayList<>();
//        return hjControllerHelper.result(list,false,"发生异常!原因是执行sql时,某个值为空",400);
//    }*/
//    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
//    @ResponseBody
//    public Map exceptionHandler(HttpRequestMethodNotSupportedException e){
//        System.out.println("执行sql语句是实体类和映射文件字段类型不一致！原因是:"+e);
//        List list = new ArrayList<>();
//        return hjControllerHelper.result(list,false,"请求的http方法不对!",403);
//    }
//    /**
//     * 处理其他异常
//     * @param req
//     * @param e
//     * @return
//     */
//    /*@ExceptionHandler(value =Exception.class)
//    @ResponseBody
//    public Map exceptionHandler(HttpServletRequest req,Exception e){
//        System.out.println("未知异常！原因是:"+e);
//        List list = new ArrayList<>();
//        return hjControllerHelper.result(list,false,"",500);
//    }*/

}
