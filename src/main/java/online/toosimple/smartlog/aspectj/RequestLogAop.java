package online.toosimple.smartlog.aspectj;

import lombok.extern.slf4j.Slf4j;
import online.toosimple.smartlog.constant.LogConstant;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.MDC;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.*;

import com.alibaba.fastjson.JSON;

@Aspect
@Component
@Slf4j
public class RequestLogAop {

    //controller下所有方法
    @Pointcut("execution(public * online.toosimple.smartlog.controller.*.*(..))")
    public void doOperation() {
    }

    @Around("doOperation()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable  {
        long beforeTime = System.currentTimeMillis();

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();

        String method = request.getMethod();
        String url = request.getRequestURI();
        String params = null;
        String body = null;
        String traceId = UUID.randomUUID().toString().replace("-", "").toUpperCase();

        if (HttpMethod.GET.name().equals(method)) {
            //get请求的查询参数
            params = request.getQueryString();
        }
        else if (HttpMethod.POST.name().equals(method)) {
            //如果是post请求，url后不要带queryString，不然会被request.getParameterMap获取到
            Object[] args = joinPoint.getArgs();
            if (args.length > 0) {
                Map<String,String> parmMap=new HashMap();
                Map<String,String[]> map= request.getParameterMap();
                if (map.size() > 0) {
                    //form-data的查询参数
                    Set<String> key = map.keySet();
                    Iterator<String> iterator = key.iterator();
                    while (iterator.hasNext()) {
                        String k = iterator.next();
                        parmMap.put(k, map.get(k)[0]);
                    }
                    body = parmMap.toString();
                } else {
                    //java对象
                    for (Object arg : args) {
                        Map objMap = getKeyAndValue(arg);
                        body = JSON.toJSONString(objMap);
                    }
                }
            }
        }
        //生成traceId，放到同一个请求的输出日志中，返回结果也会塞入这个traceId，便于查询日志
        MDC.put(LogConstant.TRACE_ID, traceId);
        log.info("request start: method: [{}], url: [{}], params: [{}], body: [{}]", method, url, params, body);
        Object result = joinPoint.proceed();

        long betweenTime = System.currentTimeMillis() - beforeTime;
        log.info("response: [{}], time consuming: [{} ms]", JSON.toJSONString(result), betweenTime);
        return result;
    }


    public static Map<String, Object> getKeyAndValue(Object obj) {
        Map<String, Object> map = new HashMap<>();
        // 得到类对象
        Class userCla = (Class) obj.getClass();
        /* 得到类中的所有属性集合 */
        Field[] fs = userCla.getDeclaredFields();
        for (int i = 0; i < fs.length; i++) {
            Field f = fs[i];
            f.setAccessible(true); // 设置些属性是可以访问的
            Object val = new Object();
            try {
                val = f.get(obj);
                // 得到此属性的值
                map.put(f.getName(), val);// 设置键值
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        return map;
    }
}
