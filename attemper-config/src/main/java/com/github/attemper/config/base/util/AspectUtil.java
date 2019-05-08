package com.github.attemper.config.base.util;

import com.github.attemper.common.result.CommonResult;
import com.github.attemper.config.base.bean.SpringContextAware;
import com.github.attemper.config.base.entity.ApiLog;
import com.github.attemper.config.base.service.ApiLogService;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class AspectUtil {

    public static void saveLog(ApiLog apiLog, CommonResult result) {
        if(result != null){
            apiLog.setCode(result.getCode());
            apiLog.setMsg(result.getMsg());
            apiLog.setResponseTime(result.getResponseTime());
            apiLog.setDuration(result.getDuration());
        }
        SpringContextAware.getBean(ApiLogService.class).save(apiLog);
    }

    public static void resolvePath(Method method, ApiLog apiLog) {
        Annotation[] annotations = method.getAnnotations();
        if(annotations != null){
            for (Annotation annotation : annotations) {
                if(annotation instanceof RequestMapping){
                    RequestMapping mappingAnnotation = (RequestMapping) annotation;
                    if(mappingAnnotation != null
                            && mappingAnnotation.value() != null
                            && mappingAnnotation.value().length > 0){
                        apiLog.setPath(mappingAnnotation.value()[0]);
                    }
                } else if (annotation instanceof GetMapping) {
                    GetMapping mappingAnnotation = (GetMapping) annotation;
                    if(mappingAnnotation != null
                            && mappingAnnotation.value() != null
                            && mappingAnnotation.value().length > 0){
                        apiLog.setPath(mappingAnnotation.value()[0]);
                    }
                } else if (annotation instanceof PostMapping) {
                    PostMapping mappingAnnotation = (PostMapping) annotation;
                    if(mappingAnnotation != null
                            && mappingAnnotation.value() != null
                            && mappingAnnotation.value().length > 0){
                        apiLog.setPath(mappingAnnotation.value()[0]);
                    }
                } else if (annotation instanceof PutMapping) {
                    PutMapping mappingAnnotation = (PutMapping) annotation;
                    if(mappingAnnotation != null
                            && mappingAnnotation.value() != null
                            && mappingAnnotation.value().length > 0){
                        apiLog.setPath(mappingAnnotation.value()[0]);
                    }
                } else if (annotation instanceof DeleteMapping) {
                    DeleteMapping mappingAnnotation = (DeleteMapping) annotation;
                    if(mappingAnnotation != null
                            && mappingAnnotation.value() != null
                            && mappingAnnotation.value().length > 0){
                        apiLog.setPath(mappingAnnotation.value()[0]);
                    }
                }
            }
        }
    }
}
