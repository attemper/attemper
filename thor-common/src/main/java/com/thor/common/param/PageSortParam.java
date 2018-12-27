package com.thor.common.param;

import com.thor.common.constant.CommonConstants;

/**
 * 分页参数
 * @author ldang
 */
public class PageSortParam implements CommonParam {

    /**
     * 页号
     */
    protected int currentPage;

    /**
     * 每页条数
     */
    protected int pageSize;

    /**
     * 排序字段
     */
    protected String sort;

    /**
     * asc/desc
     */
    protected String direction;

    public PageSortParam(){

    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    @Override
    public String validate() {
        if(this.pageSize > CommonConstants.MAX_PAGE_SIZE){
            return "1502";
        }
        return null;
    }

    @Override
    public void preHandle() {
        //set default direction
        this.direction = CommonConstants.desc.equalsIgnoreCase(direction) ?
                CommonConstants.desc : CommonConstants.asc;
        //preview handle currentPage
        if(this.currentPage == 0){
            this.currentPage = CommonConstants.DEF_CURRENT_PAGE;
        }
        //preview handle pageSize
        if(this.pageSize == 0){
            this.pageSize = CommonConstants.DEF_PAGE_SIZE;
        }
    }
}
