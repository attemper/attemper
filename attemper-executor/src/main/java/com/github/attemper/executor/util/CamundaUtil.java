package com.github.attemper.executor.util;

public class CamundaUtil {

    public static String extractKeyFromProcessDefinitionId(String processDefinitionId) {
        return processDefinitionId == null ? null : processDefinitionId.split(":")[0];
    }
}
