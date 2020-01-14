package com.github.attemper.core.ext.server;

import com.github.attemper.common.result.MapResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * util for file/directory
 */
public class ResourceUtil {

    public static List<MapResult<String, Object>> getResourceInfo() {
        Properties prop = System.getProperties();
        List<MapResult<String, Object>> list = new ArrayList<>(8);
        list.add(new MapResult<>(PID, prop.get("PID")));
        list.add(new MapResult<>(USER_NAME, prop.get("user.name")));
        list.add(new MapResult<>(USER_HOME, prop.get("user.home")));
        list.add(new MapResult<>(USER_DIR, prop.get("user.dir")));
        list.add(new MapResult<>(TEMP_DIR, prop.get("java.io.tmpdir")));
        return list;
    }

    private static final String PID = "pid";

    private static final String USER_NAME = "userName";

    private static final String USER_HOME = "userHome";

    private static final String USER_DIR = "userDir";

    private static final String TEMP_DIR = "tempDir";

}
