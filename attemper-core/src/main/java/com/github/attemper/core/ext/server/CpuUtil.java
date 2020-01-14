package com.github.attemper.core.ext.server;

import com.github.attemper.common.result.MapResult;
import com.github.attemper.common.util.NumberUtil;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * util for cpu info
 */
public class CpuUtil {

    public static List<MapResult<String, Object>> getCpuStatus(SystemInfo systemInfo) {
        CentralProcessor processor = systemInfo.getHardware().getProcessor();
        long[] prev = processor.getSystemCpuLoadTicks();
        Util.sleep(1000L);
        long[] curr = processor.getSystemCpuLoadTicks();
        Map<CentralProcessor.TickType, Long> tickMap = new HashMap<>(CentralProcessor.TickType.values().length);
        computeCpu(tickMap, curr, prev);
        long total = tickMap.values().stream().mapToLong(Long::longValue).sum();
        List<MapResult<String, Object>> list = new ArrayList<>(8);
        list.add(new MapResult<>(CORE_NUM, processor.getLogicalProcessorCount()));
        list.add(new MapResult<>(USED_RATE, NumberUtil.toPercentage(total, tickMap.get(CentralProcessor.TickType.USER))));
        list.add(new MapResult<>(SYSTEM_RATE, NumberUtil.toPercentage(total, tickMap.get(CentralProcessor.TickType.SYSTEM))));
        list.add(new MapResult<>(IO_WAIT_RATE, NumberUtil.toPercentage(total, tickMap.get(CentralProcessor.TickType.IOWAIT))));
        list.add(new MapResult<>(IDLE_RATE, NumberUtil.toPercentage(total, tickMap.get(CentralProcessor.TickType.IDLE))));
        return list;
    }

    private static void computeCpu(Map<CentralProcessor.TickType, Long> tickMap, long[] curr, long[] prev) {
        for (CentralProcessor.TickType tickType : CentralProcessor.TickType.values()) {
            tickMap.put(tickType, curr[tickType.getIndex()] - prev[tickType.getIndex()]);
        }
    }

    private static final String CORE_NUM = "coreNum";

    private static final String USED_RATE = "usedRate";

    private static final String SYSTEM_RATE = "systemRate";

    private static final String IO_WAIT_RATE = "ioWaitRate";

    private static final String IDLE_RATE = "idleRate";
}
