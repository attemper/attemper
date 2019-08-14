package com.github.attemper.sys.util;

import com.github.attemper.common.constant.GlobalConstants;
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

    public static void unZip(File inputFile, File outputFolder) throws IOException{
        if (!outputFolder.exists()) {
            outputFolder.mkdirs();
        }
        try (ZipFile zipFile = new ZipFile(inputFile);
             ZipInputStream zipInput = new ZipInputStream(new FileInputStream(inputFile));){
            ZipEntry entry;
            while ((entry = zipInput.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    String fileName = outputFolder.getAbsolutePath().replace("\\", "/") + '/' + entry.getName().replace("\\", "/");
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

    /**
     * /user/.../attemper/{programName}
     *
     * @param programName
     * @return
     */
    public static String joinUserFolder(String programName) {
        return System.getProperty("user.home").replace("\\", "/") + '/' + GlobalConstants.defaultContextPath + '/' + programName;
    }

}
