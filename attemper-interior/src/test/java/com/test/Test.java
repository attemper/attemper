package com.test;

import com.github.attemper.interior.param.CleanLogParam;
import com.github.attemper.java.sdk.common.executor.param.execution.TaskParam;
import org.springframework.core.ResolvableType;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

public class Test {
    public static void main(String[] args) {
        Method method = ReflectionUtils.findMethod(Test.class, "testing", TaskParam.class);
        System.out.println(method);
        ResolvableType resolvableType = ResolvableType.forMethodParameter(method, 0);
        Class<?> resolveGenericType = resolvableType.getGeneric(0).resolve();
        System.out.println(resolveGenericType);
    }

    public void testing(TaskParam<CleanLogParam> taskExecutionParam) {
        System.out.println(123456);
    }
}
