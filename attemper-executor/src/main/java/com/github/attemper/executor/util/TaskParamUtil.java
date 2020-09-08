package com.github.attemper.executor.util;

import com.github.attemper.common.util.ReflectUtil;
import com.github.attemper.core.ext.model.FileParam;
import com.github.attemper.core.util.FileUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * extract param from execution's variable map
 */
public class TaskParamUtil {

    public static FileParam injectFileParam(Map<String, Object> map) {
        FileParam param = ReflectUtil.reflectObj(FileParam.class, map);
        String localDirectory = StringUtils.isBlank(param.getLocalDirectory()) ? FileUtil.getUserHome() : param.getLocalDirectory();
        String fileNameWithSuffix = param.getFileName() + param.getSuffix();
        param.setLocalDirectory(localDirectory);
        param.setFileNameWithSuffix(fileNameWithSuffix);
        return param;
    }
}
