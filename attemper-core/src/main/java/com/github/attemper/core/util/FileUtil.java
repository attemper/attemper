package com.github.attemper.core.util;

import com.github.attemper.common.exception.RTException;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {

    public static byte[] inputStreamAsByteArray(InputStream is) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[16 * 1024];
            int read;
            while ((read = is.read(buffer)) > 0) {
                os.write(buffer, 0, read);
            }
            return os.toByteArray();
        } catch (IOException e) {
            throw new RTException(1100, e);
        }
    }

    public static File newFile(String parentDirectory, String subDirectory, String fileName) {
        if (StringUtils.isBlank(fileName)) {
            throw new RTException(2570);
        }
        String directoryPath = getDirectoryPath(parentDirectory, subDirectory);
        File dir = new File(directoryPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(directoryPath + fileName);
        return file;
    }

    public static File getFile(String parentDirectory, String subDirectory, String fileName) {
        if (StringUtils.isBlank(fileName)) {
            throw new RTException(2570);
        }
        String directoryPath = getDirectoryPath(parentDirectory, subDirectory);
        File file = new File(directoryPath + fileName);
        if (!file.exists()) {
            throw new RTException(2569, directoryPath + fileName);
        }
        return file;
    }

    public static String getDirectoryPath(String parentDirectory, String subDirectory) {
        parentDirectory = StringUtils.isBlank(parentDirectory) ? "" : optimizePath(parentDirectory.trim());
        subDirectory = StringUtils.isBlank(subDirectory) ? "" : optimizePath(subDirectory.trim());
        if (!parentDirectory.endsWith("/") && !subDirectory.startsWith("/")) {
            parentDirectory += "/";
        }
        if (!subDirectory.endsWith("/")) {
            subDirectory += "/";
        }
        return parentDirectory + subDirectory;
    }

    /**
     *
     * @return /user/...
     */
    public static String getUserHome() {
        return optimizePath(System.getProperty("user.home"));
    }

    public static String optimizePath(String filePath) {
        return filePath == null ? null : filePath.replace("\\", "/");
    }
}
