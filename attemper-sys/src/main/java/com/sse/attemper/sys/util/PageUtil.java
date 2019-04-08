package com.sse.attemper.sys.util;

import com.github.pagehelper.Page;
import com.sse.attemper.common.constant.CommonConstants;
import com.sse.attemper.common.result.PageResult;

import java.util.HashMap;
import java.util.Map;

/**
 * 分页相关工具类
 * @author ldang
 */
public class PageUtil {

    /**
     * 将page对象转为map
     * @param page
     * @return
     */
    public static Map<String, Object> toResultMap(Page<?> page){
        Map<String, Object> resultMap = new HashMap<>(2);
        resultMap.put(CommonConstants.page, toPageResult(page));
        resultMap.put(CommonConstants.list, page.getResult());
        return resultMap;
    }

    /**
     * PageHelper的Page对象转PageResult
     * @param page
     * @return
     */
    private static PageResult toPageResult(Page<?> page){
        return new PageResult(page.getPageNum(), page.getPageSize(), page.getTotal());
    }
}
