package com.github.attemper.core.ext.server;

import com.github.attemper.common.result.MapResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * util for machine info
 */
public class MachineUtil {

    public static List<MapResult<String, Object>> getMachineInfo() {
        Properties prop = System.getProperties();
        List<MapResult<String, Object>> list = new ArrayList<>(4);
        list.add(new MapResult<>(OS_NAME, prop.getProperty("os.name")));
        list.add(new MapResult<>(OS_ARCH, prop.getProperty("os.arch")));
        list.add(new MapResult<>(OS_VERSION, prop.getProperty("os.version")));
        return list;
    }

    private static final String OS_NAME = "osName";

    private static final String OS_ARCH = "osArch";

    private static final String OS_VERSION = "osVersion";

}
