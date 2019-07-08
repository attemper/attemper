package com.github.attemper.sys.util;

import com.github.pagehelper.Page;
import com.github.attemper.common.constant.CommonConstants;
import com.github.attemper.common.result.PageResult;

import java.util.HashMap;
import java.util.Map;

public class PageUtil {

    /**
     * page to map
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
     * PageHelper.Page to PageResult
     * @param page
     * @return
     */
    private static PageResult toPageResult(Page<?> page){
        return new PageResult()
                .setCurrentPage(page.getPageNum())
                .setPageSize(page.getPageSize())
                .setTotal(page.getTotal());
    }
}
