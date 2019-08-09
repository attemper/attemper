package com.github.attemper.sys.util;

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
            boolean isWin = isWindows();
            while ((entry = zipInput.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    String fileName = outputFolder.getAbsolutePath() + File.separator + (isWin ? entry.getName().replace("/", File.separator) : entry.getName().replace("\\", File.separator));
                    File folder = new File(fileName.substring(0, fileName.lastIndexOf(File.separator)));
                    if (!folder.exists()) {
                        folder.mkdirs();
                    }
                    IOUtils.copy(zipFile.getInputStream(entry), new FileOutputStream(fileName));
                }
            }
        }
    }

    public static String getUserHomePath() {
        return System.getProperty("user.home");
    }

    public static boolean isWindows() {
        String os = System.getProperty("os.name");
        return os.toLowerCase().startsWith("win");
    }
}
