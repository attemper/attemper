package com.github.attemper.core.util;

import com.github.attemper.common.constant.GlobalConstants;
import com.github.attemper.common.exception.RTException;
import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class FileUtil {

    public static void byteArray2File(byte[] bytes, File targetFile) throws IOException {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(targetFile))){
            bos.write(bytes);
        }
    }

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

    public static void unZip(File inputFile, File outputFolder) throws IOException{
        if (!outputFolder.exists()) {
            outputFolder.mkdirs();
        }
        try (ZipFile zipFile = new ZipFile(inputFile);
             ZipInputStream zipInput = new ZipInputStream(new FileInputStream(inputFile));){
            ZipEntry entry;
            while ((entry = zipInput.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    String fileName = optimizePath(outputFolder.getAbsolutePath()) + '/' + optimizePath(entry.getName());
                    if (!new File(fileName).exists()) {
                        File folder = new File(fileName.substring(0, fileName.lastIndexOf('/')));
                        if (!folder.exists()) {
                            folder.mkdirs();
                        }
                        IOUtils.copy(zipFile.getInputStream(entry), new FileOutputStream(fileName));
                    }
                }
            }
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
     * /user/.../attemper/{id}
     *
     * @param id
     * @return
     */
    public static String joinUserFolder(String id) {
        return getUserHome() + GlobalConstants.defaultContextPath + '/' + id;
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
