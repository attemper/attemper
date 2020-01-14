package com.github.attemper.core.ext.server;

import com.github.attemper.common.result.MapResult;
import com.github.attemper.common.util.FormatUtils;
import com.github.attemper.common.util.NumberUtil;
import oshi.SystemInfo;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;

import java.util.ArrayList;
import java.util.List;

/**
 * util for file store
 */
public class FileStoreUtil {

    public static List<MapResult<String, Object>> getFileStoreInfo(SystemInfo systemInfo) {
        FileSystem fileSystem = systemInfo.getOperatingSystem().getFileSystem();
        OSFileStore[] fileStores = fileSystem.getFileStores();
        List<MapResult<String, Object>> list = new ArrayList<>(fileStores.length);
        for (OSFileStore fs : fileStores) {
            list.add(new MapResult<>(fs.getMount(), FormatUtils.formatBytes(fs.getUsableSpace()) + '/' + FormatUtils.formatBytes(fs.getTotalSpace()) + '(' + NumberUtil.toPercentage(fs.getTotalSpace(), fs.getUsableSpace()) + ')'));
        }
        return list;
    }

}
