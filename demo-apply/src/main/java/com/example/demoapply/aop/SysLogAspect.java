package com.example.demoapply.aop;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.example.demoapply.redis.RedisUtil;
import com.example.democommon.annotation.MyLog;
import com.example.democommon.annotation.PreventSumbit;
import com.example.democommon.unit.TokenUtils;
import com.example.democommon.unitedreturn.HjControllerHelper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 系统日志：切面处理类
 */
@Aspect
@Component
public class SysLogAspect {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private HttpServletRequest request;

    HjControllerHelper hjControllerHelper = new HjControllerHelper();

    private String KEY ="VKJBZ-UOG6P-N7DDA-L4XYG-EAZVK-M7F5Q";
    // 在注解的位置进入接口
    @Pointcut("@annotation(com.example.democommon.annotation.MyLog)")
    public void logPoinCut(){

    }
    // 切面配置通知
    @AfterReturning("logPoinCut()")
    public void saveSysLog(JoinPoint joinPoint){
        // 从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取切入点所在的方法
        Method method = signature.getMethod();
        // 获取操作
        MyLog myLog = method.getAnnotation(MyLog.class);
        if (myLog != null) {
            String value = myLog.value();
        }
        String ip = request.getRemoteAddr();
        String user_phone = request.getHeader("user_phone");
        // 获取请求的类名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = method.getName();
        /*System.out.println(user_phone+","+className+"."+methodName+","+ip+","
                +AddressUtils.getLocation(ip));*/
        /*Map<Object,Object> map = new HashMap<>();
        map.put("user_phone",user_phone);
        map.put("app_oper_method",className+"."+ methodName);
        map.put("app_oper_ip",ip);
        map.put("app_oper_address",AddressUtils.getLocation(ip));*/
        //hjService.insertoper(map);
    }
    /*@Around("execution(* com.example..*Controller.*(..))&& @annotation(nrs)")
    public Object arround(ProceedingJoinPoint pjp,PreventSumbit nrs){
        //定义一个缓存
        ValueOperations<String, Integer> opsForValue = redisTemplate.opsForValue();
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            // 获取用户访问内容
            String sessionId = RequestContextHolder.getRequestAttributes().getSessionId();
            HttpServletRequest request = attributes.getRequest();
            String key = sessionId + "-" + request.getServletPath();
            // 如果缓存中有这个url视为重复
            if(opsForValue.get(key)==null){
                Object abc =pjp.proceed();
                opsForValue.set(key,0,2,TimeUnit.SECONDS);
                return abc;
            } else{
                Map<String,String> map = new HashMap<>();
                map.put("error","请勿重复提交");
                return map;
            }
        }catch (Throwable e){
            Map<String,String> map = new HashMap<>();
            map.put("error","请勿重复提交");
            return map;
        }
    }*/
    @Around("execution(* com.example..*Controller.*(..))")
    public Object Token(ProceedingJoinPoint pjp) throws Throwable{
        List<Map<?,?>> list = new ArrayList<>();
        // 获取链接
        String abc = request.getRequestURI();
        if(abc.matches(".*user.*")||abc.matches(".*class.*")||abc.equals("/api/shop/SelectSlideshow")||
                abc.equals("/api/shop/SelectShop")||abc.equals("/api/shop/SelectDetails")||abc.equals
                ("/api/shop/ShopSpecification")||abc.equals("/api/shop/ShopHot")||abc.equals("/api/shop/ShopEvaluate")||
                abc.equals("/api/shop/SelectWarehouse")||abc.equals("/api/shop/SelectManufacturers")||abc.equals
                ("/api/shop/SelectManufacturersBrand")||abc.equals("/api/shop/SelectManufacturersInfo")||abc.equals
                ("/api/shop/SelectManufacturersShop")||abc.equals("/api/shop/WorkerList")||abc.equals
                ("/api/shop/WorkerListInfo")||abc.equals("/api/shop/WorkersInfo")||abc.equals("/api/shop/WorkerEvaluate")
                ||abc.equals("InsertWorkerInfo")||abc.equals("/api/worker/WorkerLogin")||abc.equals
                ("/api/worker/WorkerPwd")||abc.equals("/api/worker/SelectTask")||abc.equals("/api/worker/SelectTakSign")
                ||abc.equals("/api/worker/InsertTakSign")){
                 // 通知方法执行
                 Object pass = pjp.proceed();
                 return pass;
             }else{
                 // 获取头部信息
                 String authorization = request.getHeader("Authorization");
                 System.out.println(authorization);
                 String userphone = TokenUtils.decoded(authorization);
                 System.out.println(redisUtil.get(userphone+"token"));
                 Object pass = pjp.proceed();
                 return pass;
//                 if (authorization.equals("")) {
//                     return hjControllerHelper.result(list, false, "账号登录已失效~请登录重试！", 401);
//                 }
//                 if(authorization.equals(redisUtil.get(userphone+"token"))){
//                     Object pass = pjp.proceed();
//                     return pass;
//                 }else{
//                     return hjControllerHelper.result(list,false,"账号或其他端登录~请登录重试！",401);
//                 }
             }
    }
}
