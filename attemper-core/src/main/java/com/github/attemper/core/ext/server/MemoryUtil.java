package com.github.attemper.core.ext.server;

import com.github.attemper.common.result.MapResult;
import com.github.attemper.common.util.FormatUtils;
import com.github.attemper.common.util.NumberUtil;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;

import java.util.ArrayList;
import java.util.List;

/**
 * util for memory info
 */
public class MemoryUtil {

    public static List<MapResult<String, Object>> getMemoryStatus(SystemInfo systemInfo) {
        GlobalMemory memory = systemInfo.getHardware().getMemory();
        String usedRate = NumberUtil.toPercentage(memory.getTotal(), memory.getTotal() - memory.getAvailable());
        String availableRate = NumberUtil.toPercentage(memory.getTotal(), memory.getAvailable());
        List<MapResult<String, Object>> list = new ArrayList<>(4);
        list.add(new MapResult<>(TOTAL_SIZE, FormatUtils.formatBytes(memory.getTotal())));
        list.add(new MapResult<>(USED, FormatUtils.formatBytes(memory.getTotal() - memory.getAvailable()) + "(" + usedRate + ")"));
        list.add(new MapResult<>(AVAILABLE, FormatUtils.formatBytes(memory.getAvailable()) + "(" + availableRate + ")"));
        return list;
    }

    private static final String TOTAL_SIZE = "totalSize";

    private static final String USED = "used";

    private static final String AVAILABLE = "available";

}
