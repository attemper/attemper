package com.github.attemper.executor.util;

public class CamundaUtil {

    public static String extractKeyFromProcessDefinitionId(String processDefinitionId) {
        return processDefinitionId == null ? null : processDefinitionId.split(":")[0];
    }

    public static String extractIdFromActInstanceId(String actInstanceId) {
        String[] array = actInstanceId.split(":");
        return array[array.length - 1];
    }
}
