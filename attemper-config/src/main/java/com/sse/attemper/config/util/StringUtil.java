package com.sse.attemper.config.util;

/**
 * @author ldang
 */
public class StringUtil {

    /**
     * 数组转json字符串
     * @param arr
     * @return
     */
    public static String formatToJsonStr(Object[] arr){
        if(arr == null || arr.length == 0){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for(Object cell : arr){
            sb.append((cell == null ? "" : cell.toString()) + ",");
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }
}
