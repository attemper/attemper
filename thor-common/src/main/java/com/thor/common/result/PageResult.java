package com.thor.common.result;

import java.io.Serializable;

/**
 * @author ldang
 */
public class PageResult implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 页号
     */
    protected int currentPage;

    /**
     * 每页条数
     */
    protected int pageSize;

    /**
     * 总条数
     * @return
     */
    protected long total;

    public PageResult() {
    }

    public PageResult(int currentPage, int pageSize, long total) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.total = total;
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

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
