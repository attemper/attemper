package com.github.attemper.core.ext.server;

import com.github.attemper.common.result.MapResult;
import com.github.attemper.common.util.FormatUtils;
import com.github.attemper.common.util.NumberUtil;
import com.github.attemper.java.sdk.common.util.DateUtil;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * util for jvm info
 */
public class JvmUtil {

    public static List<MapResult<String, Object>> getJvmStatus() {
        Properties prop = System.getProperties();
        List<MapResult<String, Object>> list = new ArrayList<>(16);
        list.add(new MapResult<>(VM_NAME, prop.getProperty("java.vm.name")));
        list.add(new MapResult<>(VERSION, prop.getProperty("java.version")));
        list.add(new MapResult<>(HOME_PATH, prop.getProperty("java.home")));
        list.add(new MapResult<>(MAX_MEMORY_SIZE, FormatUtils.formatBytes(Runtime.getRuntime().maxMemory())));
        list.add(new MapResult<>(TOTAL_MEMORY_SIZE, FormatUtils.formatBytes(Runtime.getRuntime().totalMemory())));
        list.add(new MapResult<>(USED_MEMORY, FormatUtils.formatBytes(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) + "(" + NumberUtil.toPercentage(Runtime.getRuntime().totalMemory(), Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) + ")"));
        list.add(new MapResult<>(FREE_MEMORY, FormatUtils.formatBytes(Runtime.getRuntime().freeMemory()) + "(" + NumberUtil.toPercentage(Runtime.getRuntime().totalMemory(), Runtime.getRuntime().freeMemory()) + ")"));
        list.add(new MapResult<>(START_TIME, DateUtil.format(new Date(ManagementFactory.getRuntimeMXBean().getStartTime()), DateUtil.DATE_FORMAT_YYYYMMDDHHMMSS)));
        return list;
    }

    private static final String VM_NAME = "vmName";

    private static final String VERSION = "version";

    private static final String HOME_PATH = "homePath";

    private static final String MAX_MEMORY_SIZE = "maxMemorySize";

    private static final String TOTAL_MEMORY_SIZE = "totalMemorySize";

    private static final String USED_MEMORY = "usedMemory";

    private static final String FREE_MEMORY = "freeMemory";

    private static final String START_TIME = "startTime";
}
